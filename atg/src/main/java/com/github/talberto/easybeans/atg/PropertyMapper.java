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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.adapter.gsa.EnumPropertyDescriptor;
import atg.adapter.gsa.EnumPropertyDescriptor.EnumeratedOption;
import atg.beans.DynamicPropertyDescriptor;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemDescriptor;

import com.github.talberto.easybeans.api.MappingException;
import com.github.talberto.easybeans.api.RepositoryEnumCode;
import com.github.talberto.easybeans.api.RepositoryEnumValue;
import com.github.talberto.easybeans.api.RepositoryProperty;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * A PropertyMapper handles the mapping between a given property from a Bean to a RepositoryItem and the other way around.
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public abstract class PropertyMapper {

  protected final static Logger sLog = LoggerFactory.getLogger(BeanMapper.class);
  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
  protected Method mReader;
  protected Method mWriter;
  protected PropertyDescriptor mBeanPropertyDescriptor;
  protected DynamicPropertyDescriptor mRepositoryPropertyDescriptor;
  protected NucleusEntityManager mEntityManager;
  public static PropertyMapper create(NucleusEntityManager pEntityManager, PropertyDescriptor pBeanPropertyDescriptor, RepositoryItemDescriptor pItemDescriptor) {
    sLog.debug("Extracting information from property [{}]", pBeanPropertyDescriptor.getName());
    Method reader = pBeanPropertyDescriptor.getReadMethod();
    Method writer = pBeanPropertyDescriptor.getWriteMethod();

    checkArgument(reader != null || writer != null, "Neither reader nor writer for property [{}]", pBeanPropertyDescriptor.getName());
    
    RepositoryProperty readerAnnotation = reader != null ? reader.getAnnotation(RepositoryProperty.class) : null;
    RepositoryProperty writerAnnotation = writer != null ? writer.getAnnotation(RepositoryProperty.class) : null;
    
    checkArgument(readerAnnotation != null || writerAnnotation != null, "Neither reader nor writer are annotated with @RepositoryProperty for property {}", pBeanPropertyDescriptor.getName());
    checkArgument(!(readerAnnotation != null && writerAnnotation != null), "Both reader and writer are annotated with @RepositoryProperty for property {}", pBeanPropertyDescriptor.getName());
    
    boolean configureWithReader = readerAnnotation == null ? false : true;
    Method methodToUse = configureWithReader ? reader : writer;
    RepositoryProperty propertyAnnotation = configureWithReader ? readerAnnotation : writerAnnotation;
    
    if(configureWithReader) {
      sLog.debug("Configuring property using reader method");
    } else {
      sLog.debug("Configuring property using writer method");
    }

    propertyAnnotation = methodToUse.getAnnotation(RepositoryProperty.class);
    // Extract the repository property descriptor
    DynamicPropertyDescriptor repositoryPropertyDescriptor = pItemDescriptor.getPropertyDescriptor(propertyAnnotation.propertyName());
    checkNotNull(repositoryPropertyDescriptor, "The repository property descriptor for the property [%s] is null", pBeanPropertyDescriptor.getName());
    sLog.debug("Property configured: [propertyName=[{}]]", repositoryPropertyDescriptor.getName());
    
    if(repositoryPropertyDescriptor instanceof EnumPropertyDescriptor) {
      return new EnumPropertyMapper(pEntityManager, pBeanPropertyDescriptor, repositoryPropertyDescriptor);
    } else {
      PropertyGetter getter = PropertyGetter.create(pEntityManager, pBeanPropertyDescriptor, repositoryPropertyDescriptor);
      PropertySetter setter = PropertySetter.create(pEntityManager, pBeanPropertyDescriptor, repositoryPropertyDescriptor);
      PropertyDeleter deleter = PropertyDeleter.create(pEntityManager, pBeanPropertyDescriptor, repositoryPropertyDescriptor);
      return new RegularPropertyMapper(pEntityManager, pBeanPropertyDescriptor, repositoryPropertyDescriptor, getter, setter, deleter);
    }
  }

  static abstract class PropertyGetter {
    protected NucleusEntityManager mEntityManager;
    protected Class<?> mType;
    
    public static PropertyGetter create(NucleusEntityManager pEntityManager, PropertyDescriptor pBeanPropertyDescriptor, DynamicPropertyDescriptor pItemPropertyDescriptor) {
      Class<?> repositoryClass = pItemPropertyDescriptor.getPropertyType();
      Class<?> repositoryComponentClass = pItemPropertyDescriptor.getComponentPropertyType();
      Class<?> beanClass = pBeanPropertyDescriptor.getPropertyType();
      Class<?> beanComponentClass = null;
      
      if(Collection.class.isAssignableFrom(beanClass)) {
        // User getter or setter to obtain the component's type
        if(pBeanPropertyDescriptor.getReadMethod() != null) {
          Type returnType = pBeanPropertyDescriptor.getReadMethod().getGenericReturnType();
          ParameterizedType paramType = (ParameterizedType) returnType;
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[0];
        } else {
          Type[] types = pBeanPropertyDescriptor.getWriteMethod().getGenericParameterTypes();
          ParameterizedType paramType = (ParameterizedType) types[0];
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[0];
        }
      } else if(Map.class.isAssignableFrom(beanClass)) {
        // User getter or setter to obtain the component's type
        if(pBeanPropertyDescriptor.getReadMethod() != null) {
          Type returnType = pBeanPropertyDescriptor.getReadMethod().getGenericReturnType();
          ParameterizedType paramType = (ParameterizedType) returnType;
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[1];
        } else {
          Type[] types = pBeanPropertyDescriptor.getWriteMethod().getGenericParameterTypes();
          ParameterizedType paramType = (ParameterizedType) types[0];
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[1];
        } 
      }
      
      sLog.debug("Creating PropertyGetter. repositoryClass = [{}], repositoryComponentClass = [{}], beanClass = [{}], beanComponentClass = [{}]", 
          repositoryClass, repositoryComponentClass, beanClass, beanComponentClass);
      
      if(Collection.class.isAssignableFrom(repositoryClass)) {
        if(repositoryComponentClass.equals(RepositoryItem.class)) {
          return CollectionPropertyGetterDecorator.decorate(repositoryClass, 
              new RepositoryItemPropertyGetter(pEntityManager, beanComponentClass)
              );
        } else {
          return CollectionPropertyGetterDecorator.decorate(repositoryClass, 
              new SimplePropertyGetter(pEntityManager, beanComponentClass)
              );
        }
      } else if(Map.class.isAssignableFrom(repositoryClass)) {
        if(repositoryComponentClass.equals(RepositoryItem.class)) {
          return MapPropertyGetterDecorator.decorate(
              new RepositoryItemPropertyGetter(pEntityManager, beanComponentClass)
              );
        } else {
          return MapPropertyGetterDecorator.decorate(
              new SimplePropertyGetter(pEntityManager, beanComponentClass)
              );
        }
      } else if(RepositoryItem.class.isAssignableFrom(repositoryClass)) {
        return new RepositoryItemPropertyGetter(pEntityManager, beanClass);
      } else {
        return new SimplePropertyGetter(pEntityManager, beanClass);
      }
    }
    
    protected PropertyGetter(NucleusEntityManager pEntityManager, Class<?> pType) {
      mEntityManager = pEntityManager;
      mType = pType;
    }
    
    public Object extractValue(RepositoryItem pItem, String pPropertyName) {
      return processValue(pItem.getPropertyValue(pPropertyName), mType);
    }
    
    protected abstract Object processValue(Object pPropertyValue, Class<?> pType);
  }
  
  static class SimplePropertyGetter extends PropertyGetter {
    public SimplePropertyGetter(NucleusEntityManager pEntityManager, Class<?> pType) {
      super(pEntityManager, pType);
    }

    @Override
    protected Object processValue(Object pPropertyValue, Class<?> pType) {
      return pType.cast(pPropertyValue);
    }
  }
  
  static class RepositoryItemPropertyGetter extends PropertyGetter {
    public RepositoryItemPropertyGetter(NucleusEntityManager pEntityManager, Class<?> pType) {
      super(pEntityManager, pType);
    }

    @Override
    protected Object processValue(Object pPropertyValue, Class<?> pType) {
      RepositoryItem item = (RepositoryItem) pPropertyValue;
      
      return mEntityManager.toBean(item, pType);
    }
  }
  
  static class CollectionPropertyGetterDecorator extends PropertyGetter {
    protected final Function<Object, Object> mTransformer = new Function<Object, Object>() {
      @Override
      public Object apply(Object pInput) {
        return mDecorated.processValue(pInput, mType);
      }
    };
    
    protected PropertyGetter mDecorated;
    protected CollectionCreator mCollectionCreator;
    
    public static PropertyGetter decorate(Class<?> pCollectionType, PropertyGetter pDecorated) {
      sLog.debug("Decorating PropertyGetter [{}] with collection type [{}]", pDecorated, pCollectionType);
      return new CollectionPropertyGetterDecorator(pDecorated, CollectionCreator.forCollection(pCollectionType));
    }
    
    protected CollectionPropertyGetterDecorator(PropertyGetter pDecorated, CollectionCreator pCollectionCreator) {
      super(pDecorated.mEntityManager, pDecorated.mType);
      mDecorated = pDecorated;
      mCollectionCreator = pCollectionCreator;
    }

    @Override
    protected Object processValue(Object pPropertyValue, Class<?> pType) {
      @SuppressWarnings("unchecked")
      Collection<Object> originalCollection = (Collection<Object>) pPropertyValue;
      Collection<Object> targetCollection = Collections2.transform(originalCollection, mTransformer);
      return mCollectionCreator.create(targetCollection);
    }
  }
  
  static class MapPropertyGetterDecorator extends PropertyGetter {
    protected final Function<Object, Object> mTransformer = new Function<Object, Object>() {
      @Override
      public Object apply(Object pInput) {
        return mDecorated.processValue(pInput, mType);
      }
    };
    
    protected PropertyGetter mDecorated;
    
    public static PropertyGetter decorate(PropertyGetter pDecorated) {
      sLog.debug("Decorating PropertyGetter [{}]", pDecorated);
      return new MapPropertyGetterDecorator(pDecorated);
    }
    
    protected MapPropertyGetterDecorator(PropertyGetter pDecorated) {
      super(pDecorated.mEntityManager, pDecorated.mType);
      mDecorated = pDecorated;
    }

    @Override
    protected Object processValue(Object pPropertyValue, Class<?> pType) {
      @SuppressWarnings("unchecked")
      Map<String, Object> originalMap = (Map<String, Object>) pPropertyValue;
      Map<String, Object> transformedMap = Maps.transformValues(originalMap, mTransformer);
      return Maps.newHashMap(transformedMap);
    }
  }
  
  static abstract class CollectionCreator {
    public static Map<Class<?>, CollectionCreator> CREATOR_FOR_COLLECTION = ImmutableMap.<Class<?>, CollectionCreator>builder()
        .put(Set.class, SetCreator.instance)
        .put(List.class, ListCreator.instance)
        .build();
    
    public static CollectionCreator forCollection(Class<?> pCollectionClass) {
      CollectionCreator creator = CREATOR_FOR_COLLECTION.get(pCollectionClass);
      
      checkNotNull(creator, "CollectionCreator for collection [%s] is null", pCollectionClass);
      
      return creator;
    }
    
    public abstract Collection<?> create();
    
    public abstract Collection<?> create(Collection<?> pElements);
  }
  
  static class SetCreator extends CollectionCreator {
    public static CollectionCreator instance = new SetCreator();
    
    private SetCreator() {
      
    }
    
    @Override
    public Collection<?> create() {
      return Sets.newHashSet();
    }

    @Override
    public Collection<?> create(Collection<?> pElements) {
      return Sets.newHashSet(pElements);
    }
  }
  
  static class ListCreator extends CollectionCreator {
    public static CollectionCreator instance = new ListCreator();
    
    private ListCreator() {
      
    }
    
    @Override
    public Collection<?> create() {
      return Lists.newArrayList();
    }

    @Override
    public Collection<?> create(Collection<?> pElements) {
      return Lists.newArrayList(pElements);
    }
  }
 
  static abstract class PropertySetter {
    protected NucleusEntityManager mEntityManager;
    
    public static PropertySetter create(NucleusEntityManager pEntityManager, PropertyDescriptor pBeanPropertyDescriptor, DynamicPropertyDescriptor pItemPropertyDescriptor) {
      Class<?> repositoryClass = pItemPropertyDescriptor.getPropertyType();
      Class<?> repositoryComponentClass = pItemPropertyDescriptor.getComponentPropertyType();
      Class<?> beanClass = pBeanPropertyDescriptor.getPropertyType();
      Class<?> beanComponentClass = null;
      
      if(Collection.class.isAssignableFrom(beanClass)) {
        // User getter or setter to obtain the component's type
        if(pBeanPropertyDescriptor.getReadMethod() != null) {
          Type returnType = pBeanPropertyDescriptor.getReadMethod().getGenericReturnType();
          ParameterizedType paramType = (ParameterizedType) returnType;
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[0];
        } else {
          Type[] types = pBeanPropertyDescriptor.getWriteMethod().getGenericParameterTypes();
          ParameterizedType paramType = (ParameterizedType) types[0];
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[0];
        }
      } else if(Map.class.isAssignableFrom(beanClass)) {
        // User getter or setter to obtain the component's type
        if(pBeanPropertyDescriptor.getReadMethod() != null) {
          Type returnType = pBeanPropertyDescriptor.getReadMethod().getGenericReturnType();
          ParameterizedType paramType = (ParameterizedType) returnType;
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[1];
        } else {
          Type[] types = pBeanPropertyDescriptor.getWriteMethod().getGenericParameterTypes();
          ParameterizedType paramType = (ParameterizedType) types[0];
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[1];
        } 
      }
      
      sLog.debug("Creating PropertySetter. repositoryClass = [{}], repositoryComponentClass = [{}], beanClass = [{}], beanComponentClass = [{}]", 
          repositoryClass, repositoryComponentClass, beanClass, beanComponentClass);
      
      if(Collection.class.isAssignableFrom(repositoryClass)) {
        if(repositoryComponentClass.equals(RepositoryItem.class)) {
          return CollectionPropertySetterDecorator.decorate(repositoryClass, 
              new BeanPropertySetter(pEntityManager)
              );
        } else {
          return CollectionPropertySetterDecorator.decorate(repositoryClass, 
              new SimplePropertySetter(pEntityManager)
              );
        }
      } else if(Map.class.isAssignableFrom(repositoryClass)) {
        if(repositoryComponentClass.equals(RepositoryItem.class)) {
          return MapPropertySetterDecorator.decorate(
              new BeanPropertySetter(pEntityManager)
              );
        } else {
          return MapPropertySetterDecorator.decorate(
              new SimplePropertySetter(pEntityManager)
              );
        }
      } else if(RepositoryItem.class.isAssignableFrom(repositoryClass)) {
        return new BeanPropertySetter(pEntityManager);
      } else {
        return new SimplePropertySetter(pEntityManager);
      }
    }
    
    public PropertySetter(NucleusEntityManager pEntityManager) {
      mEntityManager = pEntityManager;
    }

    public void setItemProperty(MutableRepositoryItem pItem, String pPropertyName, Object pPropertyValue) {
      pItem.setPropertyValue(pPropertyName, processValue(pPropertyValue));
    }
    
    public abstract Object processValue(Object pPropertyValue);
  }
  
  static class SimplePropertySetter extends PropertySetter {
    public SimplePropertySetter(NucleusEntityManager pEntityManager) {
      super(pEntityManager);
    }

    @Override
    public Object processValue(Object pPropertyValue) {
      return pPropertyValue;
    }
  }
  
  static class BeanPropertySetter extends PropertySetter {
    public BeanPropertySetter(NucleusEntityManager pEntityManager) {
      super(pEntityManager);
    }

    @Override
    public Object processValue(Object pBean) {
      @SuppressWarnings("unchecked")
      BeanMapper<Object> beanMapper = (BeanMapper<Object>) mEntityManager.findMapperFor(pBean.getClass());
      String beanId = beanMapper.getBeanId(pBean);
      
      if(beanId == null) {
        // Create new repository item
        beanId = mEntityManager.create(pBean);
        beanMapper.setBeanId(pBean, beanId);
      } else {
        mEntityManager.update(pBean);
      }
      
      return mEntityManager.repositoryItemForBean(pBean);
    }
  }
  
  static class CollectionPropertySetterDecorator extends PropertySetter {
    protected final Function<Object, Object> sTransformPropertyValue = new Function<Object, Object>() {
      @Override
      public Object apply(Object pPropertyValue) {
        return mDecorated.processValue(pPropertyValue);
      }
    };
    
    protected PropertySetter mDecorated;
    protected CollectionCreator mCollectionCreator;
    
    public static PropertySetter decorate(Class<?> pCollectionType, PropertySetter pDecorated) {
      sLog.debug("Decorating PropertyGetter [{}] with collection type [{}]", pDecorated, pCollectionType);
      return new CollectionPropertySetterDecorator(pDecorated, CollectionCreator.forCollection(pCollectionType));
    }
    
    protected CollectionPropertySetterDecorator(PropertySetter pDecorated, CollectionCreator pCollectionCreator) {
      super(pDecorated.mEntityManager);
      mDecorated = pDecorated;
      mCollectionCreator = pCollectionCreator;
    }

    @Override
    public Object processValue(Object pPropertyValue) {
      @SuppressWarnings("unchecked")
      Collection<Object> beanCollection = (Collection<Object>) pPropertyValue;
      Collection<Object> transformedCollection = Collections2.transform(beanCollection, sTransformPropertyValue);
      return mCollectionCreator.create(transformedCollection);
    }
  }
  
  static class MapPropertySetterDecorator extends PropertySetter {
    protected final Function<Object, Object> mTransformPropertyValue = new Function<Object, Object>() {
      @Override
      public Object apply(Object pPropertyValue) {
        return mDecorated.processValue(pPropertyValue);
      }
    };
    
    protected PropertySetter mDecorated;
    protected CollectionCreator mCollectionCreator;
    
    public static PropertySetter decorate(PropertySetter pDecorated) {
      sLog.debug("Decorating PropertyGetter [{}]", pDecorated);
      return new MapPropertySetterDecorator(pDecorated);
    }
    
    protected MapPropertySetterDecorator(PropertySetter pDecorated) {
      super(pDecorated.mEntityManager);
      mDecorated = pDecorated;
    }

    @Override
    public Object processValue(Object pPropertyValue) {
      @SuppressWarnings("unchecked")
      Map<String, Object> originalMap =  (Map<String, Object>) pPropertyValue;
      Map<String, Object> transformedMap = Maps.transformValues(originalMap, mTransformPropertyValue); 
          
      return Maps.newHashMap(transformedMap);
    }
  }
  
  static abstract class PropertyDeleter {
    protected NucleusEntityManager mEntityManager;
    
    public static PropertyDeleter create(NucleusEntityManager pEntityManager, PropertyDescriptor pBeanPropertyDescriptor, DynamicPropertyDescriptor pItemPropertyDescriptor) {
      Class<?> repositoryClass = pItemPropertyDescriptor.getPropertyType();
      Class<?> repositoryComponentClass = pItemPropertyDescriptor.getComponentPropertyType();
      Class<?> beanClass = pBeanPropertyDescriptor.getPropertyType();
      Class<?> beanComponentClass = null;
      
      if(Collection.class.isAssignableFrom(beanClass)) {
        // User getter or setter to obtain the component's type
        if(pBeanPropertyDescriptor.getReadMethod() != null) {
          Type returnType = pBeanPropertyDescriptor.getReadMethod().getGenericReturnType();
          ParameterizedType paramType = (ParameterizedType) returnType;
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[0];
        } else {
          Type[] types = pBeanPropertyDescriptor.getWriteMethod().getGenericParameterTypes();
          ParameterizedType paramType = (ParameterizedType) types[0];
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[0];
        }
      } else if(Map.class.isAssignableFrom(beanClass)) {
        // User getter or setter to obtain the component's type
        if(pBeanPropertyDescriptor.getReadMethod() != null) {
          Type returnType = pBeanPropertyDescriptor.getReadMethod().getGenericReturnType();
          ParameterizedType paramType = (ParameterizedType) returnType;
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[1];
        } else {
          Type[] types = pBeanPropertyDescriptor.getWriteMethod().getGenericParameterTypes();
          ParameterizedType paramType = (ParameterizedType) types[0];
          beanComponentClass = (Class<?>) paramType.getActualTypeArguments()[1];
        } 
      }
      
      sLog.debug("Creating PropertyDeleter. repositoryClass = [{}], repositoryComponentClass = [{}], beanClass = [{}], beanComponentClass = [{}]", 
          repositoryClass, repositoryComponentClass, beanClass, beanComponentClass);
      
      if(Collection.class.isAssignableFrom(repositoryClass)) {
        if(repositoryComponentClass.equals(RepositoryItem.class)) {
          return CollectionPropertyDeleterDecorator.decorate(repositoryClass, 
              new BeanPropertyDeleter(pEntityManager)
              );
        } else {
          return new NoOpPropertyDeleter(pEntityManager);
        }
      } else if(Map.class.isAssignableFrom(repositoryClass)) {
        if(repositoryComponentClass.equals(RepositoryItem.class)) {
          return MapPropertyDeleterDecorator.decorate(
              new BeanPropertyDeleter(pEntityManager)
              );
        } else {
          return new NoOpPropertyDeleter(pEntityManager);
        }
      } else if(RepositoryItem.class.isAssignableFrom(repositoryClass)) {
        return new BeanPropertyDeleter(pEntityManager);
      } else {
        return new NoOpPropertyDeleter(pEntityManager);
      }
    }
    
    protected PropertyDeleter(NucleusEntityManager pEntityManager) {
      mEntityManager = pEntityManager;
    }

    public abstract void delete(Object pPropertyValue);
  }
  
  static class NoOpPropertyDeleter extends PropertyDeleter {
    protected NoOpPropertyDeleter(NucleusEntityManager pEntityManager) {
      super(pEntityManager);
    }

    @Override
    public void delete(Object pPropertyValue) {
    } 
  }
  
  static class BeanPropertyDeleter extends PropertyDeleter {
    protected BeanPropertyDeleter(NucleusEntityManager pEntityManager) {
      super(pEntityManager);
    }

    @Override
    public void delete(Object pBean) {
      mEntityManager.delete(pBean, true);
    }
  }
  
  static class CollectionPropertyDeleterDecorator extends PropertyDeleter {
    protected PropertyDeleter mDecorated;
    protected CollectionCreator mCollectionCreator;

    public static PropertyDeleter decorate(Class<?> pCollectionType, PropertyDeleter pDecorated) {
      sLog.debug("Decorating PropertyDeleter [{}] with collection type [{}]", pDecorated, pCollectionType);
      return new CollectionPropertyDeleterDecorator(pDecorated, CollectionCreator.forCollection(pCollectionType));
    }
    
    protected CollectionPropertyDeleterDecorator(PropertyDeleter pDecorated, CollectionCreator pCollectionCreator) {
      super(pDecorated.mEntityManager);
      mDecorated = pDecorated;
      mCollectionCreator = pCollectionCreator;
    }

    @Override
    public void delete(Object pPropertyValue) {
      @SuppressWarnings("unchecked")
      Collection<Object> beanCollection = (Collection<Object>) pPropertyValue;
      for(Object bean : beanCollection) {
        mDecorated.delete(bean);
      }
    }
  }
  
  static class MapPropertyDeleterDecorator extends PropertyDeleter {
    protected PropertyDeleter mDecorated;
    protected CollectionCreator mCollectionCreator;

    public static PropertyDeleter decorate(PropertyDeleter pDecorated) {
      sLog.debug("Decorating PropertyDeleter [{}]", pDecorated);
      return new MapPropertyDeleterDecorator(pDecorated);
    }
    
    protected MapPropertyDeleterDecorator(PropertyDeleter pDecorated) {
      super(pDecorated.mEntityManager);
      mDecorated = pDecorated;
    }

    @Override
    public void delete(Object pPropertyValue) {
      @SuppressWarnings("unchecked")
      Map<String, Object> originalMap = (Map<String, Object>) pPropertyValue;
      for(Object bean : originalMap.values()) {
        mDecorated.delete(bean);
      }
    }
  }
  
  protected PropertyMapper(NucleusEntityManager pEntityManager, PropertyDescriptor pBeanPropertyDescriptor, DynamicPropertyDescriptor pRepositoryPropertyDescriptor) {
    mEntityManager = pEntityManager;
    mBeanPropertyDescriptor = pBeanPropertyDescriptor;
    mRepositoryPropertyDescriptor = pRepositoryPropertyDescriptor;
    mReader = mBeanPropertyDescriptor.getReadMethod();
    mWriter = mBeanPropertyDescriptor.getWriteMethod();
  }

  /**
   * @return the repositoryPropertyName
   */
  public String getRepositoryPropertyName() {
    return mRepositoryPropertyDescriptor.getName();
  }
  
  public String getBeanPropertyName() {
    return mBeanPropertyDescriptor.getName();
  }

  public void setBeanProperty(Object pBean, Object pPropertyValue) {
    try {
      mWriter.invoke(pBean, pPropertyValue);
    } catch (Exception e) {
      throw new MappingException(String.format("Couldn't set bean property [%s] with value [%s]", mBeanPropertyDescriptor.getName(), pPropertyValue), e);
    }
  }
  
  public Object getBeanProperty(Object pBean) {
    try {
      return mReader.invoke(pBean);
    } catch(Exception e) {
      throw new MappingException(String.format("Couldn't get bean property [%s]", mBeanPropertyDescriptor.getName()), e);
    }
  }
  
  public Class<?> getPropertyBeanType() {
    return mBeanPropertyDescriptor.getPropertyType();
  }

  /**
   * @return the repositoryPropertyDescriptor
   */
  public DynamicPropertyDescriptor getRepositoryPropertyDescriptor() {
    return mRepositoryPropertyDescriptor;
  }

  public abstract void removeProperty(Object pBean);

  public abstract void updateRepositoryItemProperty(MutableRepositoryItem pItem, Object pBean);

  public abstract Object mapRepositoryProperty(RepositoryItem pItem);
}

class EnumPropertyMapper extends PropertyMapper {
  protected final static Predicate<Method> sIsCodeGetterCandidate = new Predicate<Method>() {
    @Override
    public boolean apply(Method pMethod) {
      return pMethod.isAnnotationPresent(RepositoryEnumCode.class) && // Check it has the corresponding annotation
          !Modifier.isStatic(pMethod.getModifiers()); // Check it isn't static
    }  
  };
  protected final static Predicate<Method> sIsValueGetterCandidate = new Predicate<Method>() {
    @Override
    public boolean apply(Method pMethod) {
      // Check it has the corresponding annotation
      return pMethod.isAnnotationPresent(RepositoryEnumValue.class) && // Check it has the corresponding annotation
          !Modifier.isStatic(pMethod.getModifiers()); // Check it isn't static
    }  
  };
  protected final static Predicate<Method> sIsFromCodeCandidate = new Predicate<Method>() {
    @Override
    public boolean apply(Method pMethod) {
      // Check it has the corresponding annotation
      return pMethod.isAnnotationPresent(RepositoryEnumCode.class) && // Check it has the corresponding annotation
          Modifier.isStatic(pMethod.getModifiers()); // Check it is static;
    }  
  };
  protected final static Predicate<Method> sIsFromValueCandidate = new Predicate<Method>() {
    @Override
    public boolean apply(Method pMethod) {
      // Check it has the corresponding annotation
      return pMethod.isAnnotationPresent(RepositoryEnumValue.class) && // Check it has the corresponding annotation
      Modifier.isStatic(pMethod.getModifiers()); // Check it is static
    }  
  };
  protected final static Function<EnumeratedOption, RepositoryEnumItem> sEnumOptToRepoEnumItem = new Function<EnumeratedOption, RepositoryEnumItem>() {
    @Override
    public RepositoryEnumItem apply(EnumeratedOption pInput) {
      return new RepositoryEnumItem(pInput.getCode(), pInput.getValue());
    }
  };

  protected final Method mCodeGetter;
  protected final Method mValueGetter;
  protected final Method mFromCode;
  protected final Method mFromValue;
  protected EnumPropertyDescriptor mEnumRepositoryType;
  
  protected EnumPropertyMapper(NucleusEntityManager pEntityManager, PropertyDescriptor pBeanPropertyDescriptor, DynamicPropertyDescriptor pRepositoryPropertyDescriptor) {
    super(pEntityManager, pBeanPropertyDescriptor, pRepositoryPropertyDescriptor);
    
    checkArgument(pRepositoryPropertyDescriptor instanceof EnumPropertyDescriptor, "The DynamycPropertyDescriptor isn't an instance of EnumPropertyDescriptor");
    mEnumRepositoryType = (EnumPropertyDescriptor) pRepositoryPropertyDescriptor;
    
    @SuppressWarnings("unchecked")
    Class<? extends Enum<?>> enumBeanType = (Class<? extends Enum<?>>) pBeanPropertyDescriptor.getPropertyType();
    checkArgument(enumBeanType.isEnum(), "The bean property [%s] isn't of type enum", pBeanPropertyDescriptor.getName());
    
    // Extract the methods or fail fast
    mCodeGetter = getCodeGetterMethod(enumBeanType);
    mValueGetter = getValueGetterMethod(enumBeanType);
    mFromCode = getFromCodeMethod(enumBeanType);
    mFromValue = getFromValueMethod(enumBeanType);
    
    // Check that the two enumerations are compatible
    List<EnumeratedOption> repoEnumOptions = Arrays.asList(mEnumRepositoryType.getEnumeratedOptions());
    List<? extends Enum<?>> beanEnumConstants = Arrays.asList(enumBeanType.getEnumConstants());
    List<RepositoryEnumItem> repoEnumItems = RepositoryEnumItem.ORDERING.immutableSortedCopy((Lists.transform(repoEnumOptions, sEnumOptToRepoEnumItem)));
    List<RepositoryEnumItem> beanEnumItems = RepositoryEnumItem.ORDERING.immutableSortedCopy((Lists.transform(beanEnumConstants, 
        new Function<Enum<?>, RepositoryEnumItem>() {
          @Override
          public RepositoryEnumItem apply(Enum<?> pInput) {
            try {
              int code = (Integer) mCodeGetter.invoke(pInput);
              String value = (String) mValueGetter.invoke(pInput);
              return new RepositoryEnumItem(code, value);
            } catch (Exception e) {
              throw new MappingException(String.format("Error transforming enum [%s]", pInput), e);
            }
          }
    })));
    if(!Iterables.elementsEqual(repoEnumItems, beanEnumItems)) {
      throw new MappingException("The repository enumeration and the bean enumeration are not compatible");
    }
  }

  private Method getFromValueMethod(Class<? extends Enum<?>> pEnumBeanType) {
    Collection<Method> methods = Arrays.asList(pEnumBeanType.getMethods());
    Collection<Method> fromValueCandidates = Collections2.filter(methods, sIsFromValueCandidate);
    
    if(fromValueCandidates.isEmpty()) {
      throw new MappingException("Couldn't find any class method annotated with @RepositoryEnumValue");
    } else if(fromValueCandidates.size() > 1){
      throw new MappingException("Found more than one class method annotated with @RepositoryEnumValue");
    }
    
    Method fromValueCandidate = Iterables.getOnlyElement(fromValueCandidates);
    
    // Check that the method has the correct signature
    if(!(fromValueCandidate.getParameterTypes().length == 1 && 
        fromValueCandidate.getParameterTypes()[0].equals(String.class) && 
        fromValueCandidate.getReturnType().equals(pEnumBeanType))) { 
      throw new MappingException(String.format("The method [%s] doesn't have the expected signature", fromValueCandidate));
    }
    
    return fromValueCandidate;
  }

  private Method getFromCodeMethod(Class<? extends Enum<?>> pEnumBeanType) {
    Collection<Method> methods = Arrays.asList(pEnumBeanType.getMethods());
    Collection<Method> fromCodeCandidates = Collections2.filter(methods, sIsFromCodeCandidate);
    
    if(fromCodeCandidates.isEmpty()) {
      throw new MappingException("Couldn't find any class method annotated with @RepositoryEnumCode");
    } else if(fromCodeCandidates.size() > 1){
      throw new MappingException("Found more than one class method annotated with @RepositoryEnumCode");
    }
    
    Method fromCodeCandidate = Iterables.getOnlyElement(fromCodeCandidates);
    
    // Check that the method has the correct signature
    if(!(fromCodeCandidate.getParameterTypes().length == 1 && 
        fromCodeCandidate.getParameterTypes()[0].equals(Integer.class) && 
        fromCodeCandidate.getReturnType().equals(pEnumBeanType))) { 
      throw new MappingException(String.format("The method [%s] doesn't have the expected signature", fromCodeCandidate));
    }
    
    return fromCodeCandidate;
  }

  private Method getValueGetterMethod(Class<? extends Enum<?>> pEnumBeanType) {
    Collection<Method> methods = Arrays.asList(pEnumBeanType.getMethods());
    Collection<Method> valueGetterCandidates = Collections2.filter(methods, sIsValueGetterCandidate);
    
    if(valueGetterCandidates.isEmpty()) {
      throw new MappingException("Couldn't find any instance method annotated with @RepositoryEnumValue");
    } else if(valueGetterCandidates.size() > 1){
      throw new MappingException("Found more than one instance method annotated with @RepositoryEnumValue");
    }
    
    Method valueGetterCandidate = Iterables.getOnlyElement(valueGetterCandidates);
    
    // Check that the method has the correct signature
    if(!(valueGetterCandidate.getParameterTypes().length == 0 && 
        valueGetterCandidate.getReturnType().equals(String.class))) { 
      throw new MappingException(String.format("The method [%s] doesn't have the expected signature", valueGetterCandidate));
    }
    
    return valueGetterCandidate;
  }

  private Method getCodeGetterMethod(Class<? extends Enum<?>> pEnumBeanType) {
    Collection<Method> methods = Arrays.asList(pEnumBeanType.getMethods());
    Collection<Method> codeGetterCandidates = Collections2.filter(methods, sIsCodeGetterCandidate);
    
    if(codeGetterCandidates.isEmpty()) {
      throw new MappingException("Couldn't find any instance method annotated with @RepositoryEnumCode");
    } else if(codeGetterCandidates.size() > 1){
      throw new MappingException("Found more than one instance method annotated with @RepositoryEnumCode");
    }
    
    Method codeGetterCandidate = Iterables.getOnlyElement(codeGetterCandidates);
    
    // Check that the method has the correct signature
    if(!(codeGetterCandidate.getParameterTypes().length == 0 && 
        codeGetterCandidate.getReturnType().equals(Integer.class))) { 
      throw new MappingException(String.format("The method [%s] doesn't have the expected signature", codeGetterCandidate));
    }
    
    return codeGetterCandidate;
  }

  /* (non-Javadoc)
   * @see com.github.talberto.easybeans.atg.PropertyMapper#removeProperty(java.lang.Object)
   */
  @Override
  public void removeProperty(Object pBean) {
  }

  /* (non-Javadoc)
   * @see com.github.talberto.easybeans.atg.PropertyMapper#updateRepositoryItemProperty(atg.repository.MutableRepositoryItem, java.lang.Object)
   */
  @Override
  public void updateRepositoryItemProperty(MutableRepositoryItem pItem, Object pBean) {
    Object enumItem = getBeanProperty(pBean);
    try {
      Object enumCodeOrValue;
      if(mEnumRepositoryType.getUseCodeForValue()) {
        enumCodeOrValue = mCodeGetter.invoke(enumItem);
      } else {
        enumCodeOrValue = mValueGetter.invoke(enumItem);
      }
      pItem.setPropertyValue(getRepositoryPropertyName(), enumCodeOrValue);
    } catch (Exception e) {
      throw new MappingException(String.format("Error extracting and converting [%s] enum", enumItem), e);
    }
  }

  /* (non-Javadoc)
   * @see com.github.talberto.easybeans.atg.PropertyMapper#mapRepositoryProperty(atg.repository.RepositoryItem)
   */
  @Override
  public Object mapRepositoryProperty(RepositoryItem pItem) {
    Object propertyValue = pItem.getPropertyValue(getRepositoryPropertyName());
    try {
      Object enumItem;
      if(mEnumRepositoryType.getUseCodeForValue()) {
        enumItem = mFromCode.invoke(null, propertyValue);
      } else {
        enumItem = mFromValue.invoke(null, propertyValue);
      }
      return mBeanPropertyDescriptor.getPropertyType().cast(enumItem);
    } catch (Exception e) {
      throw new MappingException(String.format("Error extracting and converting [%s] enum", propertyValue));
    }
  }
}

class RegularPropertyMapper extends PropertyMapper {
  protected PropertyGetter mGetter;
  protected PropertySetter mSetter;
  protected PropertyDeleter mDeleter;

  protected RegularPropertyMapper(NucleusEntityManager pEntityManager, PropertyDescriptor pBeanPropertyDescriptor,
      DynamicPropertyDescriptor pRepositoryPropertyDescriptor, PropertyGetter pPropertyGetter, PropertySetter pPropertySetter, PropertyDeleter pPropertyDeleter) {
    super(pEntityManager, pBeanPropertyDescriptor, pRepositoryPropertyDescriptor);
    mGetter = pPropertyGetter;
    mSetter = pPropertySetter;
    mDeleter = pPropertyDeleter;
  }

  /**
   * Maps a RepositoryItem property to a bean property
   * 
   * @param pItem
   * @return
   */
  @Override
  public Object mapRepositoryProperty(RepositoryItem pItem) {
    return mGetter.extractValue(pItem, getRepositoryPropertyName());
  }

  @Override
  public void updateRepositoryItemProperty(MutableRepositoryItem pItem, Object pBean) {
    Object beanPropertyValue = getBeanProperty(pBean);
    mSetter.setItemProperty(pItem, getRepositoryPropertyName(), beanPropertyValue);
  }

  @Override
  public void removeProperty(Object pBean) {
    Object beanPropertyValue = getBeanProperty(pBean);
    mDeleter.delete(beanPropertyValue);
  }
}