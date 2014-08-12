package org.easybeans.atg;

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

import atg.beans.DynamicPropertyDescriptor;
import atg.nucleus.Nucleus;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemDescriptor;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

/**
 * Maps a RepositoryBean to a RepositoryItem and the other way around
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class RepositoryBeanMapper<T> {

  protected final static Logger sLog = LoggerFactory.getLogger(RepositoryBeanMapper.class);
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
  protected Map<String, RepositoryPropertyMapper> mRepositoryDescForBeanPropertyName = Maps.newHashMap();
  protected Map<String, RepositoryPropertyMapper> mRepositoryDescForRepositoryPropertyName = Maps.newHashMap();
  /** Instantiator for the class <code>T</code> */
  protected ObjectInstantiator mInstantiator;
  /** Reference to the {@link NucleusEntityManager} */
  protected NucleusEntityManager mEntityManager;
  protected Nucleus mNucleus;
  protected PropertyDescriptor mIdDescriptor;
  
  public static <T> RepositoryBeanMapper<T> from(NucleusEntityManager pEntityManager, Class<T> pType) {
    sLog.trace("Entering from({})", pType);
    return new RepositoryBeanMapper<T>(pEntityManager, pType);
  }
  
  protected RepositoryBeanMapper(NucleusEntityManager pEntityManager, Class<T> pType) {
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
    }
    
    // Remove descriptors that doesn't have any annotations neither on getter nor in setter    
    Collection<PropertyDescriptor> repositoryPropertyDescriptors = Collections2.filter(descriptors, sNotRepositoryPropertyAnnotated);
    
    if(repositoryPropertyDescriptors.isEmpty()) {
      mLog.warn("The type [{}] doesn't have any @RepositoryProperty annotated property", mType);
    }
    
    for(PropertyDescriptor property : repositoryPropertyDescriptors) {
      RepositoryPropertyMapper propertyMapper = RepositoryPropertyMapper.from(property);
      mRepositoryDescForBeanPropertyName.put(property.getName(), propertyMapper);
      mRepositoryDescForRepositoryPropertyName.put(propertyMapper.getRepositoryPropertyName(), propertyMapper);
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
    for(RepositoryPropertyMapper propertyMapper : mRepositoryDescForBeanPropertyName.values()) {
      String repositoryPropertyName = propertyMapper.getRepositoryPropertyName();
      mLog.debug("Mapping repository property [{}] to bean property [{}]", repositoryPropertyName, propertyMapper.getBeanPropertyName());
      DynamicPropertyDescriptor propertyDescriptor;
      try {
        propertyDescriptor = pItem.getItemDescriptor().getPropertyDescriptor(repositoryPropertyName);
      } catch (RepositoryException e) {
        throw new IllegalArgumentException(e);
      }
      
      if(propertyDescriptor == null) {
        throw new IllegalArgumentException(String.format("The property [%s] doesn't exist for the RepositoryItem [%s]", repositoryPropertyName, pItem));
      }
      
      Object repositoryPropertyValue;
      if(propertyDescriptor.getPropertyType() == RepositoryItem.class) {
        mLog.debug("Repository property is of type RepositoryItem, asking NucleusEntityManager to map this property");
        RepositoryItem nestedItem = (RepositoryItem) pItem.getPropertyValue(repositoryPropertyName);
        repositoryPropertyValue = mEntityManager.find(propertyMapper.getPropertyBeanType(), nestedItem.getRepositoryId());
      } else {
        repositoryPropertyValue = pItem.getPropertyValue(repositoryPropertyName);
      }
      mLog.debug("The value of the property is [{}], setting it in the bean", repositoryPropertyValue);
      propertyMapper.setBeanProperty(bean, repositoryPropertyValue);
    }
    
    return bean;
  }
  
  public String create(T pItem) {
    mLog.trace("Entering create({})", pItem);
    MutableRepositoryItem repositoryItem;
    try {
      repositoryItem = mRepository.createItem(mItemDescriptor.getItemDescriptorName());
    } catch (RepositoryException e) {
      throw new MappingException(String.format("Exception creating RepositoryItem from bean [%s]", pItem), e);
    }
    
    mLog.debug("Created new RepositoryItem [{}]", repositoryItem);
    
    mLog.debug("Extracting all properties from the Bean [{}]", pItem);
    for(RepositoryPropertyMapper propertyMapper : mRepositoryDescForBeanPropertyName.values()) {
      String repositoryPropertyName = propertyMapper.getRepositoryPropertyName();
      String beanPropertyName = propertyMapper.getBeanPropertyName();
      mLog.debug("Mapping repository property [{}] to bean property [{}]", repositoryPropertyName, beanPropertyName);
      DynamicPropertyDescriptor propertyDescriptor;
      try {
        propertyDescriptor = repositoryItem.getItemDescriptor().getPropertyDescriptor(repositoryPropertyName);
      } catch (RepositoryException e) {
        throw new IllegalArgumentException(e);
      }
      
      if(propertyDescriptor == null) {
        throw new IllegalArgumentException(String.format("The property [%s] doesn't exist for the RepositoryItem [%s]", repositoryPropertyName, pItem));
      }
      
      Object beanPropertyValue = propertyMapper.getBeanProperty(pItem, beanPropertyName);
      if(propertyDescriptor.getPropertyType() == RepositoryItem.class) {
        mLog.debug("Repository property is of type RepositoryItem, asking NucleusEntityManager to map this property");
        String nestedRepositoryItemId = mEntityManager.create(beanPropertyValue);
        // Assume the nested repository item belongs to the same repository
        try {
          RepositoryItem nestedRepositoryItem = mRepository.getItem(nestedRepositoryItemId, repositoryPropertyName);
          repositoryItem.setPropertyValue(repositoryPropertyName, nestedRepositoryItem);
        } catch (RepositoryException e) {
          throw new MappingException(String.format("Error mapping bean property [%s] to repository property [%s]", beanPropertyName, repositoryPropertyName), e);
        }
      } else {
        repositoryItem.setPropertyValue(repositoryPropertyName, beanPropertyValue);
      }
    }
    
    // Update the repository with the newly created item
    RepositoryItem finalRepositoryItem;
    try {
      finalRepositoryItem = mRepository.addItem(repositoryItem);
    } catch (RepositoryException e) {
      throw new MappingException(String.format("Error inserting new RepositoryItem [%s]", repositoryItem), e);
    }
    
    return finalRepositoryItem.getRepositoryId();
  }
  
  public RepositoryItem toRepositoryItem(MutableRepositoryItem pItem, T pBean) {
    return null;
  }
  
  /**
   * @return the type
   */
  public Class<T> getType() {
    return mType;
  }
}
