package exceptions;

/** Indicates a reserved word in this context has been used */
public class ReservedWordUsed extends NetSimException{
    public ReservedWordUsed(String msg) {
        super(msg);
    }
}
