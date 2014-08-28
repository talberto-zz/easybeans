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

package com.github.talberto.easybeans.atg;

import com.github.talberto.easybeans.api.RepositoryEnumCode;
import com.github.talberto.easybeans.api.RepositoryEnumValue;

public enum Type {
  PREMIUM("premium", 0),
  STANDARD("standard", 1),
  PROFESSIONAL("professional", 2);
  
  String value;
  Integer code;
  
  Type(String value, Integer code) {
    this.value = value;
    this.code = code;
  }
  
  @RepositoryEnumValue
  public String value() {
    return value;
  }
  
  @RepositoryEnumCode
  public Integer code() {
    return code;
  }
  
  @RepositoryEnumValue
  public static Type fromValue(String value) {
    for(Type type : Type.values()) {
      if(type.value.equals(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException(String.format("%s isn't a valid value of the enum Type", value));
  }
  
  @RepositoryEnumCode
  public static Type fromCode(Integer code) {
    for(Type type : Type.values()) {
      if(type.code.equals(code)) {
        return type;
      }
    }
    throw new IllegalArgumentException(String.format("%s isn't a valid code of the enum Type", code));
  }
}
