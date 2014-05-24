package org.easybeans.core;

/**
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public class MetaImport {
  String mName;

  public MetaImport(String pName) {
    mName = pName;
  }
  
  public String getName() {
    return mName;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mName == null) ? 0 : mName.hashCode());
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
    if (!(obj instanceof MetaImport))
      return false;
    MetaImport other = (MetaImport) obj;
    if (mName == null) {
      if (other.mName != null)
        return false;
    } else if (!mName.equals(other.mName))
      return false;
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetaImport [mName=" + mName + "]";
  }
}
