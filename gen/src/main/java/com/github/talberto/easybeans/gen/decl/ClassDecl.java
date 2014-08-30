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

package com.github.talberto.easybeans.gen.decl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class ClassDecl {
  protected final String mClassName;
  protected final List<InstanceVarDecl> mInstanceVars;
  protected final List<MethodDecl> mMethods;

  public static ClassDecl create(String pClassName, List<InstanceVarDecl> pInstanceVars, List<MethodDecl> pMethods) {
    return new ClassDecl(pClassName, pInstanceVars, pMethods);
  }

  protected ClassDecl(String pClassName, List<InstanceVarDecl> pInstanceVars, List<MethodDecl> pMethods) {
    checkNotNull(pClassName, "Class name cannot be null");
    checkNotNull(pInstanceVars, "Instance vars cannot be null");
    checkNotNull(pMethods, "Methods cannot be null");
    mClassName = pClassName;
    mInstanceVars = ImmutableList.copyOf(pInstanceVars);
    mMethods = ImmutableList.copyOf(pMethods);
  }

  public String getClassName() {
    return mClassName;
  }
  
  public List<InstanceVarDecl> getInstanceVars() {
    return mInstanceVars;
  }
  
  public List<MethodDecl> getMethods() {
    return mMethods;
  }
}