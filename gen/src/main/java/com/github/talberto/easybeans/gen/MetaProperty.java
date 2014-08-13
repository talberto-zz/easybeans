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


/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public class MetaProperty {

  String mName;
  MetaType mType;
  MetaInstanceVar mInstanceVar;
  MetaGetter mGetter;
  MetaSetter mSetter;
  Set<MetaImport> mImports = new HashSet<MetaImport>();
  
  public MetaProperty(String pName, MetaType pType) {
    mName = pName;
    mType = pType;
    
    // Build instance var, getter and setter
    mInstanceVar = new MetaInstanceVar(mName, Visibility.PROTECTED, pType);
    mGetter = new MetaGetter(this, mInstanceVar);
    mSetter = new MetaSetter(this, mInstanceVar);
    mImports = pType.getImports();
  }

  public MetaInstanceVar getInstanceVar() {
    return mInstanceVar;
  }

  public MetaGetter getGetter() {
    return mGetter;
  }

  public MetaSetter getSetter() {
    return mSetter;
  }

  public Set<MetaImport> getImports() {
    return mImports;
  }

  public String getName() {
    return mName;
  }

  public MetaType getType() {
    return mInstanceVar.getType();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaProperty [mName=" + mName + ", mType=" + mType + "]";
  }
}
