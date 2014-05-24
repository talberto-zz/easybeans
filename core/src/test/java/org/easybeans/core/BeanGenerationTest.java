package org.easybeans.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.easybeans.core.CodeGenerator;
import org.easybeans.core.MetaBean;
import org.easybeans.core.MetaProperty;
import org.easybeans.core.MetaType;
import org.easybeans.core.tools.DynamicJavaSourceCodeObject;
import org.junit.Test;

import com.google.common.reflect.TypeToken;

public class BeanGenerationTest {

  @Test
  public void testBeanGeneration() {
    @SuppressWarnings("serial")
    MetaType type = MetaType.of(new TypeToken<List<String>>() {});
    MetaProperty myProperty = new MetaProperty("myProperty", type);
    MetaBean bean = new MetaBean("myBeanName", Collections.singletonList(myProperty));
    CodeGenerator generator = new CodeGenerator();
    String code = generator.generate(bean);

    System.out.println(code);

    /* Creating dynamic java source code file object */
    SimpleJavaFileObject fileObject = new DynamicJavaSourceCodeObject("MyBeanName", code);
    JavaFileObject javaFileObjects[] = new JavaFileObject[] { fileObject };

    /* Instantiating the java compiler */
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    /**
     * Retrieving the standard file manager from compiler object, which is used to provide basic building block for
     * customizing how a compiler reads and writes to files.
     * 
     * The same file manager can be reopened for another compiler task. Thus we reduce the overhead of scanning through
     * file system and jar files each time
     */
    StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);

    /* Prepare a list of compilation units (java source code file objects) to input to compilation task */
    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(javaFileObjects);

    /* Prepare any compilation options to be used during compilation */
    // In this example, we are asking the compiler to place the output files under bin folder.
    String[] compileOptions = new String[] { };
    Iterable<String> compilationOptions = Arrays.asList(compileOptions);

    /* Create a diagnostic controller, which holds the compilation problems */
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

    /* Create a compilation task from compiler by passing in the required input objects prepared above */
    CompilationTask compilerTask = compiler.getTask(null, stdFileManager, diagnostics, compilationOptions, null, compilationUnits);

    // Perform the compilation by calling the call method on compilerTask object.
    boolean status = compilerTask.call();

    assertThat("Compilation status is not ok", status, equalTo(Boolean.TRUE));
  }
}
