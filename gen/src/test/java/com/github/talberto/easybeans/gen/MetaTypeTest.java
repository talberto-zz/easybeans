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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.github.talberto.easybeans.gen.MetaImport;
import com.github.talberto.easybeans.gen.MetaType;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class MetaTypeTest {

  static final MetaImport sListImport = new MetaImport("java.util.List");
  static final MetaImport sStringImport = new MetaImport("java.lang.String");
  
  @Test
  public void listOfString() {
    @SuppressWarnings("serial")
    MetaType type = MetaType.of(new TypeToken<List<String>>() {});
    
    Set<MetaImport> expectedImports = Sets.newHashSet(sListImport, sStringImport);
    
    assertThat("The MetaType.name is wrong", type.getName(), equalTo("List<String>"));
    assertThat("The MetaType.getImports are wrong", type.getImports(), equalTo(expectedImports));
  }
  
  @Test
  public void string() {
    @SuppressWarnings("serial")
    MetaType type = MetaType.of(new TypeToken<String>() {});

    Set<MetaImport> expectedImports = Sets.newHashSet(sStringImport);
    
    assertThat("The MetaType.name is wrong", type.getName(), equalTo("String"));
    assertThat("The MetaType.getImports are wrong", type.getImports(), equalTo(expectedImports));
  }
}
