package exceptions;

public class UnsupportedSize extends NetSimException {
    public UnsupportedSize(String layer, long len) {
        super("Data size "+len+" is not supported in the "+layer+" layer.");
    }
}
