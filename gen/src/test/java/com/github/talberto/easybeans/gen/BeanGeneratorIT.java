/**
 * Copyright 2014 Tomas Rodriguez (rodriguez@progiweb.com)
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.beans.DynamicPropertyDescriptor;
import atg.nucleus.Nucleus;
import atg.nucleus.NucleusTestUtils;
import atg.nucleus.ServiceException;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItemDescriptor;

/**
 * 
 * @author Tomás Rodríguez (rodriguez@progiweb.com)
 *
 */
public class BeanGeneratorIT {

  Logger mLog = LoggerFactory.getLogger(this.getClass());
  Nucleus mNucleus = null;
  Repository mUserRepository;
  BeanGenerator mBeanGenerator;
  
  @Before
  public void setUp() throws Exception {
    mLog.info("Start Nucleus.");
    try {
      mNucleus = NucleusTestUtils.startNucleusWithModules(new String[] { "DAF.Deployment" }, this.getClass(), "");
      
      assertNotNull(mNucleus);
      mUserRepository = (Repository) mNucleus.resolveName("/easybeans/UserRepository");
      assertNotNull(mUserRepository);
      mBeanGenerator = (BeanGenerator) mNucleus.resolveName("/easybeans/BeanGenerator");
      assertNotNull(mBeanGenerator);
    } catch (ServletException e) {
      fail(e.getMessage());
    }
  }

  @After
  public void tearDown() {
    mLog.info("Stop Nucleus");
    if (mNucleus != null) {
      try {
        NucleusTestUtils.shutdownNucleus(mNucleus);
      } catch (ServiceException e) {
        fail(e.getMessage());
      } catch (IOException e) {
        fail(e.getMessage());
      }
    }
  }
  
  @Test public void generateUserBean() {
    BeanDefinition beanDefinition = mBeanGenerator.generateBean(mUserRepository, "user");
    
    assertThat("Returned bean definition is null", beanDefinition, notNullValue());
    assertThat("Incorrect bean name", beanDefinition.getBeanName(), equalTo("User"));
    
    List<PropertyDefinition> properties = beanDefinition.getProperties();
    
    assertThat("List of properties is null", properties, notNullValue());
    assertThat("List of properties is empty", properties, hasSize(1));
    
    PropertyDefinition property = properties.get(0);
    
    assertThat("Incorrect property name", property.getName(), equalTo("firstName"));
    assertThat("Property firstName should be readable", property.isReadable(), equalTo(true));
    assertThat("Property firstName should be writable", property.isWritable(), equalTo(true));
    assertThat("Property firstName incorrect type", property.getType(), equalTo("String"));
  }
  
  @Test public void testGeneratePropertyDefinition() throws RepositoryException {
    RepositoryItemDescriptor itemDescriptor = mUserRepository.getItemDescriptor("user");
    assertThat("The item descriptor is null", itemDescriptor, notNullValue());
    DynamicPropertyDescriptor propertyDescriptor = itemDescriptor.getPropertyDescriptor("firstName");
    assertThat("The property descriptor is null", propertyDescriptor, notNullValue());
    
    PropertyDefinition property = mBeanGenerator.generatePropertyDefinition(propertyDescriptor);
    
    assertThat("Incorrect property name", property.getName(), equalTo("firstName"));
    assertThat("Property firstName should be readable", property.isReadable(), equalTo(true));
    assertThat("Property firstName should be writable", property.isWritable(), equalTo(true));
    assertThat("Property firstName incorrect type", property.getType(), equalTo("String"));
  }
}
