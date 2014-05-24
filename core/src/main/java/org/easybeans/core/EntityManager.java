package org.easybeans.core;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public interface EntityManager {

  public <T> T find(Class<T> pType, Object pPk);
}
