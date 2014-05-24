package org.easybeans.atg;

import static org.easymock.EasyMock.createNiceMock;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.easybeans.core.MetaBean;
import org.junit.Ignore;
import org.junit.Test;

import atg.repository.Repository;


public class RepositoryBeansExtractorTest {

  @Ignore
  @Test
  public void testExtraction() {
    Repository repo = createNiceMock(Repository.class);
    List<String> beanNames = Arrays.asList("fakeBean");
    
    RepositoryBeansExtractor extractor = new RepositoryBeansExtractor();
    
    List<MetaBean> extractedBeans = extractor.extractBeans(repo, beanNames);
    assertThat("Incorrect number of extracted beans", extractedBeans, hasSize(1));
    MetaBean testedBean = extractedBeans.get(0);
    assertThat("Incorrect name for the extracted bean", testedBean.getName(), equalTo("fakeBean"));
  }
}
