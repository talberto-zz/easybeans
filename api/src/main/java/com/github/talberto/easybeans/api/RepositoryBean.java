package com.github.talberto.easybeans.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryBean {
  /**
   * Nucleus path of the repository
   * @return
   */
  String repository();
  
  /**
   * Descriptor name within the repository
   * @return
   */
  String descriptorName();
}
