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
import java.util.Set;

/**
 * Holds information about a class file
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class ClassFile {
  protected final String mPackageName;
  protected final Set<ImportDecl> mImports;
  protected ClassDecl mClassDecl;

  public static ClassFile create(String pClassName, String pPackageName, Set<ImportDecl> pImports, List<InstanceVarDecl> pInstanceVars, List<MethodDecl> pMethods) {
    ClassDecl classDecl = ClassDecl.create(pClassName, pInstanceVars, pMethods);
    return new ClassFile(pPackageName, pImports, classDecl);
  }

  protected ClassFile(String pPackageName, Set<ImportDecl> pImports, ClassDecl pClassDecl) {
    checkNotNull(pPackageName, "Package name cannot be null");
    checkNotNull(pImports, "Imports cannot be null");
    checkNotNull(pClassDecl, "ClassDecl cannot be null");
    mPackageName = pPackageName;
    mImports = pImports;
    mClassDecl = pClassDecl;
  }

  /**
   * @return the className
   */
  public String getClassName() {
    return mClassDecl.getClassName();
  }

  /**
   * @return the packageName
   */
  public String getPackage() {
    return mPackageName;
  }

  /**
   * @return the imports
   */
  public Set<ImportDecl> getImports() {
    return mImports;
  }

  /**
   * @return the instanceVars
   */
  public List<InstanceVarDecl> getInstanceVars() {
    return mClassDecl.getInstanceVars();
  }

  /**
   * @return the methods
   */
  public List<MethodDecl> getMethods() {
    return mClassDecl.getMethods();
  }

  /**
   * @return the data
   */
  public ClassDecl getClassDecl() {
    return mClassDecl;
  }
}
