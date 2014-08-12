package com.github.talberto.easybeans.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryProperty {
  /**
   * Name of the property in the repository
   * @return
   */
  String propertyName();
}
