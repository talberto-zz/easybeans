package com.github.talberto.easybeans.gen;

import java.io.IOException;
import java.io.Reader;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Hamcrest matchers for Streams
 * 
 * @author Tomás Rodríguez (rstomasalberto@gmail.com)
 *
 */
public class StreamMatchers {
  /**
   * A matcher that compares the contents of two readers
   * 
   * @author Tomás Rodríguez (rstomasalberto@gmail.com)
   *
   */
  static class ContentEqualsTo extends TypeSafeMatcher<Reader> {
    Reader expectedReader;
    
    ContentEqualsTo(Reader expectedReader) {
      this.expectedReader = expectedReader;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("not same contents");
    }

    @Override
    protected boolean matchesSafely(Reader actualReader) {
      try {
        return TestUtils.contentEquals(actualReader, expectedReader);
      } catch (IOException e) {
        throw new IllegalStateException("Couldn't finish to compare contents due to an unexpected exception", e);
      }
    }
    
    @Factory
    public static <T> Matcher<Reader> contentEqualsTo(Reader expectedReader) {
      return new ContentEqualsTo(expectedReader);
    }
  }
  
  public static <T> Matcher<Reader> contentEqualsTo(Reader expectedReader) {
    return new ContentEqualsTo(expectedReader);
  }
}