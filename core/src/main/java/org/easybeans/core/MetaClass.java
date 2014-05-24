package org.easybeans.core;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public class MetaClass {
  Set<MetaImport> mImports = new HashSet<MetaImport>();
  String mName;
  List<MetaInstanceVar> mInstanceVariables = new LinkedList<MetaInstanceVar>();
  List<MetaGetter> mGetters = new LinkedList<MetaGetter>();
  List<MetaSetter> mSetters = new LinkedList<MetaSetter>();
  
  public MetaClass(String pName, List<MetaInstanceVar> pInstanceVariables, List<MetaGetter> pGetters, List<MetaSetter> pSetters, Set<MetaImport> pImports) {
    mName = pName;
    mInstanceVariables = pInstanceVariables;
    mGetters = pGetters;
    mSetters = pSetters;
    mImports = pImports;
  }

  public Set<MetaImport> getImports() {
    return mImports;
  }

  public String getName() {
    return mName;
  }

  public List<MetaInstanceVar> getInstanceVariables() {
    return mInstanceVariables;
  }

  public List<MetaGetter> getGetters() {
    return mGetters;
  }

  public List<MetaSetter> getSetters() {
    return mSetters;
  }
}
