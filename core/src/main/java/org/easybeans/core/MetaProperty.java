package org.easybeans.core;

import java.util.HashSet;
import java.util.Set;


/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public class MetaProperty {

  String mName;
  MetaType mType;
  MetaInstanceVar mInstanceVar;
  MetaGetter mGetter;
  MetaSetter mSetter;
  Set<MetaImport> mImports = new HashSet<MetaImport>();
  
  public MetaProperty(String pName, MetaType pType) {
    mName = pName;
    mType = pType;
    
    // Build instance var, getter and setter
    mInstanceVar = new MetaInstanceVar(mName, Visibility.PROTECTED, pType);
    mGetter = new MetaGetter(this, mInstanceVar);
    mSetter = new MetaSetter(this, mInstanceVar);
    mImports = pType.getImports();
  }

  public MetaInstanceVar getInstanceVar() {
    return mInstanceVar;
  }

  public MetaGetter getGetter() {
    return mGetter;
  }

  public MetaSetter getSetter() {
    return mSetter;
  }

  public Set<MetaImport> getImports() {
    return mImports;
  }

  public String getName() {
    return mName;
  }

  public MetaType getType() {
    return mInstanceVar.getType();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaProperty [mName=" + mName + ", mType=" + mType + "]";
  }
}
