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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.beans.DynamicPropertyDescriptor;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItemDescriptor;

import com.github.talberto.easybeans.api.MappingException;
import com.github.talberto.easybeans.gen.MetaBean;
import com.github.talberto.easybeans.gen.MetaProperty;
import com.github.talberto.easybeans.gen.MetaType;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

public class RepositoryBeansExtractor {  
  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
      
  public List<MetaBean> extractBeans(Repository pRepo, List<String> pBeanNames) {
    mLog.info("Extracting beans [{}] from repository [{}]", Joiner.on(",").join(pBeanNames), pRepo.toString());
    List<MetaBean> extractedBeans = Lists.newLinkedList();
    
    for(String beanName : pBeanNames) {
      try {
        RepositoryItemDescriptor descriptor = pRepo.getItemDescriptor(beanName);
        
        if(descriptor == null) {
          throw new IllegalArgumentException(String.format("Doesn't exist an item descriptor named [%s]", beanName));
        }
        
        List<MetaProperty> properties = extractProperties(descriptor);
        extractedBeans.add(new MetaBean(beanName, properties));
      } catch (RepositoryException e) {
        throw new MappingException(e);
      }
    }
    
    return extractedBeans;
  }

  private List<MetaProperty> extractProperties(RepositoryItemDescriptor pItemDescriptor) {
    List<DynamicPropertyDescriptor> propertyDescriptors = ImmutableList.copyOf(pItemDescriptor.getPropertyDescriptors());
    List<MetaProperty> properties = Lists.transform(propertyDescriptors, new Function<DynamicPropertyDescriptor, MetaProperty>() {
      @Override
      public MetaProperty apply(DynamicPropertyDescriptor pPropDesc) {
        return extractProperty(pPropDesc);
      }
    });
    
    return ImmutableList.copyOf(properties);
  }
  
  private MetaProperty extractProperty(DynamicPropertyDescriptor pPropDesc) {
    String name = pPropDesc.getName();
    Class<?> type = pPropDesc.getPropertyType();
    
    MetaType metaType = MetaType.of(TypeToken.of(type));
    MetaProperty property = new MetaProperty(name, metaType);
    
    mLog.debug("Extracted property [{}]", property);
    
    return property;
  }
}
