package org.easybeans.atg;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.easybeans.core.MetaBean;
import org.easybeans.core.MetaProperty;
import org.junit.Test;

import atg.beans.DynamicPropertyDescriptor;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItemDescriptor;

import com.google.common.base.CaseFormat;


public class RepositoryBeansExtractorTest {

  private static final Class<Boolean> PROPERTY_TYPE = Boolean.class;
  private static final String PROPERTY_NAME = "booleanProperty";
  private static final String BEAN_NAME = "fakeBean";

  @Test
  public void testExtraction() throws RepositoryException {
    Repository repo = buildRepository();
    List<String> beanNames = Arrays.asList(BEAN_NAME);
    
    RepositoryBeansExtractor extractor = new RepositoryBeansExtractor();
    
    List<MetaBean> extractedBeans = extractor.extractBeans(repo, beanNames);
    assertThat("Incorrect number of extracted beans", extractedBeans, hasSize(1));
    MetaBean extractedBean = extractedBeans.get(0);
    assertThat("Incorrect name for the extracted bean", extractedBean.getName(), equalTo(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, BEAN_NAME)));
    assertThat("Incorrect number of extracted properties", extractedBean.getProperties(), hasSize(1));
    MetaProperty extractedProperty = extractedBean.getProperties().get(0);
    assertThat("Incorrect property name", extractedProperty.getName(), equalTo(PROPERTY_NAME));
    assertThat("Incorrect property type", extractedProperty.getType().getName(), equalTo(PROPERTY_TYPE.getSimpleName()));
  }

  private Repository buildRepository() throws RepositoryException {
    Repository repo = createNiceMock(Repository.class);
    RepositoryItemDescriptor fakeItemDescriptor = createNiceMock(RepositoryItemDescriptor.class);
    DynamicPropertyDescriptor booleanPropertyDescriptor = createNiceMock(DynamicPropertyDescriptor.class);
    
    expect(repo.getItemDescriptor(BEAN_NAME)).andReturn(fakeItemDescriptor).anyTimes();
    expect(fakeItemDescriptor.getPropertyDescriptors()).andReturn(new DynamicPropertyDescriptor[] {booleanPropertyDescriptor}).anyTimes();
    expect(booleanPropertyDescriptor.getName()).andReturn(PROPERTY_NAME).anyTimes();
    expect(booleanPropertyDescriptor.getPropertyType()).andReturn(PROPERTY_TYPE).anyTimes();
    
    replay(repo, fakeItemDescriptor, booleanPropertyDescriptor);
    
    return repo;
  }
}