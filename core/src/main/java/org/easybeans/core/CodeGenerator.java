package org.easybeans.core;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.STGroupFile;

public class CodeGenerator {
  
  private static final String TEMPLATES_BASEDIR = "/org/easybeans/core/template";

  STGroup mGroup;
  Logger mLogger = LoggerFactory.getLogger("");
  
  public CodeGenerator() {
    try {
      URL url = this.getClass().getResource(TEMPLATES_BASEDIR);
      File dir = new File(url.toURI());
      mGroup = new STGroupDir(dir.getAbsolutePath());
    } catch (URISyntaxException e) {
      mLogger.error("Error finding the templates", e);
    }
  }
  
  public String generate(ClassDefinition pClassDefinition) throws URISyntaxException {
    ST st = mGroup.getInstanceOf("class_def");
    st.add("class", pClassDefinition);
    return st.render();
  }

  public String generate(MetaClass pBean) {
    ST st = getBeanTemplate();
    st.add("bean", pBean);
    return st.render();
  }

  private ST getBeanTemplate() {
    try {
      URL url = this.getClass().getResource(TEMPLATES_BASEDIR + "/" + "class.stg");
      File file = new File(url.toURI());
      STGroup group = new STGroupFile(file.getAbsolutePath());
      return group.getInstanceOf("class_decl");
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("The url for the group file is wrong", e);
    }
  }

  public String generate(MetaBean pBean) {
    MetaClass metaClass = pBean.getMetaClass();
    ST template = getBeanTemplate();
    template.add("class", metaClass);
    return template.render();
  }
}
