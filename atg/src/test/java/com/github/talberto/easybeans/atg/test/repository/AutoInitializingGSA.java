package com.github.talberto.easybeans.atg.test.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import atg.adapter.gsa.InitializingGSA;

public class AutoInitializingGSA extends InitializingGSA {

  
  @Override
  public File[] getImportFiles () {
    File[] configuredImportFiles = super.getImportFiles();
    File[] importFiles = getAutoImportFiles();
    if (importFiles != null && configuredImportFiles != null) {
      importFiles = (File[]) ArrayUtils.addAll(importFiles, configuredImportFiles);
    }
    if (importFiles == null) {
      importFiles = configuredImportFiles;
    }
    return importFiles;
  }

  protected File[] getAutoImportFiles () {
    File[] configPath = getNucleus().getConfigPath();
    List<File> dataFiles = new ArrayList<File>();
    for (File configLayer : configPath) {
      if (configLayer.isDirectory() && configLayer.getName().equals("config")) {
        File dataFile = new File(configLayer, getAbsoluteName() + "-data.xml");
        if (dataFile.exists()) {
          if (dataFile.isFile()) {
            if (isLoggingInfo()) {
              logInfo("Data file found " + dataFile.getAbsolutePath());
            }
            dataFiles.add(dataFile);
          }
        }
      }
    }
    File[] dataPathArray = new File[dataFiles.size()];
    return (File[]) dataFiles.toArray(dataPathArray);
  }
}
