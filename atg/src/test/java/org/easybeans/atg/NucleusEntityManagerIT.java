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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.nucleus.Nucleus;
import atg.nucleus.NucleusTestUtils;
import atg.nucleus.ServiceException;
import atg.repository.Repository;

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
//      System.setProperty("derby.locks.deadlockTrace", "true");
//      // Use the DBUtils utility class to get JDBC properties for an in memory
//      // HSQL DB called "testdb".
//      Properties props = DBUtils.getHSQLDBInMemoryDBConnection("testdb");
//
//      // Start up our database
//      DBUtils db = new DBUtils(props);
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

  @Test
  public void testFind() {
    SimpleItem item = mEntityManager.find(SimpleItem.class, "simpleItem1");
    
    assertThat("The fetched item is null", item, notNullValue());
    assertThat("The stringProperty value is incorrect", item.getStringProperty(), equalTo("Hello World!"));
  }
}
