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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Holds information about an instance variable
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class InstanceVarDecl {
  protected final Identifier mName;
  protected final MetaType mType;

  public static InstanceVarDecl create(Identifier pInstanceIdent, MetaType pType) {
    return new InstanceVarDecl(pInstanceIdent, pType);
  }
  
  public static InstanceVarDecl create(String pName, MetaType pType) {
    return new InstanceVarDecl(Identifier.create(pName), pType);
  }
  
  protected InstanceVarDecl(Identifier pName, MetaType pType) {
    checkNotNull(pName, "The identifier cannot be null");
    checkNotNull(pType, "The type cannot be null");
    mName = pName;
    mType = pType;
  }

  /**
   * @return the name
   */
  public Identifier getIdentifier() {
    return mName;
  }

  /**
   * @return the type
   */
  public MetaType getType() {
    return mType;
  }
}
