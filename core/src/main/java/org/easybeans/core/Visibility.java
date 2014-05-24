package org.easybeans.core;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public enum Visibility {
  PUBLIC("public"),
  PRIVATE("private"),
  PROTECTED("protected");
  
  String value;
  
  Visibility(String value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return value;
  }
  
  public static Visibility fromValue(String value) {
    for(Visibility vis : Visibility.values()) {
      if(vis.value.equals(value)) {
        return vis;
      }
    }
    throw new IllegalArgumentException("No existing value " + value + " for Visibility enumeration");
  }
}
