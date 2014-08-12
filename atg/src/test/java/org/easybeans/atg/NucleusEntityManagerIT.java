package org.easybeans.atg;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.servlet.ServletException;

import org.easybeans.core.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.nucleus.Nucleus;
import atg.nucleus.NucleusTestUtils;
import atg.nucleus.ServiceException;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 * 
 */
public class NucleusEntityManagerIT {

  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
  protected Nucleus mNucleus = null;
  protected EntityManager mEntityManager = null;
  protected Repository mSimpleRepository;
  
  @Before
  public void setUp() throws Exception {
    mLog.info("Start Nucleus.");
    try {
      mNucleus = NucleusTestUtils.startNucleusWithModules(new String[] { "DAF.Deployment", "DPS" }, this.getClass(), "");
      
      assertNotNull(mNucleus);
      mEntityManager = (EntityManager) mNucleus.resolveName("/easybeans/EntityManager");
      assertNotNull(mEntityManager);
      mSimpleRepository = (Repository) mNucleus.resolveName("/easybeans/SimpleRepository");
      assertNotNull(mSimpleRepository);
    } catch (ServletException e) {
      fail(e.getMessage());
    }
  }

  @After
  public void tearDown() {
    mLog.info("Stop Nucleus");
    if (mNucleus != null) {
      try {
        NucleusTestUtils.shutdownNucleus(mNucleus);
      } catch (ServiceException e) {
        fail(e.getMessage());
      } catch (IOException e) {
        fail(e.getMessage());
      }
    }
  }

  @Ignore
  @Test
  public void testFind() {
    SimpleItem item = mEntityManager.find(SimpleItem.class, "simpleItem1");
    
    assertThat("The fetched item is null", item, notNullValue());
    assertThat("The id is incorrect", item.getId(), equalTo("simpleItem1"));
    assertThat("The stringProperty value is incorrect", item.getStringProperty(), equalTo("Hello World!"));
  }
  
  @Test
  public void testCreate() throws RepositoryException {
    SimpleItem simpleItem = new SimpleItem();
    simpleItem.setStringProperty("A random value");
    
    String id = mEntityManager.create(simpleItem);
    
    RepositoryItem item = mSimpleRepository.getItem(id, "simpleItem");
    assertNotNull("The repository item retrieved is null", item);
    assertThat("The repository id isn't correct", item.getRepositoryId(), equalTo(id));
    assertThat("The bean id isn't correct", simpleItem.getId(), equalTo(id));
    assertThat("The property stringProperty isn't correct", (String) item.getPropertyValue("stringProperty"), equalTo("A random value"));
  }
  
  
  @Test
  public void testUpdate() throws RepositoryException {
    SimpleItem simpleItem = new SimpleItem();
    simpleItem.setId("simpleItem1");
    simpleItem.setStringProperty("Updated value");
    
    mEntityManager.update(simpleItem);
    RepositoryItem item = mSimpleRepository.getItem(simpleItem.getId(), "simpleItem");
    assertThat("The property stringProperty isn't correct", (String) item.getPropertyValue("stringProperty"), equalTo("Updated value"));
  }
}
