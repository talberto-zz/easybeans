package org.easybeans.atg;

/**
 * Signals an error extracting bean definition
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class ExtractionException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -332858168467666664L;

  public ExtractionException() {
    super();
  }

  public ExtractionException(String pMessage, Throwable pCause) {
    super(pMessage, pCause);
  }

  public ExtractionException(String pMessage) {
    super(pMessage);
  }

  public ExtractionException(Throwable pCause) {
    super(pCause);
  }
}
