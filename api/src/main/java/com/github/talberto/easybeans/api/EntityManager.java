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
}
