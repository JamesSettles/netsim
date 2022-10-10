package exceptions;

/** Indicates a null was detected somewhere null should not occur */
public class CantBeNull extends NetSimException {
    public CantBeNull(String var) {
        super("Can't pass a null value for "+var);
    }
}
