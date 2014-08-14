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

package com.github.talberto.easybeans.api;


/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public interface EntityManager {

  /**
   * Finds and retrieves a bean of type pType with primary key pPk
   * 
   * @param pType class of the repository item to find
   * @param pPk primary key
   * @return the bean corresponding to the repository item if found, null otherwise
   */
  public <T> T find(Class<T> pType, Object pPk);

  /**
   * Creates a new repository item
   * 
   * @param pItem the bean representing the repository item to be created
   * @return the id of the newly created item
   */
  public <T> String create(T pBean);

  /**
   * Updates the corresponding RepositoryItem's from a given bean. The bean's @RepositoryId property must be non null.
   *  
   * @param pItem
   */
  public <T> void update(T pBean);

  /**
   * Deletes the RepositoryItem's that corresponds to a given bean. The bean's @RepositoryId property must be non null.
   * 
   * @param pItem
   */
  public <T> void delete(T pBean);
  
  /**
   * Deletes the RepositoryItem's that corresponds to a given bean. The bean's @RepositoryId property must be non null.
   * 
   * @param pItem
   */
  public <T> void delete(T pBean, boolean pDeleteNested);
}
