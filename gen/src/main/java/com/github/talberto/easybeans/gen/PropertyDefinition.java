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
 * Describes a property of a bean that is about to be created (doesn't exist yet).
 * 
 * @author Tomás Rodríguez (rodriguez@progiweb.com)
 *
 */
public class PropertyDefinition {

  protected final String mName;
  protected final String mType;
  protected final boolean mReadable;
  protected final boolean mWritable;
  
  public PropertyDefinition(String pName, String pType, boolean pReadable, boolean pWritable) {
    mName = pName;
    mType = pType;
    mReadable = pReadable;
    mWritable = pWritable;
  }

  /**
   * Gets <code>this</code> property's name
   * 
   * @return
   */
  public String getName() {
    return mName;
  }

  /**
   * Gets the readable property of <code>this</code> property
   * 
   * @return true if the property is readable, false otherwise
   */
  public Boolean isReadable() {
    return mReadable;
  }

  /**
   * Gets the writable property of <code>this</code> property
   * 
   * @return true if the property is writable, false otherwise
   */
  public Boolean isWritable() {
    return mWritable;
  }

  /**
   * Gets the type of <code>this</code> property
   * 
   * @return
   */
  public String getType() {
    return mType;
  }
}
