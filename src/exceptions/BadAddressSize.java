package exceptions;

public class BadAddressSize extends NetSimException {
    /** Indicates a ConfigCommand matched but now can't run */
    public BadAddressSize(int givenSize) {
        super("Address must be of size 2. Given size: " + givenSize);
    }
}

