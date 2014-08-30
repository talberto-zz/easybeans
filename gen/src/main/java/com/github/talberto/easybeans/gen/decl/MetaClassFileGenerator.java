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

import java.util.List;
import java.util.Set;

import com.github.talberto.easybeans.gen.MetaBean;
import com.github.talberto.easybeans.gen.MetaProperty;
import com.github.talberto.easybeans.gen.MetaType;
import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * An object that is able to generate MetaClassFile objects from different sources
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class MetaClassFileGenerator {
  protected static final Function<MetaType, ImportDecl> sTypeToImport = new Function<MetaType, ImportDecl>() {
    @Override
    public ImportDecl apply(MetaType pType) {
      return ImportDecl.create(pType.getPackage() + "." + pType.getName());
    }
  };
  
  /**
   * Creates a MetaClassFile from a MetaBean
   * 
   * @param pMetaBean
   * @return
   */
  public ClassFile from(MetaBean pMetaBean) {
    String packageName = pMetaBean.getPackage();
    Set<MetaType> referencedTypes = pMetaBean.getReferencedTypes();
    Set<ImportDecl> imports = Sets.newHashSet(Collections2.transform(referencedTypes, sTypeToImport));
    String className = pMetaBean.getName();
    List<MethodDecl> methods = Lists.newArrayList();
    List<InstanceVarDecl> instanceVars = Lists.newArrayList();
    
    for(MetaProperty property : pMetaBean.getProperties()) {
      String propertyName = property.getName();
      propertyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, propertyName);
      Identifier instanceVarName = Identifier.create("m" + propertyName);
      MetaType type = property.getType();
      // Create getter
      MethodDecl getter = MethodDecl.createGetter(propertyName, type);
      MethodDecl setter = MethodDecl.createSetter(propertyName, type, instanceVarName);
      
      methods.add(getter);
      methods.add(setter);
      instanceVars.add(InstanceVarDecl.create(instanceVarName, type));
    }
    
    return ClassFile.create(
        className,
        packageName,
        imports,
        instanceVars,
        methods
        );
  }
}
