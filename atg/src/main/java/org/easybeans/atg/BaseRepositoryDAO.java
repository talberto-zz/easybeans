package org.easybeans.atg;

import org.easybeans.core.EntityManager;

import atg.repository.MutableRepository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

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
