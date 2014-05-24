package org.easybeans.core;

import com.google.common.base.CaseFormat;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 * 
 */
public class MetaInstanceVar {
  String mName;
  MetaType mType;
  Visibility mVisibility;
  String mDefault;

  public MetaInstanceVar(String pName, Visibility pVisibility, MetaType pType) {
    mName = "m" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, pName);
    mVisibility = pVisibility;
    mType = pType;
  }

  public String getName() {
    return mName;
  }

  public void setName(String pName) {
    mName = pName;
  }

  public Visibility getVisibility() {
    return mVisibility;
  }

  public void setVisibility(Visibility pVisibility) {
    mVisibility = pVisibility;
  }

  public String getDefault() {
    return mDefault;
  }

  public void setDefault(String pDefault) {
    mDefault = pDefault;
  }
  
  public String getTypeName() {
    return mType.getName();
  }

  public MetaType getType() {
    return mType;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaInstanceVar [mName=" + mName + ", mType=" + mType + ", mVisibility=" + mVisibility + ", mDefault=" + mDefault + "]";
  }
}
