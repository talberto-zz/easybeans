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

import com.github.talberto.easybeans.gen.MetaType;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;

/**
 * Holds information about a method
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class MethodDecl {
  protected final Identifier mName;
  protected final MetaType mReturnType;
  protected final List<ParameterDecl> mParameters;
  protected final SentenceBlock mBody;

  public static MethodDecl create(String pMethodName, List<ParameterDecl> pParams) {
    return new MethodDecl(Identifier.create(pMethodName), pParams, MetaType.VOID_TYPE);
  }
  
  public static MethodDecl create(String pMethodName, List<ParameterDecl> pParams, MetaType pReturnType) {
    return new MethodDecl(Identifier.create(pMethodName), pParams, pReturnType);
  }
  
  public static MethodDecl create(String pMethodName, List<ParameterDecl> pParams, MetaType pReturnType, SentenceBlock pBody) {
    return new MethodDecl(Identifier.create(pMethodName), pParams, pReturnType, pBody);
  }
  
  public static MethodDecl createGetter(String pPropertyName, MetaType pType) {
    return create(
        "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, pPropertyName), 
        Lists.<ParameterDecl>newArrayList(), 
        pType);
  }
  
  public static MethodDecl createSetter(String pPropertyName, MetaType pType, Identifier pInstanceVarIdent) {
    String propertyName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, pPropertyName);
    Identifier methodIdent = Identifier.create("set" + propertyName);
    Identifier paramIdent = Identifier.create("p" + propertyName);
    SentenceBlock body = Sentence.newSentenceBlock(
        Sentence.newAssignment(pInstanceVarIdent, paramIdent)
        );
    
    return new MethodDecl(
        methodIdent, 
        Lists.newArrayList(ParameterDecl.create(paramIdent, pType)),
        pType,
        body
        );
  }

  protected MethodDecl(Identifier pMethodName, List<ParameterDecl> pParams, MetaType pReturnType) {
    this(pMethodName, pParams, pReturnType, SentenceBlock.EMPTY_BLOCK);
  }
  
  protected MethodDecl(Identifier pMethodName, List<ParameterDecl> pParams, MetaType pReturnType, SentenceBlock pBlock) {
    mName = pMethodName;
    mParameters = pParams;
    mReturnType = pReturnType;
    mBody = pBlock;
  }

  /**
   * @return the name
   */
  public Identifier getName() {
    return mName;
  }

  /**
   * @return the returnType
   */
  public MetaType getReturnType() {
    return mReturnType;
  }

  /**
   * @return the parameters
   */
  public List<ParameterDecl> getParameters() {
    return mParameters;
  }

  /**
   * @return the block
   */
  public SentenceBlock getBody() {
    return mBody;
  }
}
