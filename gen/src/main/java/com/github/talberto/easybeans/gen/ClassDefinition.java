package com.github.talberto.easybeans.gen;

import java.util.HashSet;
import java.util.Set;

public class ClassDefinition {

  Visibility mVisibility = Visibility.PUBLIC;
  boolean mStatic;
  boolean mAbstract;
  String mName;

  public String getName() {
    return mName;
  }

  public void setName(String pName) {
    mName = pName;
  }

  Set<PropertyDefinition> mProperties = new HashSet<PropertyDefinition>();

  public ClassDefinition() {
  }

  public Visibility getVisibility() {
    return mVisibility;
  }

  public void setVisibility(Visibility pVisibility) {
    mVisibility = pVisibility;
  }

  public boolean isStatic() {
    return mStatic;
  }

  public void setStatic(boolean pStatic) {
    mStatic = pStatic;
  }

  public boolean isAbstract() {
    return mAbstract;
  }

  public void setAbstract(boolean pAbstract) {
    mAbstract = pAbstract;
  }

  public Set<PropertyDefinition> getProperties() {
    return mProperties;
  }

  public void setProperties(Set<PropertyDefinition> pProperties) {
    mProperties = pProperties;
  }
}
