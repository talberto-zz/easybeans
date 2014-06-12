package org.easybeans.atg;

@RepositoryBean(
    repository = "/easybeans/SimpleRepository",
    descriptorName = "simpleItem")
public class SimpleItem {

  private String mStringProperty;

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
