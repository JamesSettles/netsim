package exceptions;

public class BrokenLayer extends NetSimException {
    public BrokenLayer(String l) {
        super("Unexpected runtime break in "+l+" layer");
    }
}
