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

import com.github.talberto.easybeans.api.RepositoryBean;
import com.github.talberto.easybeans.api.RepositoryId;
import com.github.talberto.easybeans.api.RepositoryProperty;

@RepositoryBean(
    repository = "/easybeans/UserRepository",
    descriptorName = "contactInfo")
public class ContactInfo {

  private String mId;
  private String mAddress1;
  private String mAddress2;
  private String mAddress3;
  private String mPostalCode;
  private String mTelephoneNumber;
  private String mCity;
  private String mCountry;
  
  public ContactInfo() {
  }

  /**
   * @return the id
   */
  @RepositoryId
  public String getId() {
    return mId;
  }

  /**
   * @param pId the id to set
   */
  public void setId(String pId) {
    mId = pId;
  }
  
  /**
   * @return the address1
   */
  @RepositoryProperty(propertyName="address1")
  public String getAddress1() {
    return mAddress1;
  }

  /**
   * @param pAddress1 the address1 to set
   */
  public void setAddress1(String pAddress1) {
    mAddress1 = pAddress1;
  }

  /**
   * @return the address2
   */
  @RepositoryProperty(propertyName="address2")
  public String getAddress2() {
    return mAddress2;
  }

  /**
   * @param pAddress2 the address2 to set
   */
  public void setAddress2(String pAddress2) {
    mAddress2 = pAddress2;
  }

  /**
   * @return the address3
   */
  @RepositoryProperty(propertyName="address3")
  public String getAddress3() {
    return mAddress3;
  }

  /**
   * @param pAddress3 the address3 to set
   */
  public void setAddress3(String pAddress3) {
    mAddress3 = pAddress3;
  }

  /**
   * @return the postalCode
   */
  @RepositoryProperty(propertyName="postalCode")
  public String getPostalCode() {
    return mPostalCode;
  }

  /**
   * @param pPostalCode the postalCode to set
   */
  public void setPostalCode(String pPostalCode) {
    mPostalCode = pPostalCode;
  }

  /**
   * @return the telephoneNumber
   */
  @RepositoryProperty(propertyName="telephoneNumber")
  public String getTelephoneNumber() {
    return mTelephoneNumber;
  }

  /**
   * @param pTelephoneNumber the telephoneNumber to set
   */
  public void setTelephoneNumber(String pTelephoneNumber) {
    mTelephoneNumber = pTelephoneNumber;
  }

  /**
   * @return the city
   */
  @RepositoryProperty(propertyName="city")
  public String getCity() {
    return mCity;
  }

  /**
   * @param pCity the city to set
   */
  public void setCity(String pCity) {
    mCity = pCity;
  }

  /**
   * @return the country
   */
  @RepositoryProperty(propertyName="country")
  public String getCountry() {
    return mCountry;
  }

  /**
   * @param pCountry the country to set
   */
  public void setCountry(String pCountry) {
    mCountry = pCountry;
  }
}
