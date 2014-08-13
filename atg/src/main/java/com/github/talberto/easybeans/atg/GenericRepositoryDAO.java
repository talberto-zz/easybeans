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

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import atg.nucleus.GenericService;

import com.github.talberto.easybeans.gen.GenericDAO;

/**
 * Generic dao implementation of {@link GenericDAO} for Nucleus. 
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 * 
 * @param <T>
 */
public abstract class GenericRepositoryDAO<T> extends GenericService implements GenericDAO<T> {
  
  @SuppressWarnings("unchecked")
  protected Class<T> capture() {
    Type superclass = getClass().getGenericSuperclass();
    checkArgument(superclass instanceof ParameterizedType,
        "%s isn't parameterized", superclass);
    Type paramType = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    checkArgument(paramType instanceof Class,
        "%s isn't a class, paramType");
    return (Class<T>) paramType;
  }
  
  public GenericRepositoryDAO() {
  }
}
