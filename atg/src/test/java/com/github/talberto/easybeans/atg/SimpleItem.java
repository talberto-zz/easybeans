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
    repository = "/easybeans/SimpleRepository",
    descriptorName = "simpleItem")
public class SimpleItem {

  private String mStringProperty;
  private String mId; 
  
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
   * @return the stringProperty
   */
  @RepositoryProperty(propertyName = "stringProperty")
  public String getStringProperty() {
    return mStringProperty;
  }

  /**
   * @param pStringProperty the stringProperty to set
   */
  public void setStringProperty(String pStringProperty) {
    mStringProperty = pStringProperty;
  }
}
