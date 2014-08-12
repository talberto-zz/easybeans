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