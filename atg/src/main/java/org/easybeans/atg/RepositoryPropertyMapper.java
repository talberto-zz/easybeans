package org.easybeans.atg;

import static com.google.common.base.Preconditions.checkArgument;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class RepositoryPropertyMapper {

  protected static Logger sLog = LoggerFactory.getLogger(RepositoryBeanMapper.class);
  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
  protected Method mReader;
  protected Method mWriter;
  protected PropertyDescriptor mProperty;
  protected String mRepositoryPropertyName;
  
  public static RepositoryPropertyMapper from(PropertyDescriptor pProperty) {
    return new RepositoryPropertyMapper(pProperty);
  }
  
  protected RepositoryPropertyMapper(PropertyDescriptor pProperty) {
    mProperty = pProperty;
    
    mLog.debug("Extracting information from property [{}]", mProperty.getName());
    mReader = pProperty.getReadMethod();
    mWriter = mProperty.getWriteMethod();

    checkArgument(mReader != null || mWriter != null, "Neither reader nor writer for property [{}]", mProperty.getName());
    
    RepositoryProperty readerAnnotation = mReader != null ? mReader.getAnnotation(RepositoryProperty.class) : null;
    RepositoryProperty writerAnnotation = mWriter != null ? mWriter.getAnnotation(RepositoryProperty.class) : null;
    
    checkArgument(readerAnnotation != null || writerAnnotation != null, "Neither reader nor writer are annotated with @RepositoryProperty for property {}", mProperty.getName());
    checkArgument(!(readerAnnotation != null && writerAnnotation != null), "Both reader and writer are annotated with @RepositoryProperty for property {}", mProperty.getName());
    
    boolean configureWithReader = readerAnnotation == null ? false : true;
    RepositoryProperty propertyAnnotation = configureWithReader ? readerAnnotation : writerAnnotation;
    
    if(configureWithReader) {
      mLog.debug("Configuring property using reader method");
    } else {
      mLog.debug("Configuring property using writer method");
    }

    propertyAnnotation = mReader.getAnnotation(RepositoryProperty.class);
    if(propertyAnnotation != null) {
      mRepositoryPropertyName = propertyAnnotation.propertyName(); // Name of the property in the Repository
      mLog.debug("Property configured: [propertyName=[{}]]", mRepositoryPropertyName);
    }
  }

  /**
   * @return the repositoryPropertyName
   */
  public String getRepositoryPropertyName() {
    return mRepositoryPropertyName;
  }
  
  public String getBeanPropertyName() {
    return mProperty.getName();
  }

  public void setBeanProperty(Object pBean, Object pPropertyValue) {
    try {
      mWriter.invoke(pBean, pPropertyValue);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public Class<?> getPropertyBeanType() {
    return mProperty.getPropertyType();
  }
}
