package exceptions;

/** Indicates a layer couldn't be initialized */
public class CantCreateLayer extends NetSimException {
    public CantCreateLayer(String l) {
        super("Can't create "+l+" layer.");
    }
}