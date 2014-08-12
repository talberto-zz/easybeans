package com.github.talberto.easybeans.gen;

public class PropertyDefinition {

  String mName;
  boolean mReadable;
  boolean mWritable;
  Class<?> mType;
  
  public PropertyDefinition() {
  }
  
  public Class<?> getType() {
    return mType;
  }

  public void setType(Class<?> pType) {
    mType = pType;
  }
  
  public String getName() {
    return mName;
  }
  public void setName(String pName) {
    mName = pName;
  }
  public boolean isReadable() {
    return mReadable;
  }
  public void setReadable(boolean pReadable) {
    mReadable = pReadable;
  }
  public boolean isWritable() {
    return mWritable;
  }
  public void setWritable(boolean pWritable) {
    mWritable = pWritable;
  }
  
}
