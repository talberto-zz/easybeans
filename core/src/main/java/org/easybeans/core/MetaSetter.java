package org.easybeans.core;

import com.google.common.base.CaseFormat;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 * 
 */
public class MetaSetter {
  MetaProperty mProperty;
  MetaInstanceVar mInstanceVariable;
  String mName;
  MetaType mType;
  String mArgument;

  public MetaSetter(MetaProperty pProperty, MetaInstanceVar pInstanceVariable) {
    mProperty = pProperty;
    mInstanceVariable = pInstanceVariable;
    mName = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, getPropertyName());
    mType = mInstanceVariable.getType();
    mArgument = getInstanceVariableName().replaceFirst("m", "p");
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

  public String getArgument() {
    return mArgument;
  }
  
  public String getTypeName() {
    return mType.getName();
  }
  
  public String getPropertyName() {
    return mProperty.getName();
  }
}
