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

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public class MetaClass {
  Set<MetaImport> mImports = new HashSet<MetaImport>();
  String mName;
  List<MetaInstanceVar> mInstanceVariables = new LinkedList<MetaInstanceVar>();
  List<MetaGetter> mGetters = new LinkedList<MetaGetter>();
  List<MetaSetter> mSetters = new LinkedList<MetaSetter>();
  
  public MetaClass(String pName, List<MetaInstanceVar> pInstanceVariables, List<MetaGetter> pGetters, List<MetaSetter> pSetters, Set<MetaImport> pImports) {
    mName = pName;
    mInstanceVariables = pInstanceVariables;
    mGetters = pGetters;
    mSetters = pSetters;
    mImports = pImports;
  }

  public Set<MetaImport> getImports() {
    return mImports;
  }

  public String getName() {
    return mName;
  }

  public List<MetaInstanceVar> getInstanceVariables() {
    return mInstanceVariables;
  }

  public List<MetaGetter> getGetters() {
    return mGetters;
  }

  public List<MetaSetter> getSetters() {
    return mSetters;
  }
}
