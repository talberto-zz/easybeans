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

import java.util.HashSet;
import java.util.Set;

public class ClassDefinition {

  Visibility mVisibility = Visibility.PUBLIC;
  boolean mStatic;
  boolean mAbstract;
  String mName;

  public String getName() {
    return mName;
  }

  public void setName(String pName) {
    mName = pName;
  }

  Set<PropertyDefinition> mProperties = new HashSet<PropertyDefinition>();

  public ClassDefinition() {
  }

  public Visibility getVisibility() {
    return mVisibility;
  }

  public void setVisibility(Visibility pVisibility) {
    mVisibility = pVisibility;
  }

  public boolean isStatic() {
    return mStatic;
  }

  public void setStatic(boolean pStatic) {
    mStatic = pStatic;
  }

  public boolean isAbstract() {
    return mAbstract;
  }

  public void setAbstract(boolean pAbstract) {
    mAbstract = pAbstract;
  }

  public Set<PropertyDefinition> getProperties() {
    return mProperties;
  }

  public void setProperties(Set<PropertyDefinition> pProperties) {
    mProperties = pProperties;
  }
}
