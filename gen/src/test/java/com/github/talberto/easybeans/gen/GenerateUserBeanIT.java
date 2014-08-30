package com.github.talberto.easybeans.gen;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.nucleus.Nucleus;
import atg.nucleus.NucleusTestUtils;
import atg.nucleus.ServiceException;
import atg.repository.Repository;

public class GenerateUserBeanIT {

  public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
  public static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
  
  protected Logger mLog = LoggerFactory.getLogger(this.getClass());
  protected Nucleus mNucleus = null;
  protected Repository mUserRepository;
  
  @Before
  public void setUp() throws Exception {
    mLog.info("Start Nucleus.");
    try {
      mNucleus = NucleusTestUtils.startNucleusWithModules(new String[] { "DAF.Deployment", "DPS" }, this.getClass(), "");
      
      assertNotNull(mNucleus);
      mUserRepository = (Repository) mNucleus.resolveName("/easybeans/UserRepository");
      assertNotNull(mUserRepository);
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
}
