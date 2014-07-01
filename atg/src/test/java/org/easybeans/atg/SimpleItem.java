package org.easybeans.atg;

@RepositoryBean(
    repository = "/easybeans/SimpleRepository",
    descriptorName = "simpleItem")
public class SimpleItem {

  private String mStringProperty;
  private String mId; 
  
  /**
   * @return the id
   */
  @RepositoryId
  public String getId() {
    return mId;
  }

  /**
   * @param pId the id to set
   */
  public void setId(String pId) {
    mId = pId;
  }
  
  /**
   * @return the stringProperty
   */
  @RepositoryProperty(propertyName = "stringProperty")
  public String getStringProperty() {
    return mStringProperty;
  }

  /**
   * @param pStringProperty the stringProperty to set
   */
  public void setStringProperty(String pStringProperty) {
    mStringProperty = pStringProperty;
  }
}
