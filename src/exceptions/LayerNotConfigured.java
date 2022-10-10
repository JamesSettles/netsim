package exceptions;

public class LayerNotConfigured extends NetSimException {
    public LayerNotConfigured(String l) {
        super("Layer "+l+" is incompletely configured.");
    }
}
