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

package com.github.talberto.easybeans.gen;

import static com.google.common.collect.Lists.transform;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import atg.beans.DynamicPropertyDescriptor;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItemDescriptor;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

/**
 * An object capable of generating {@link BeanDefinition}'s from a <code>Repository</code>
 * 
 * @author Tomás Rodríguez (rodriguez@progiweb.com)
 *
 */
public class RepositoryBeanGenerator {
  
  protected final Function<DynamicPropertyDescriptor, PropertyDefinition> mExtractPropertyDefinition = new Function<DynamicPropertyDescriptor, PropertyDefinition>() {
    @Override
    public PropertyDefinition apply(DynamicPropertyDescriptor pPropertyDescriptor) {
      return generatePropertyDefinition(pPropertyDescriptor);
    }
  };
  
  /**
   * Generates a <code>BeanDefinition</code> from a <code>Repository</code> and a repository descriptor name
   * 
   * @param pRepository
   * @param pRepositoryDescriptorName
   * @return
   */
  public BeanDefinition generateBean(Repository pRepository, String pRepositoryDescriptorName) {
    RepositoryItemDescriptor descriptor;
    try {
      descriptor = pRepository.getItemDescriptor(pRepositoryDescriptorName);
    } catch (RepositoryException e) {
      throw new IllegalArgumentException(String.format("Couldn't extract BeanDefintion from repository [%s] and descriptor name [%s]", pRepository, pRepositoryDescriptorName), e);
    }
    
    String beanName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, pRepositoryDescriptorName);
    List<DynamicPropertyDescriptor> propertyDescriptors = ImmutableList.copyOf(descriptor.getPropertyDescriptors());
    List<PropertyDefinition> propertyDefinitions = transform(propertyDescriptors, mExtractPropertyDefinition);
    
    return new BeanDefinition(beanName, propertyDefinitions);
  }

  /**
   * Generates a {@link PropertyDefinition} from a given <code>DynamicPropertyDescriptor</code>
   * 
   * @param pPropertyDescriptor
   * @return
   */
  protected PropertyDefinition generatePropertyDefinition(DynamicPropertyDescriptor pPropertyDescriptor) {
    String propertyName = pPropertyDescriptor.getName();
    Class<?> propertyType = pPropertyDescriptor.getPropertyType();
    boolean readable = pPropertyDescriptor.isReadable();
    boolean writable = pPropertyDescriptor.isWritable();
    
    String propertyTypeStr;
    
    if(Collection.class.isAssignableFrom(propertyType)) {
      propertyTypeStr = "List";
    } else if(Map.class.isAssignableFrom(propertyType)) {
      propertyTypeStr = "Map";
    } else {
      propertyTypeStr = propertyType.getSimpleName();
    }
    
    return new PropertyDefinition(propertyName, propertyTypeStr, readable, writable);
  }
}
