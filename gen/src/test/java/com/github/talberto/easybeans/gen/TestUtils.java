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

package com.github.talberto.easybeans.gen;

import java.beans.FeatureDescriptor;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Some utils for testing purposes...
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public class TestUtils {
  public static boolean contentEquals(Reader r1, Reader r2) throws IOException {
    int data1, data2;

    do {
      data1 = r1.read();
      data2 = r2.read();
      
      // Contents are different... return false
      if(data1 != data2) {
        return false;
      }
      // We reached the end of BOTH files and we didn't return... contents are equals
      if(data1 == -1 && data2 == -1) {
        return true;
      }
    } while(true);
  }
  
  public static FeatureDescriptor findDescriptorByName(List<? extends FeatureDescriptor> pPropertyDescriptors, final String pName) {
    return Iterables.find(pPropertyDescriptors, new Predicate<FeatureDescriptor>() {
      @Override
      public boolean apply(FeatureDescriptor pPropertyDescriptor) {
        if(pPropertyDescriptor.getName().equals(pName)) {
          return true;
        } else {
          return false;
        }
      }
    });
  }
}