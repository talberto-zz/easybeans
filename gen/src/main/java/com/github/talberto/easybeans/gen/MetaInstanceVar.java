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

import com.google.common.base.CaseFormat;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 * 
 */
public class MetaInstanceVar {
  String mName;
  MetaType mType;
  Visibility mVisibility;
  String mDefault;

  public MetaInstanceVar(String pName, Visibility pVisibility, MetaType pType) {
    mName = "m" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, pName);
    mVisibility = pVisibility;
    mType = pType;
  }

  public String getName() {
    return mName;
  }

  public void setName(String pName) {
    mName = pName;
  }

  public Visibility getVisibility() {
    return mVisibility;
  }

  public void setVisibility(Visibility pVisibility) {
    mVisibility = pVisibility;
  }

  public String getDefault() {
    return mDefault;
  }

  public void setDefault(String pDefault) {
    mDefault = pDefault;
  }
  
  public String getTypeName() {
    return mType.getName();
  }

  public MetaType getType() {
    return mType;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaInstanceVar [mName=" + mName + ", mType=" + mType + ", mVisibility=" + mVisibility + ", mDefault=" + mDefault + "]";
  }
}
