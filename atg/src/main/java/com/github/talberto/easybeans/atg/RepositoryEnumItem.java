package com.github.talberto.easybeans.atg;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Comparator;

import com.google.common.collect.Ordering;

public class RepositoryEnumItem implements Comparable<RepositoryEnumItem> {
  /** Lexicographical ordering */
  public final static Ordering<RepositoryEnumItem> ORDERING = Ordering.from(new Comparator<RepositoryEnumItem>() {
    @Override
    public int compare(RepositoryEnumItem pO1, RepositoryEnumItem pO2) {
      return pO1.getCode() - pO2.getCode();
    }
  }).compound(new Comparator<RepositoryEnumItem>() {
    @Override
    public int compare(RepositoryEnumItem pO1, RepositoryEnumItem pO2) {
      return pO1.getValue().compareTo(pO2.getValue());
    }
  });
  
  final protected int mCode;
  final protected String mValue;
  
  public RepositoryEnumItem(int pCode, String pValue) {
    checkNotNull(pCode, "The code cannot be null");
    checkNotNull(pValue, "The value cannot be null");
    
    mCode = pCode;
    mValue = pValue;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return mValue;
  }

  /**
   * @return the code
   */
  public int getCode() {
    return mCode;
  }

  @Override
  public int compareTo(RepositoryEnumItem pO) {
    return ORDERING.compare(this, pO);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + mCode;
    result = prime * result + ((mValue == null) ? 0 : mValue.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof RepositoryEnumItem))
      return false;
    RepositoryEnumItem other = (RepositoryEnumItem) obj;
    if (mCode != other.mCode)
      return false;
    if (mValue == null) {
      if (other.mValue != null)
        return false;
    } else if (!mValue.equals(other.mValue))
      return false;
    return true;
  }
}
