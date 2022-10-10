package exceptions;

/** Indicates a layer configuration is missing */
public class LayerNotConfigured extends NetSimException {
    public LayerNotConfigured(String l) {
        super("Layer "+l+" is incompletely configured.");
    }
}
