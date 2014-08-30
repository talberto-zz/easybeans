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

import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

/**
 * Holds information about a bean
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class MetaBean {
  protected final static Function<MetaProperty, MetaType> sTypeFromProperty = new Function<MetaProperty, MetaType>() {
    @Override
    public MetaType apply(MetaProperty pProperty) {
      return pProperty.getType();
    }
  };
  
  private final String mName;
  private final String mPackage;
  private final List<MetaProperty> mProperties;
  
  public static MetaBean create(String pName, String pPackage, MetaProperty... pProperties) {
    return new MetaBean(pName, pPackage, ImmutableList.copyOf(pProperties));
  }
  
  public static MetaBean create(String pName, String pPackage, List<MetaProperty> pProperties) {
    return new MetaBean(pName, pPackage, pProperties);
  }

  protected MetaBean(String pName, String pPackage, List<MetaProperty> pProperties) {
    mName = pName;
    mPackage = pPackage;
    mProperties = ImmutableList.copyOf(pProperties);
  }

  /**
   * @return the name
   */
  public String getName() {
    return mName;
  }

  /**
   * @return the package
   */
  public String getPackage() {
    return mPackage;
  }

  /**
   * @return the properties
   */
  public List<MetaProperty> getProperties() {
    return mProperties;
  }

  public Set<MetaType> getReferencedTypes() {
    return Sets.newHashSet(Collections2.transform(mProperties, sTypeFromProperty));
  }
}
