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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.nucleus.Nucleus;
import atg.nucleus.NucleusTestUtils;
import atg.nucleus.ServiceException;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

import com.github.talberto.easybeans.api.EntityManager;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 * 
 */
public class NucleusEntityManagerIT {

  public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
  public static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
  
  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
  protected Nucleus mNucleus = null;
  protected EntityManager mEntityManager = null;
  protected Repository mUserRepository;
  
  @Before
  public void setUp() throws Exception {
    mLog.info("Start Nucleus.");
    try {
      mNucleus = NucleusTestUtils.startNucleusWithModules(new String[] { "DAF.Deployment", "DPS" }, this.getClass(), "");
      
      assertNotNull(mNucleus);
      mEntityManager = (EntityManager) mNucleus.resolveName("/easybeans/EntityManager");
      assertNotNull(mEntityManager);
      mUserRepository = (Repository) mNucleus.resolveName("/easybeans/UserRepository");
      assertNotNull(mUserRepository);
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
  
  @Test
  public void testFind() throws ParseException {
    User user = mEntityManager.find(User.class, "user01");
    
    assertThat("The fetched item is null", user, notNullValue());
    assertThat("The id is incorrect", user.getId(), equalTo("user01"));
    assertThat("The firstName is incorrect", user.getFirstName(), equalTo("Tomas"));
    assertThat("The lastName is incorrect", user.getLastName(), equalTo("Rodriguez"));
    assertThat("The sex is incorrect", user.getSex(), equalTo("M"));
    assertThat("The birthDate is incorrect", new Date(user.getBirthDate().getTime()), equalTo(dateFormatter.parse("01/07/1980")));
    assertThat("The lastModifiedDate is incorrect", new Date(user.getLastModifiedDate().getTime()), equalTo(dateTimeFormatter.parse("10/01/2014 15:33")));
    assertThat("The points is incorrect", user.getPoints(), equalTo(53));
    assertThat("The rating is incorrect", user.getRating(), equalTo(98.5));
    assertThat("The derivedFirstName is incorrect", user.getDerivedFirstName(), equalTo("Tomas"));
    
    // Check billingAddress
    assertThat("The billingAddress is null", user.getBillingAddress(), notNullValue());
    ContactInfo billingAddress = user.getBillingAddress();
    assertThat("The address1 is incorrect", billingAddress.getAddress1(), equalTo("Avenida Andalucia 25"));
    assertThat("The address2 isn't null", billingAddress.getAddress2(), nullValue());
    assertThat("The address3 isn't null", billingAddress.getAddress3(), nullValue());
    assertThat("The city is incorrect", billingAddress.getCity(), equalTo("Malaga"));
    assertThat("The country is incorrect", billingAddress.getCountry(), equalTo("Spain"));
    assertThat("The postalCode is incorrect", billingAddress.getPostalCode(), equalTo("29006"));
    assertThat("The telephoneNumber is incorrect", billingAddress.getTelephoneNumber(), equalTo("952689656"));
    
    // Check children
    assertThat("The children is null", user.getChildren(), notNullValue());
    List<Child> children = user.getChildren();
    assertThat("The number of children is incorrect", children, hasSize(2));
    Child child = children.get(0);
    assertThat("The name is incorrect", child.getName(), equalTo("Javier"));
    assertThat("The age is incorrect", child.getAge(), equalTo(9));
    child = children.get(1);
    assertThat("The name is incorrect", child.getName(), equalTo("Juan"));
    assertThat("The age is incorrect", child.getAge(), equalTo(12));
    
    // Check favorite numbers
    List<Integer> favoriteNumbers = user.getFavoriteNumbers();
    assertThat("The property favoriteNumbers is null", favoriteNumbers, notNullValue());
    assertThat("The property favoriteNumbers isn't correct", favoriteNumbers, equalTo(Arrays.asList(1,3,7)));
    
    // Check the addresses
    Map<String, ContactInfo> addresses = user.getAddresses();
    assertThat("The property addresses is null", addresses, notNullValue());
    ContactInfo officeAddress = addresses.get("officeAddress");
    assertThat("The office address is null", officeAddress, notNullValue());
    assertThat("The office address id is incorrect", officeAddress.getId(), equalTo("officeContact")); 
    assertThat("The office address1 is incorrect", officeAddress.getAddress1(), equalTo("Avenida Diagonal 12")); 
    assertThat("The office address2 isn't null", officeAddress.getAddress2(), nullValue()); 
    assertThat("The office address3 isn't null", officeAddress.getAddress3(), nullValue()); 
    assertThat("The office city is incorrect", officeAddress.getCity(), equalTo("Barcelona")); 
    assertThat("The office country is incorrect", officeAddress.getCountry(), equalTo("Spain")); 
    assertThat("The office postal code is incorrect", officeAddress.getPostalCode(), equalTo("08019")); 
    assertThat("The office telephone number is incorrect", officeAddress.getTelephoneNumber(), equalTo("93694578")); 
    
    // Check the type
    assertThat("The user type isn't correct", user.getType(), equalTo(Type.PREMIUM));
  }
  
  @Test
  public void testCreate() throws RepositoryException, ParseException {
    ContactInfo billingAddress = new ContactInfo();
    billingAddress.setAddress1("35 Park avenue");
    billingAddress.setCity("New York");
    billingAddress.setCountry("USA");
    
    ContactInfo officeContact = new ContactInfo();
    officeContact.setAddress1("Puerta del Sol");
    officeContact.setCity("Madrid");
    officeContact.setCountry("Spain");
    
    Child child = new Child();
    child.setName("John Doe");
    child.setAge(43);
    
    User user = new User();
    user.setFirstName("Jane");
    user.setLastName("Fonda");
    user.setSex("F");
    user.setBirthDate(dateFormatter.parse("05/03/1945"));
    user.setBillingAddress(billingAddress);
    user.setChildren(Lists.newArrayList(child));
    user.setFavoriteNumbers(Arrays.asList(2,4,6));
    user.setAddresses(Maps.newHashMap(ImmutableMap.of("officeAddress", officeContact)));
    user.setType(Type.STANDARD);
    
    String userId = mEntityManager.create(user);
    RepositoryItem userItem = mUserRepository.getItem(userId, "user");
    assertThat("User item is null", userItem, notNullValue());
    assertThat("The property firstName is incorrect", (String) userItem.getPropertyValue("firstName"), equalTo("Jane"));
    assertThat("The property lastName is incorrect", (String) userItem.getPropertyValue("lastName"), equalTo("Fonda"));
    assertThat("The property sex is incorrect", (String) userItem.getPropertyValue("sex"), equalTo("F"));
    assertThat("The property birthDate is incorrect", new Date(((Date) userItem.getPropertyValue("birthDate")).getTime()), equalTo(dateFormatter.parse("05/03/1945")));
    assertThat("The property lastModifiedDate is incorrect", (Date) userItem.getPropertyValue("lastModifiedDate"), nullValue());
    assertThat("The property points is incorrect", (String) userItem.getPropertyValue("points"), nullValue());
    assertThat("The property rating is incorrect", (String) userItem.getPropertyValue("rating"), nullValue());
    assertThat("The derivedFirstName is incorrect", (String) userItem.getPropertyValue("derivedFirstName"), equalTo("Jane"));
    assertThat("The user type isn't correct", (String) userItem.getPropertyValue("type"), equalTo(Type.STANDARD.value()));
    
    RepositoryItem billingAddressItem = (RepositoryItem) userItem.getPropertyValue("billingAddress");
    assertThat("The property address1 is incorrect", (String) billingAddressItem.getPropertyValue("address1"), equalTo("35 Park avenue"));
    assertThat("The property address2 is incorrect", (String) billingAddressItem.getPropertyValue("address2"), nullValue());
    assertThat("The property address3 is incorrect", (String) billingAddressItem.getPropertyValue("address3"), nullValue());
    assertThat("The property postalCode is incorrect", (String) billingAddressItem.getPropertyValue("postalCode"), nullValue());
    assertThat("The property city is incorrect", (String) billingAddressItem.getPropertyValue("city"), equalTo("New York"));
    assertThat("The property country is incorrect", (String) billingAddressItem.getPropertyValue("country"), equalTo("USA"));
    assertThat("The property telephoneNumber is incorrect", (String) billingAddressItem.getPropertyValue("telephoneNumber"), nullValue());
    
    @SuppressWarnings("unchecked")
    List<RepositoryItem> childrenItems = (List<RepositoryItem>) userItem.getPropertyValue("children");
    assertThat("The property children is null", childrenItems, notNullValue());
    assertThat("The property children has the incorrect number of elements", childrenItems, hasSize(1));
    
    RepositoryItem childItem = childrenItems.get(0);
    assertThat("The property name is incorrect", (String) childItem.getPropertyValue("name"), equalTo("John Doe"));
    assertThat("The property age is incorrect", (Integer) childItem.getPropertyValue("age"), equalTo(43));
    
    @SuppressWarnings("unchecked")
    List<Integer> favoriteNumbersItem = (List<Integer>) userItem.getPropertyValue("favoriteNumbers");
    assertThat("The property favoriteNumbers is null", favoriteNumbersItem, notNullValue());
    assertThat("The property favoriteNumbers isn't correct", favoriteNumbersItem, equalTo(Arrays.asList(2,4,6)));
    
    @SuppressWarnings("unchecked")
    Map<String, RepositoryItem> addresses = (Map<String, RepositoryItem>) userItem.getPropertyValue("addresses");
    assertThat("The property addresses is null", addresses, notNullValue());
    RepositoryItem officeContactItem = addresses.get("officeAddress");
    assertThat("The office contact is null", officeContactItem, notNullValue());
    assertThat("The office contact address1 is incorrect", (String) officeContactItem.getPropertyValue("address1"), equalTo("Puerta del Sol"));
    assertThat("The office contact address2 isn't null", officeContactItem.getPropertyValue("address2"), nullValue());
    assertThat("The office contact address3 isn't null", officeContactItem.getPropertyValue("address3"), nullValue());
    assertThat("The office contact postalCode isn't null", officeContactItem.getPropertyValue("postalCode"), nullValue());
    assertThat("The office contact telephoneNumber isn't null", officeContactItem.getPropertyValue("telephoneNumber"), nullValue());
    assertThat("The office contact city is incorrect", (String) officeContactItem.getPropertyValue("city"), equalTo("Madrid"));
    assertThat("The office contact country is incorrect", (String) officeContactItem.getPropertyValue("country"), equalTo("Spain"));
  }
  
  @Test
  public void testUpdate() throws RepositoryException, ParseException {
    ContactInfo billingAddress = new ContactInfo();
    billingAddress.setId("billingAddress");
    billingAddress.setAddress1("35 Park avenue");
    billingAddress.setCity("New York");
    billingAddress.setCountry("USA");
    
    ContactInfo officeContact = new ContactInfo();
    officeContact.setId("officeContact");
    officeContact.setAddress1("Puerta del Sol");
    officeContact.setCity("Madrid");
    officeContact.setCountry("Spain");
    
    Child child = new Child();
    child.setId("child01");
    child.setName("John Doe");
    child.setAge(43);
    
    User user = new User();
    user.setId("user01");
    user.setFirstName("Jane");
    user.setLastName("Fonda");
    user.setSex("F");
    user.setBirthDate(dateFormatter.parse("05/03/1945"));
    user.setBillingAddress(billingAddress);
    user.setChildren(Lists.newArrayList(child));
    user.setFavoriteNumbers(Arrays.asList(2,4,6));
    user.setAddresses(Maps.newHashMap(ImmutableMap.of("officeAddress", officeContact)));
    user.setType(Type.STANDARD);
    
    mEntityManager.update(user);
    RepositoryItem userItem = mUserRepository.getItem("user01", "user");
    assertThat("User item is null", userItem, notNullValue());
    assertThat("The property firstName is incorrect", (String) userItem.getPropertyValue("firstName"), equalTo("Jane"));
    assertThat("The property lastName is incorrect", (String) userItem.getPropertyValue("lastName"), equalTo("Fonda"));
    assertThat("The property sex is incorrect", (String) userItem.getPropertyValue("sex"), equalTo("F"));
    assertThat("The property birthDate is incorrect", new Date(((Date) userItem.getPropertyValue("birthDate")).getTime()), equalTo(dateFormatter.parse("05/03/1945")));
    assertThat("The property lastModifiedDate is incorrect", (Date) userItem.getPropertyValue("lastModifiedDate"), nullValue());
    assertThat("The property points is incorrect", (String) userItem.getPropertyValue("points"), nullValue());
    assertThat("The property rating is incorrect", (String) userItem.getPropertyValue("rating"), nullValue());
    assertThat("The property derivedFirstName is incorrect", (String) userItem.getPropertyValue("derivedFirstName"), equalTo("Jane"));
    assertThat("The property type is incorrect", (String) userItem.getPropertyValue("type"), equalTo(Type.STANDARD.value()));
    
    RepositoryItem billingAddressItem = (RepositoryItem) userItem.getPropertyValue("billingAddress");
    assertThat("The property address1 is incorrect", (String) billingAddressItem.getPropertyValue("address1"), equalTo("35 Park avenue"));
    assertThat("The property address2 is incorrect", (String) billingAddressItem.getPropertyValue("address2"), nullValue());
    assertThat("The property address3 is incorrect", (String) billingAddressItem.getPropertyValue("address3"), nullValue());
    assertThat("The property postalCode is incorrect", (String) billingAddressItem.getPropertyValue("postalCode"), nullValue());
    assertThat("The property city is incorrect", (String) billingAddressItem.getPropertyValue("city"), equalTo("New York"));
    assertThat("The property country is incorrect", (String) billingAddressItem.getPropertyValue("country"), equalTo("USA"));
    assertThat("The property telephoneNumber is incorrect", (String) billingAddressItem.getPropertyValue("telephoneNumber"), nullValue());
    
    @SuppressWarnings("unchecked")
    List<RepositoryItem> childrenItems = (List<RepositoryItem>) userItem.getPropertyValue("children");
    assertThat("The property children is null", childrenItems, notNullValue());
    assertThat("The property children has the incorrect number of elements", childrenItems, hasSize(1));
    
    RepositoryItem childItem = childrenItems.get(0);
    assertThat("The property name is incorrect", (String) childItem.getPropertyValue("name"), equalTo("John Doe"));
    assertThat("The property age is incorrect", (Integer) childItem.getPropertyValue("age"), equalTo(43));
    
    @SuppressWarnings("unchecked")
    List<Integer> favoriteNumbersItem = (List<Integer>) userItem.getPropertyValue("favoriteNumbers");
    assertThat("The property favoriteNumbers is null", favoriteNumbersItem, notNullValue());
    assertThat("The property favoriteNumbers isn't correct", favoriteNumbersItem, equalTo(Arrays.asList(2,4,6)));
    
    @SuppressWarnings("unchecked")
    Map<String, RepositoryItem> addresses = (Map<String, RepositoryItem>) userItem.getPropertyValue("addresses");
    assertThat("The property addresses is null", addresses, notNullValue());
    RepositoryItem officeContactItem = addresses.get("officeAddress");
    assertThat("The office contact is null", officeContactItem, notNullValue());
    assertThat("The office contact address1 is incorrect", (String) officeContactItem.getPropertyValue("address1"), equalTo("Puerta del Sol"));
    assertThat("The office contact address2 isn't null", officeContactItem.getPropertyValue("address2"), nullValue());
    assertThat("The office contact address3 isn't null", officeContactItem.getPropertyValue("address3"), nullValue());
    assertThat("The office contact postalCode isn't null", officeContactItem.getPropertyValue("postalCode"), nullValue());
    assertThat("The office contact telephoneNumber isn't null", officeContactItem.getPropertyValue("telephoneNumber"), nullValue());
    assertThat("The office contact city is incorrect", (String) officeContactItem.getPropertyValue("city"), equalTo("Madrid"));
    assertThat("The office contact country is incorrect", (String) officeContactItem.getPropertyValue("country"), equalTo("Spain"));
  }
  
  @Test
  public void testDelete() throws RepositoryException {
    Child child1 = new Child();
    child1.setId("child01");
    
    Child child2 = new Child();
    child2.setId("child2");
    
    ContactInfo billingAddress = new ContactInfo();
    billingAddress.setId("billingAddress");
    
    ContactInfo officeContact = new ContactInfo();
    officeContact.setId("officeContact");
    
    User user = new User();
    user.setId("user01");
    user.setBillingAddress(billingAddress);
    user.setChildren(Lists.newArrayList(child1, child2));
    user.setAddresses(Maps.newHashMap(ImmutableMap.of("officeAddress", officeContact)));
    
    mEntityManager.delete(user);
    
    // Check that the repository items no longer exist
    RepositoryItem item = mUserRepository.getItem("user01", "user");
    assertThat("The repository item user still exists", item, nullValue());
    item = mUserRepository.getItem("billingAddress", "contactInfo");
    assertThat("The billing address doesn't exists", item, notNullValue());
    item = mUserRepository.getItem("child01", "child");
    assertThat("The first child doesn't exists", item, notNullValue());
    item = mUserRepository.getItem("child02", "child");
    assertThat("The second child doesn't exists", item, notNullValue());
    item = mUserRepository.getItem("officeContact", "contactInfo");
    assertThat("The office contact doesn't exists", item, notNullValue());
  }
  
  @Test
  public void testDeleteNested() throws RepositoryException {
    Child child1 = new Child();
    child1.setId("child01");
    
    Child child2 = new Child();
    child2.setId("child02");
    
    ContactInfo billingAddress = new ContactInfo();
    billingAddress.setId("billingAddress");
    
    ContactInfo officeContact = new ContactInfo();
    officeContact.setId("officeContact");
    
    User user = new User();
    user.setId("user01");
    user.setBillingAddress(billingAddress);
    user.setChildren(Lists.newArrayList(child1, child2));
    user.setAddresses(Maps.newHashMap(ImmutableMap.of("officeAddress", officeContact)));
    
    mEntityManager.delete(user, true);
    
    // Check that the repository items no longer exist
    RepositoryItem item = mUserRepository.getItem("user01", "user");
    assertThat("The repository item user still exists", item, nullValue());
    item = mUserRepository.getItem("billingAddress", "contactInfo");
    assertThat("The billing address still exists", item, nullValue());
    item = mUserRepository.getItem("child01", "child");
    assertThat("The first child still exists", item, nullValue());
    item = mUserRepository.getItem("child02", "child");
    assertThat("The second child still exists", item, nullValue());
    item = mUserRepository.getItem("officeContact", "contactInfo");
    assertThat("The office contact still exists", item, nullValue());
  }
}
