package exceptions;

/** Indicates that the requested port is already in use */
public class PortNotAvailable extends NetSimException {
    public PortNotAvailable(int p) {
        super("Port "+p+" already in use");
    }
}