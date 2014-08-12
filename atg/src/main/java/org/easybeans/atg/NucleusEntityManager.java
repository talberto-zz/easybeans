package org.easybeans.atg;

import java.util.Map;

import javax.transaction.TransactionManager;

import org.easybeans.core.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;

import com.google.common.collect.Maps;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class NucleusEntityManager extends GenericService implements EntityManager {

  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
  protected Map<Class<?>, RepositoryBeanMapper<?>> mMapperForType = Maps.newConcurrentMap();
  protected TransactionManager mTransactionManager;
  
  @Override
  public <T> T find(Class<T> pType, Object pPk) {
    mLog.trace("Entering find");
    return findMapperFor(pType).find(pPk);
  }

  private <T> RepositoryBeanMapper<T> findMapperFor(Class<T> pType) {
    mLog.trace("Entering findMapperFor({})", pType);
    // Look first in the map
    mLog.debug("Looking for a RepositoryBeanMapper for the type {}", pType);
    @SuppressWarnings("unchecked")
    RepositoryBeanMapper<T> mapper = (RepositoryBeanMapper<T>) mMapperForType.get(pType);
    if(mapper == null) {
      mLog.debug("RepositoryBeanMapper for type {} not found, creating one", pType);
      mapper = RepositoryBeanMapper.from(this, pType);
      mMapperForType.put(pType, mapper);
    }
    return mapper;
  }
  
  @Override
  public <T> String create(T pItem) {
    mLog.trace("Entering create");
    TransactionDemarcation td = new TransactionDemarcation();
    boolean rollback = true;
    try {
      td.begin(mTransactionManager, TransactionDemarcation.REQUIRED);
      @SuppressWarnings("unchecked")
      Class<T> type = (Class<T>) pItem.getClass();
      String id = findMapperFor(type).create(pItem);
      rollback = false;
      return id;
    } catch (TransactionDemarcationException e) {
      throw new MappingException(e);
    } finally {
      try {
        td.end(rollback);
      } catch (TransactionDemarcationException e) {
        throw new MappingException(e);
      }
    }
  }

  @Override
  public <T> void update(T pItem) {
    mLog.trace("Entering update");
    TransactionDemarcation td = new TransactionDemarcation();
    boolean rollback = true;
    try {
      td.begin(mTransactionManager, TransactionDemarcation.REQUIRED);
      @SuppressWarnings("unchecked")
      Class<T> type = (Class<T>) pItem.getClass();
      findMapperFor(type).update(pItem);
      rollback = false;
    } catch (TransactionDemarcationException e) {
      throw new MappingException(e);
    } finally {
      try {
        td.end(rollback);
      } catch (TransactionDemarcationException e) {
        throw new MappingException(e);
      }
    }
  }

  /**
   * @return the transactionManager
   */
  public TransactionManager getTransactionManager() {
    return mTransactionManager;
  }

  /**
   * @param pTransactionManager the transactionManager to set
   */
  public void setTransactionManager(TransactionManager pTransactionManager) {
    mTransactionManager = pTransactionManager;
  }
}
