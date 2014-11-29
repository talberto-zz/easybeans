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

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.SimpleJavaFileObject;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

public class BeanRendererTest {

  BeanDefinition bean;
  BeanRenderer renderer;
  
  @Before public void setup() {
    PropertyDefinition property = new PropertyDefinition("firstName", "String", true, true);
    bean = new BeanDefinition("User", ImmutableList.of(property));
    renderer = new BeanRenderer();
  }
  
  @Test public void testRender() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException {
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
    Field[] fields = beanGeneratedClass.getDeclaredFields();
    assertThat("The number of fields is incorrect", fields, arrayWithSize(1));
    Field firstNameField = fields[0];
    assertThat("Incorrect field name", firstNameField.getName(), equalTo("firstName"));
    
    // Check the methods
    Method[] allMethods = beanGeneratedClass.getDeclaredMethods();
    assertThat("The number of methods is incorrect", allMethods, arrayWithSize(2));
    Method getterMethod = beanGeneratedClass.getDeclaredMethod("getFirstName");
    assertThat("Incorrect getter name", getterMethod.getName(), equalTo("getFirstName"));
    Method setterMethod = beanGeneratedClass.getDeclaredMethod("setFirstName", String.class);
    assertThat("Incorrect setter name", setterMethod.getName(), equalTo("setFirstName"));
  }
}
