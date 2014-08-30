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

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class Assignment extends Sentence {
  protected final Identifier mIdentifier;
  protected final Expression mExpression;
  
  public static Sentence create(Identifier pRight, Expression pLeft) {
    return new Assignment(pRight, pLeft);
  }
  
  protected Assignment(Identifier pIdentifier, Expression pExpression) {
    mIdentifier = pIdentifier;
    mExpression = pExpression;
  }

  /**
   * @return the identifier
   */
  public Identifier getIdentifier() {
    return mIdentifier;
  }

  /**
   * @return the expression
   */
  public Expression getExpression() {
    return mExpression;
  }
}
