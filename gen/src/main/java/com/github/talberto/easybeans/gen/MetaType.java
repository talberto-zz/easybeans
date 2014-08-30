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
 * Holds information about a type
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class MetaType {
  public static final MetaType VOID_TYPE = new MetaType("void");
  
  protected final String mPackage;
  protected final String mName;
  
  public static MetaType create(Class<?> pType) {
    return new MetaType(pType);
  }

  protected MetaType(Class<?> pType) {
    mPackage = pType.getPackage().getName();
    mName = pType.getSimpleName();
  }

  protected MetaType(String pName) {
    this(pName, null);
  }

  protected MetaType(String pName, String pPackage) {
    mName = pName;
    mPackage = pPackage;
  }
  
  /**
   * @return the package
   */
  public String getPackage() {
    return mPackage;
  }

  /**
   * @return the name
   */
  public String getName() {
    return mName;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mName == null) ? 0 : mName.hashCode());
    result = prime * result + ((mPackage == null) ? 0 : mPackage.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof MetaType))
      return false;
    MetaType other = (MetaType) obj;
    if (mName == null) {
      if (other.mName != null)
        return false;
    } else if (!mName.equals(other.mName))
      return false;
    if (mPackage == null) {
      if (other.mPackage != null)
        return false;
    } else if (!mPackage.equals(other.mPackage))
      return false;
    return true;
  }
}
