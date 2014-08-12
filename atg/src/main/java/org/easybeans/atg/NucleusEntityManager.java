package org.easybeans.atg;

import java.util.Map;

import org.easybeans.core.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class NucleusEntityManager implements EntityManager {

  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
  protected Map<Class<?>, RepositoryBeanMapper<?>> mMapperForType = Maps.newConcurrentMap();
  
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
    @SuppressWarnings("unchecked")
    Class<T> type = (Class<T>) pItem.getClass();
    return findMapperFor(type).create(pItem);
  }
}
