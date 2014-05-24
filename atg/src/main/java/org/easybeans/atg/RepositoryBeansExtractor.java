package org.easybeans.atg;

import java.util.List;

import org.easybeans.core.MetaBean;
import org.easybeans.core.MetaProperty;
import org.easybeans.core.MetaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.beans.DynamicPropertyDescriptor;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItemDescriptor;

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
        throw new ExtractionException();
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
