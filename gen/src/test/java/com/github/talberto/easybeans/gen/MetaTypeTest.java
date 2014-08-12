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
