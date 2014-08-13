/**
 * Copyright 2014 Tomas Rodriguez 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License. 
 */

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
