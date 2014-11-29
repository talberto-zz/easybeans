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

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.google.common.base.Charsets;

/**
 * A <code>BeanRenderer</code> is an object capable of rendering {@link BeanDefinition}, for example, 
 * generating the content of java source file.
 * 
 * @author Tomás Rodríguez (rodriguez@progiweb.com)
 *
 */
public class BeanRenderer {

  public void render(BeanDefinition pBean, Writer pWriter) throws IOException {
    ST st = getST(pBean);
    
    String str = st.render();
    pWriter.write(str);
  }
  
  public String render(BeanDefinition pBean) {
    ST st = getST(pBean);
    
    return st.render();
  }

  protected ST getST(BeanDefinition pBean) {
    URL templateFileUrl = this.getClass().getResource("template/bean_definition.stg");
    checkState(templateFileUrl != null, "Couldn't find template file");
    STGroup stGroup = new STGroupFile(templateFileUrl, Charsets.UTF_8.name(), '<', '>');
    ST st = stGroup.getInstanceOf("bean_definition");
    st.add("bean", pBean);
    checkState(st != null, "Couldn't get an instance of bean_definitin template");
    return st;
  }
}
