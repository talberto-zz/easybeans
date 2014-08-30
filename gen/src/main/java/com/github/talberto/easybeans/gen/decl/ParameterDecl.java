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

import com.github.talberto.easybeans.gen.MetaType;

/**
 * Holds information about a method parameter
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class ParameterDecl {
  protected final Identifier mName;
  protected final MetaType mType;
  
  public static ParameterDecl create(String pParamIdent, MetaType pType) {
    return new ParameterDecl(Identifier.create(pParamIdent), pType);
  }
  
  public static ParameterDecl create(Identifier pParamIdent, MetaType pType) {
    return new ParameterDecl(pParamIdent, pType);
  }
  
  protected ParameterDecl(Identifier pParamName, MetaType pType) {
    mName = pParamName;
    mType = pType;
  }

  /**
   * @return the name
   */
  public Identifier getName() {
    return mName;
  }

  /**
   * @return the type
   */
  public MetaType getType() {
    return mType;
  }
}
