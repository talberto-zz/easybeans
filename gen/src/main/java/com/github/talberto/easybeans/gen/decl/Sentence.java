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

package com.github.talberto.easybeans.gen.decl;

import com.google.common.collect.ImmutableList;

/**
 * Models a Java statement
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public abstract class Sentence {
  public static Sentence newAssignment(Identifier pRight, Identifier pLeft) {
    return Assignment.create(pRight, pLeft);
  }
  
  public static SentenceBlock newSentenceBlock(Sentence... pSentences) {
    return SentenceBlock.create(ImmutableList.copyOf(pSentences));
  }
}
