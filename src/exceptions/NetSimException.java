package exceptions;

/** Parent for all exceptions.
 * <p>Should not be thrown directly; subclasses are expected to be thrown.</p>
 */
public class NetSimException extends RuntimeException {
    protected NetSimException(String msg) {
        super(msg);
    }
}
