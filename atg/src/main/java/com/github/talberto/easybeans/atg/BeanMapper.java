/**
 * Copyright 2014 Tomas Rodriguez 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License. 
 */

package com.github.talberto.easybeans.atg;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.basic.AccessibleInstantiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.nucleus.Nucleus;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemDescriptor;

import com.github.talberto.easybeans.api.MappingException;
import com.github.talberto.easybeans.api.RepositoryBean;
import com.github.talberto.easybeans.api.RepositoryId;
import com.github.talberto.easybeans.api.RepositoryProperty;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

/**
 * Maps a Bean to a RepositoryItem and the other way around
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class BeanMapper<T> {

  protected final static Logger sLog = LoggerFactory.getLogger(BeanMapper.class);
  protected final static Predicate<PropertyDescriptor> sNotRepositoryPropertyAnnotated = new Predicate<PropertyDescriptor>() {
    @Override
    public boolean apply(PropertyDescriptor pPropertyDesc) {
      Method reader = pPropertyDesc.getReadMethod();
      Method writer = pPropertyDesc.getWriteMethod();
      
      if(reader == null && writer == null) {
        return false;
      }
      
      RepositoryProperty readerAnnotation = reader != null ? reader.getAnnotation(RepositoryProperty.class) : null;
      RepositoryProperty writerAnnotation = writer != null ? writer.getAnnotation(RepositoryProperty.class) : null;
      
      if(readerAnnotation == null && writerAnnotation == null) {
        return false;
      }
      return true;
    }
  };
  
  protected final static Predicate<PropertyDescriptor> sRepositoryIdAnnotated = new Predicate<PropertyDescriptor>() {

    @Override
    public boolean apply(PropertyDescriptor pDescriptor) {
      Method reader = pDescriptor.getReadMethod();
      Method writer = pDescriptor.getWriteMethod();
      
      if(reader == null & writer == null) {
        return false;
      }
      
      if(reader != null && reader.getAnnotation(RepositoryId.class) != null) {
        return true;
      } else if(writer != null && writer.getAnnotation(RepositoryId.class) != null) {
        return true;
      } else {
        return false;
      }
    }
  };
  
  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
  /** Class of the mapped bean */
  protected Class<T> mType;
  /** Repository where the repository items of type <code>T</code> live */
  protected MutableRepository mRepository;
  /** Repository item descriptor */
  protected RepositoryItemDescriptor mItemDescriptor;
  protected Map<String, PropertyMapper> mPropMapperForBeanPropertyName = Maps.newHashMap();
  protected Map<String, PropertyMapper> mPropMapperForRepositoryPropertyName = Maps.newHashMap();
  /** Instantiator for the class <code>T</code> */
  protected ObjectInstantiator mInstantiator;
  /** Reference to the {@link NucleusEntityManager} */
  protected NucleusEntityManager mEntityManager;
  protected Nucleus mNucleus;
  protected PropertyDescriptor mIdDescriptor;
  
  public static <T> BeanMapper<T> create(NucleusEntityManager pEntityManager, Class<T> pType) {
    sLog.trace("Entering from({})", pType);
    return new BeanMapper<T>(pEntityManager, pType);
  }
  
  protected BeanMapper(NucleusEntityManager pEntityManager, Class<T> pType) {
    mLog.trace("RepositoryBeanMapper.<clinit>({}, {})", pEntityManager, pType);
    mEntityManager = pEntityManager;
    mType = pType;
    mInstantiator = new AccessibleInstantiator(pType);
    mLog.debug("Extracting metadata from class {}", pType);
    RepositoryBean repositoryBeanAnnotation = mType.getAnnotation(RepositoryBean.class);
    checkArgument(repositoryBeanAnnotation != null, "the type %s isn't annotated with @RepositoryBean", mType.getName());
    String repositoryPath = repositoryBeanAnnotation.repository();
    mRepository = (MutableRepository) getNucleus().resolveName(repositoryPath);
    checkNotNull(mRepository, "Repository %s doesn't exist", repositoryPath);
    String descriptorName = repositoryBeanAnnotation.descriptorName();
    try {
      mItemDescriptor = mRepository.getItemDescriptor(descriptorName);
    } catch (RepositoryException e1) {
      throw new IllegalArgumentException(String.format("Error building RepositoryBeanMapper for class [%s]", pType), e1);
    }
    mLog.debug("Metadata extracted for type [{}]: [repositoryPath=[{}], descriptorName=[{}]", pType, repositoryPath, descriptorName);
    
    mLog.debug("Scanning method annotations");
    BeanInfo beanInfo;
    try {
      beanInfo = Introspector.getBeanInfo(pType);
    } catch (IntrospectionException e) {
      throw new MappingException(String.format("Error extracting BeanInfo from %s", pType), e);
    }
    List<PropertyDescriptor> descriptors = ImmutableList.copyOf(beanInfo.getPropertyDescriptors());
    
    // Try to find the id property
    Collection<PropertyDescriptor> idDescriptors = Collections2.filter(descriptors, sRepositoryIdAnnotated);
    if(idDescriptors.isEmpty()) {
      throw new IllegalArgumentException(String.format("The type [%s] doesn't have any @RepositoryId annotated property", mType)); 
    } else if(idDescriptors.size() > 1) {
      throw new IllegalArgumentException(String.format("The type [%s] has more than one @RepositoryId annotated property", mType));
    } else {
      mIdDescriptor = idDescriptors.iterator().next();
    }
    
    // Remove descriptors that doesn't have any annotations neither on getter nor in setter    
    Collection<PropertyDescriptor> validBeanPropertyDescriptors = Collections2.filter(descriptors, sNotRepositoryPropertyAnnotated);
    
    if(validBeanPropertyDescriptors.isEmpty()) {
      mLog.warn("The type [{}] doesn't have any @RepositoryProperty annotated property", mType);
    }
    
    for(PropertyDescriptor property : validBeanPropertyDescriptors) {
      PropertyMapper propertyMapper = PropertyMapper.create(mEntityManager, property, mItemDescriptor);
      mPropMapperForBeanPropertyName.put(property.getName(), propertyMapper);
      mPropMapperForRepositoryPropertyName.put(propertyMapper.getRepositoryPropertyName(), propertyMapper);
    }
  }

  protected Nucleus getNucleus() {
    if(mNucleus == null) {
      mNucleus = Nucleus.getGlobalNucleus();
    }
    return mNucleus;
  }
  
  public T find(Object pPk) {
    mLog.trace("Entering find()");
    try {
      RepositoryItem item = mRepository.getItem(pPk.toString(), mItemDescriptor.getItemDescriptorName());
      
      if(item == null) {
        mLog.debug("Couldn't find any RepositoryItem with id [{}] in the repository [{}]. Returning null", pPk, mRepository);
        return null;
      } else {
        mLog.debug("Found RepositoryItem [{}] for id [{}] in Repository [{}]. Converting it to a Bean of type [{}]", item, pPk, mRepository, mType);
        return toBean(item);
      }
    } catch (RepositoryException e) {
      throw new IllegalArgumentException();
    }
  }
  
  public T toBean(RepositoryItem pItem) {
    mLog.trace("Entering toBean({})", pItem);
    mLog.debug("Creating new instance of type [{}]", mType);
    @SuppressWarnings("unchecked")
    T bean = (T) mInstantiator.newInstance();
    
    mLog.debug("Extracting all properties from the RepositoryItem [{}]", pItem);
    for(PropertyMapper propertyMapper : mPropMapperForBeanPropertyName.values()) {
      mLog.debug("Property mapper [{}] will map the property", propertyMapper);
      Object propertyValue = propertyMapper.mapRepositoryProperty(pItem);
      
      propertyMapper.setBeanProperty(bean, propertyValue);
    }
    
    // Set the bean id
    setBeanId(bean, pItem.getRepositoryId());
    
    return bean;
  }
  
  public String create(T pBean) {
    mLog.trace("Entering create({})", pBean);
    MutableRepositoryItem repositoryItem;
    try {
      repositoryItem = mRepository.createItem(mItemDescriptor.getItemDescriptorName());
    } catch (RepositoryException e) {
      throw new MappingException(String.format("Exception creating RepositoryItem from bean [%s]", pBean), e);
    }
    
    mLog.debug("Created new RepositoryItem [{}]", repositoryItem);
    
    mLog.debug("Extracting all properties from the Bean [{}]", pBean);
    for(PropertyMapper propertyMapper : mPropMapperForBeanPropertyName.values()) {
      propertyMapper.updateRepositoryItemProperty(repositoryItem, pBean);
    }
    
    // Update the repository with the newly created item
    RepositoryItem finalRepositoryItem;
    try {
      finalRepositoryItem = mRepository.addItem(repositoryItem);
    } catch (RepositoryException e) {
      throw new MappingException(String.format("Error inserting new RepositoryItem [%s]", repositoryItem), e);
    }
    
    // Set the id of the bean
    setBeanId(pBean, finalRepositoryItem.getRepositoryId());
    
    return finalRepositoryItem.getRepositoryId();
  }
  
  protected void setBeanId(T pBean, String pId) {
    try {
      mIdDescriptor.getWriteMethod().invoke(pBean, pId);
    } catch (Exception e) {
      throw new MappingException(String.format("Error setting bean id [%s] of bean [%s]", pId, pBean), e);
    }
  }

  protected String getBeanId(T pBean) {
    try {
      return (String) mIdDescriptor.getReadMethod().invoke(pBean);
    } catch (Exception e) {
      throw new MappingException(String.format("Error getting bean id of bean [%s]", pBean), e);
    }
  }
  
  public RepositoryItem toRepositoryItem(MutableRepositoryItem pItem, T pBean) {
    return null;
  }
  
  public void update(T pBean) {
    mLog.trace("Entering update({})", pBean);
    String repositoryId = getBeanId(pBean);
    checkNotNull(repositoryId, "The id of the bean [%s] is null", pBean);
    MutableRepositoryItem repositoryItem;
    try {
      repositoryItem = mRepository.getItemForUpdate(repositoryId, mItemDescriptor.getItemDescriptorName());
    } catch (RepositoryException e) {
      throw new MappingException(String.format("Exception updating RepositoryItem from bean [%s]", pBean), e);
    }
    
    mLog.debug("Got RepositoryItem for update[{}]", repositoryItem);
    
    mLog.debug("Extracting all properties from the Bean [{}]", pBean);
    for(PropertyMapper propertyMapper : mPropMapperForBeanPropertyName.values()) {
      propertyMapper.updateRepositoryItemProperty(repositoryItem, pBean);
    }
    
    // Update the repository with the item
    try {
      mRepository.updateItem(repositoryItem);
    } catch (RepositoryException e) {
      throw new MappingException(String.format("Error inserting new RepositoryItem [%s]", repositoryItem), e);
    }
  }
  
  public void delete(T pBean, boolean pDeleteNested) {
    mLog.trace("Entering delete({})", pBean);
    String repositoryId = getBeanId(pBean);
    
    mLog.debug("Deleting RepositoryItem [{}] of type [{}]", repositoryId, this.mItemDescriptor.getItemDescriptorName());
    try {
      mRepository.removeItem(repositoryId, this.mItemDescriptor.getItemDescriptorName());
      
      if(pDeleteNested) {
        for(PropertyMapper propertyMapper : mPropMapperForBeanPropertyName.values()) {
          propertyMapper.removeProperty(pBean);
        }
      }
    } catch (RepositoryException e) {
      throw new MappingException(String.format("Error removing item [%s] of type [%s] from Repository [%s]", repositoryId, this.mItemDescriptor.getItemDescriptorName(), mRepository), e);
    }
  }
  
  /**
   * @return the type
   */
  public Class<T> getType() {
    return mType;
  }

  protected RepositoryItem repositoryItemForBean(Object pBean) {
    checkArgument(pBean.getClass().equals(mType), "The bean pBean is of type [%s] whereas this mapper is of type [%s]", pBean.getClass(), mType);
    
    @SuppressWarnings("unchecked")
    T castedBean = (T) pBean;
    try {
      return mRepository.getItem(getBeanId(castedBean), mItemDescriptor.getItemDescriptorName());
    } catch (RepositoryException e) {
      throw new MappingException(String.format("Error getting a RepositoryItem for bean [%s]", castedBean), e);
    }
  }
}
