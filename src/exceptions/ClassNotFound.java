package exceptions;

public class ClassNotFound extends NetSimException {
    public ClassNotFound(String cls) {
        super("Class "+cls+" was not found by the class loader.");
    }
}
