package com.github.talberto.easybeans.gen;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.CaseFormat;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 * 
 */
public class MetaBean {
  String mName;
  List<MetaProperty> mProperties;
  MetaClass mMetaClass;
  
  public MetaBean(String pName, List<MetaProperty> pProperties) {
    mName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, pName);
    mProperties = pProperties;
    
    // Create MetaClass
    List<MetaInstanceVar> instanceVars = new LinkedList<MetaInstanceVar>();
    List<MetaGetter> getters = new LinkedList<MetaGetter>();
    List<MetaSetter> setters = new LinkedList<MetaSetter>();
    Set<MetaImport> imports = new HashSet<MetaImport>();
    for(MetaProperty property : mProperties) {
      instanceVars.add(property.getInstanceVar());
      getters.add(property.getGetter());
      setters.add(property.getSetter());
      imports.addAll(property.getImports());
    }
    mMetaClass = new MetaClass(mName, instanceVars, getters, setters, imports);
  }

  public String getName() {
    return mName;
  }

  public List<MetaProperty> getProperties() {
    return mProperties;
  }

  public MetaClass getMetaClass() {
    return mMetaClass;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaBean [mName=" + mName + ", mProperties=" + mProperties + ", mMetaClass=" + mMetaClass + "]";
  }
}
