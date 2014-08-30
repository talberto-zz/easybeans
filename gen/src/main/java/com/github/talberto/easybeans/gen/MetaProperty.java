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



/**
 * Holds information about a bean's property
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class MetaProperty {
  protected final String mName;
  protected final MetaType mType;
  
  public static MetaProperty create(String pName, Class<?> pType) {
    return new MetaProperty(pName, pType);
  }

  public MetaProperty(String pName, MetaType pType) {
    mName = pName;
    mType = pType;
  }
  
  public MetaProperty(String pName, Class<?> pType) {
    this(pName, MetaType.create(pType));
  }

  /**
   * @return the name
   */
  public String getName() {
    return mName;
  }

  /**
   * @return the type
   */
  public MetaType getType() {
    return mType;
  }
}
