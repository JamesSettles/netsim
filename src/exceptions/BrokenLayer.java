package exceptions;

/** Indicates a layer that started isn't working */
public class BrokenLayer extends NetSimException {
    public BrokenLayer(String l) {
        super("Unexpected runtime break in "+l+" layer");
    }
}
