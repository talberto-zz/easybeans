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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.CaseFormat;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 * 
 */
public class MetaBean {
  String mName;
  List<MetaProperty> mProperties;
  MetaClass mMetaClass;
  
  public MetaBean(String pName, List<MetaProperty> pProperties) {
    mName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, pName);
    mProperties = pProperties;
    
    // Create MetaClass
    List<MetaInstanceVar> instanceVars = new LinkedList<MetaInstanceVar>();
    List<MetaGetter> getters = new LinkedList<MetaGetter>();
    List<MetaSetter> setters = new LinkedList<MetaSetter>();
    Set<MetaImport> imports = new HashSet<MetaImport>();
    for(MetaProperty property : mProperties) {
      instanceVars.add(property.getInstanceVar());
      getters.add(property.getGetter());
      setters.add(property.getSetter());
      imports.addAll(property.getImports());
    }
    mMetaClass = new MetaClass(mName, instanceVars, getters, setters, imports);
  }

  public String getName() {
    return mName;
  }

  public List<MetaProperty> getProperties() {
    return mProperties;
  }

  public MetaClass getMetaClass() {
    return mMetaClass;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaBean [mName=" + mName + ", mProperties=" + mProperties + ", mMetaClass=" + mMetaClass + "]";
  }
}
