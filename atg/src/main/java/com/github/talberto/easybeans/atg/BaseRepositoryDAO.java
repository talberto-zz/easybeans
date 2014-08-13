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

import atg.repository.MutableRepository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

import com.github.talberto.easybeans.api.EntityManager;
import com.github.talberto.easybeans.api.RepositoryBean;
import com.google.common.base.Preconditions;


/**
 * Base repository based DAO.
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 * @param <T>
 */
public abstract class BaseRepositoryDAO<T> extends GenericRepositoryDAO<T> {
  
  protected Class<T> mType;
  protected MutableRepository mRepository;
  protected String mDescriptorName;
  protected EntityManager mEntityManager;
  
  public BaseRepositoryDAO() {
    configure();
  }

  private void configure() {
    Class<?> type = capture();
    RepositoryBean repositoryBeanAnnotation = type.getAnnotation(RepositoryBean.class);
    Preconditions.checkArgument(repositoryBeanAnnotation != null, "the type %s isn't annotated with @RepositoryBean", type.getName());
    String repositoryPath = repositoryBeanAnnotation.repository();
    String descriptorName = repositoryBeanAnnotation.descriptorName();
    MutableRepository repository = (MutableRepository) resolveName(repositoryPath);
    Preconditions.checkNotNull(repository, "the repository %s is null", repositoryPath);
    mRepository = (MutableRepository) resolveName(repositoryPath);
    mDescriptorName = descriptorName;
  }

  /* (non-Javadoc)
   * @see org.easybeans.core.GenericDAO#find(java.lang.Object)
   */
  @Override
  public T find(Object pPk) {
    try {
      RepositoryItem item = mRepository.getItem(pPk.toString(), mDescriptorName);
      
      return null;
    } catch (RepositoryException e) {
      throw new IllegalArgumentException();
    }
  }

  /* (non-Javadoc)
   * @see org.easybeans.core.GenericDAO#save(java.lang.Object)
   */
  @Override
  public void save(T pBean) {
    // TODO Auto-generated method stub
    
  }
}
