package org.easybeans.atg;

/**
 * 
 * @author Tomas Rodriguez (rodriguez@progiweb.com)
 *
 */
public class MappingException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 5921314140734021682L;

  public MappingException() {
    super();
  }

  public MappingException(String pMessage, Throwable pCause) {
    super(pMessage, pCause);
  }

  public MappingException(String pMessage) {
    super(pMessage);
  }

  public MappingException(Throwable pCause) {
    super(pCause);
  }

}
