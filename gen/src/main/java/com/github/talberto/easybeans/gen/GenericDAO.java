package com.github.talberto.easybeans.gen;

/**
 * Generic DAO
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 * @param <T>
 */
public interface GenericDAO<T> {

  public T find(Object pPk);
  
  public void save(T pBean);
  
  public Class<T> getType();
}
