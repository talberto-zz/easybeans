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

import static com.github.talberto.easybeans.gen.TestUtils.findDescriptorByName;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import javax.tools.SimpleJavaFileObject;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

public class BeanRendererTest {

  BeanDefinition bean;
  BeanRenderer renderer;
  
  @Before public void setup() {
    List<PropertyDefinition> properties = ImmutableList.<PropertyDefinition>builder()
        .add(new PropertyDefinition("firstName", "String", true, true))
        .add(new PropertyDefinition("age", "int", true, true))
        .add(new PropertyDefinition("points", "double", true, true))
        .add(new PropertyDefinition("premiumUser", "boolean", true, true))
        .build();
    bean = new BeanDefinition("UserBean", properties);
    renderer = new BeanRenderer();
  }
  
  @Test public void testRender() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, NoSuchFieldException, IntrospectionException {
    String generatedCode = renderer.render(bean);
    assertThat("The generated source code is null", generatedCode, notNullValue());
    
    File tmpDir = Files.createTempDir();
    tmpDir.deleteOnExit();
    SimpleJavaFileObject javaSource = new DynamicJavaSourceCodeObject(bean.getBeanName(), generatedCode);
    boolean result = CompilerUtils.compile(javaSource, tmpDir);
    
    assertThat("The generated source code didn't compile correctly", result, equalTo(true));
    
    URLClassLoader loader = new URLClassLoader(new URL[] {tmpDir.toURI().toURL()});
    Class<?> beanGeneratedClass = loader.loadClass(bean.getBeanName());
    loader.close();
    
    // Check the fields
    checkFields(bean, beanGeneratedClass);
    
    // Check the methods
    checkProperties(bean, beanGeneratedClass);
  }

  private void checkProperties(BeanDefinition pBeanDefinition, Class<?> pBeanGeneratedClass) throws NoSuchMethodException, IntrospectionException {
    assertThat("The number of fields is incorrect", pBeanGeneratedClass.getDeclaredFields(), arrayWithSize(pBeanDefinition.getProperties().size()));
    BeanInfo beanInfo = Introspector.getBeanInfo(pBeanGeneratedClass);
    List<PropertyDescriptor> propertyDescriptors = ImmutableList.copyOf(beanInfo.getPropertyDescriptors());
    
    for(PropertyDefinition propertyDefinition : pBeanDefinition.getProperties()) {
      PropertyDescriptor propertyDescriptor = (PropertyDescriptor) findDescriptorByName(propertyDescriptors, propertyDefinition.getName());
      assertThat("The property type don't match", propertyDescriptor.getPropertyType().getSimpleName(), equalTo(propertyDefinition.getType()));
      assertThat("The readable attribute doesn't match", propertyDescriptor.getReadMethod() != null, equalTo(propertyDefinition.isReadable()));
      assertThat("The writable attribute doesn't match", propertyDescriptor.getWriteMethod() != null, equalTo(propertyDefinition.isWritable()));
    }
  }

  private void checkFields(BeanDefinition pBeanDefinition, Class<?> pBeanGeneratedClass) throws NoSuchFieldException, SecurityException {
    assertThat("The number of fields is incorrect", pBeanGeneratedClass.getDeclaredFields(), arrayWithSize(pBeanDefinition.getProperties().size()));
    
    for(PropertyDefinition property : pBeanDefinition.getProperties()) {
      Field field = pBeanGeneratedClass.getDeclaredField(property.getName());
      assertThat("Incorrect field name", field.getName(), equalTo(property.getName()));
      assertThat("Incorrect field type", field.getType().getSimpleName(), equalTo(property.getType()));
    }
  }
}
