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

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.talberto.easybeans.api.RepositoryBean;
import com.github.talberto.easybeans.api.RepositoryId;
import com.github.talberto.easybeans.api.RepositoryProperty;

@RepositoryBean(
    repository = "/easybeans/UserRepository",
    descriptorName = "user")
public class User {

  private String mId; 
  private String mFirstName;
  private String mLastName;
  private String mSex;
  private Date mBirthDate;
  private Date mLastModifiedDate;
  private Integer mPoints;
  private Double mRating;
  private ContactInfo mBillingAddress;
  private List<Child> mChildren;
  private List<Integer> mFavoriteNumbers;
  private Map<String, ContactInfo> mAddresses;
  private Type mType;
  private String mDerivedFirstName;
  
  public User() {
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
   * @return the firstName
   */
  @RepositoryProperty(propertyName="firstName")
  public String getFirstName() {
    return mFirstName;
  }

  /**
   * @param pFirstName the firstName to set
   */
  public void setFirstName(String pFirstName) {
    mFirstName = pFirstName;
  }

  /**
   * @return the lastName
   */
  @RepositoryProperty(propertyName="lastName")
  public String getLastName() {
    return mLastName;
  }

  /**
   * @param pLastName the lastName to set
   */
  public void setLastName(String pLastName) {
    mLastName = pLastName;
  }

  /**
   * @return the sex
   */
  @RepositoryProperty(propertyName="sex")
  public String getSex() {
    return mSex;
  }

  /**
   * @param pSex the sex to set
   */
  public void setSex(String pSex) {
    mSex = pSex;
  }

  /**
   * @return the birthDate
   */
  @RepositoryProperty(propertyName="birthDate")
  public Date getBirthDate() {
    return mBirthDate;
  }

  /**
   * @param pBirthDate the birthDate to set
   */
  public void setBirthDate(Date pBirthDate) {
    mBirthDate = pBirthDate;
  }

  /**
   * @return the lastModifiedDate
   */
  @RepositoryProperty(propertyName="lastModifiedDate")
  public Date getLastModifiedDate() {
    return mLastModifiedDate;
  }

  /**
   * @param pLastModifiedDate the lastModifiedDate to set
   */
  public void setLastModifiedDate(Date pLastModifiedDate) {
    mLastModifiedDate = pLastModifiedDate;
  }

  /**
   * @return the points
   */
  @RepositoryProperty(propertyName="points")
  public Integer getPoints() {
    return mPoints;
  }

  /**
   * @param pPoints the points to set
   */
  public void setPoints(Integer pPoints) {
    mPoints = pPoints;
  }

  /**
   * @return the rating
   */
  @RepositoryProperty(propertyName="rating")
  public Double getRating() {
    return mRating;
  }

  /**
   * @param pRating the rating to set
   */
  public void setRating(Double pRating) {
    mRating = pRating;
  }

  /**
   * @return the billingAddress
   */
  @RepositoryProperty(propertyName="billingAddress")
  public ContactInfo getBillingAddress() {
    return mBillingAddress;
  }

  /**
   * @param pBillingAddress the billingAddress to set
   */
  public void setBillingAddress(ContactInfo pBillingAddress) {
    mBillingAddress = pBillingAddress;
  }

  /**
   * @return the children
   */
  @RepositoryProperty(propertyName="children")
  public List<Child> getChildren() {
    return mChildren;
  }

  /**
   * @param pChildren the children to set
   */
  public void setChildren(List<Child> pChildren) {
    mChildren = pChildren;
  }

  /**
   * @return the favoriteNumbers
   */
  @RepositoryProperty(propertyName="favoriteNumbers")
  public List<Integer> getFavoriteNumbers() {
    return mFavoriteNumbers;
  }

  /**
   * @param pFavoriteNumbers the favoriteNumbers to set
   */
  public void setFavoriteNumbers(List<Integer> pFavoriteNumbers) {
    mFavoriteNumbers = pFavoriteNumbers;
  }

  /**
   * @return the addresses
   */
  @RepositoryProperty(propertyName="addresses")
  public Map<String, ContactInfo> getAddresses() {
    return mAddresses;
  }

  /**
   * @param pAddresses the addresses to set
   */
  public void setAddresses(Map<String, ContactInfo> pAddresses) {
    mAddresses = pAddresses;
  }

  /**
   * @return the type
   */
  @RepositoryProperty(propertyName="type")
  public Type getType() {
    return mType;
  }

  /**
   * @param pType the type to set
   */
  public void setType(Type pType) {
    mType = pType;
  }

  /**
   * @return the derivedFirstName
   */
  @RepositoryProperty(propertyName="derivedFirstName")
  public String getDerivedFirstName() {
    return mDerivedFirstName;
  }

  /**
   * @param pDerivedFirstName the derivedFirstName to set
   */
  public void setDerivedFirstName(String pDerivedFirstName) {
    mDerivedFirstName = pDerivedFirstName;
  }
}
