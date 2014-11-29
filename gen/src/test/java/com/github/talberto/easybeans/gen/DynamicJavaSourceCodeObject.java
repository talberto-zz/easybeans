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

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Creates a dynamic source code file object
 * 
 * This is an example of how we can prepare a dynamic java source code for compilation. This class reads the java code
 * from a string and prepares a JavaFileObject
 * 
 */
public class DynamicJavaSourceCodeObject extends SimpleJavaFileObject {
  private final String qualifiedName;
  private final String sourceCode;

  /**
   * Converts the name to an URI, as that is the format expected by JavaFileObject
   * 
   * 
   * @param fully
   *          qualified name given to the class file
   * @param code
   *          the source code string
   */
  public DynamicJavaSourceCodeObject(String name, String code) {
    super(URI.create("string:///" + name.replace(".", "/") + Kind.SOURCE.extension), Kind.SOURCE);
    this.qualifiedName = name;
    this.sourceCode = code;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return sourceCode;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public String getSourceCode() {
    return sourceCode;
  }
}
