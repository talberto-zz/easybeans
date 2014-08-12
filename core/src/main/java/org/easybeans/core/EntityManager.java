package org.easybeans.core;



/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public interface EntityManager {

  /**
   * Finds and retrieves a repository item
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
   * @return
   */
  public <T> String create(T pItem);

  public <T> void update(T pSimpleItem);
}
