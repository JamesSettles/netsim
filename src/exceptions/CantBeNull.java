package exceptions;

public class CantBeNull extends NetSimException {
    public CantBeNull(String var) {
        super("Can't pass a null value for "+var);
    }
}
