package exceptions;

public class PortNotAvailable extends NetSimException {
    public PortNotAvailable(int p) {
        super("Port "+p+" already in use");
    }
}