package com.github.talberto.easybeans.gen;

import com.google.common.base.CaseFormat;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public class MetaGetter {
  MetaProperty mProperty;
  MetaInstanceVar mInstanceVariable;
  String mName;
  MetaType mType;
  
  public MetaGetter(MetaProperty pProperty, MetaInstanceVar pInstanceVariable) {
    mProperty = pProperty;
    mInstanceVariable = pInstanceVariable;
    mName = "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getPropertyName());
    mType = pInstanceVariable.getType();
  }

  public MetaInstanceVar getInstanceVariable() {
    return mInstanceVariable;
  }

  public String getInstanceVariableName() {
    return mInstanceVariable.getName();
  }
  
  public String getName() {
    return mName;
  }
  
  public String getTypeName() {
    return mType.getName();
  }
  
  public String getPropertyName() {
    return mProperty.getName();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaGetter [mProperty=" + mProperty + ", mInstanceVariable=" + mInstanceVariable + ", mName=" + mName + ", mType=" + mType + "]";
  }
}