package exceptions;

public class CantCreateLayer extends NetSimException {
    public CantCreateLayer(String l) {
        super("Can't create "+l+" layer.");
    }
}