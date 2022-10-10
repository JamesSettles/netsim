package exceptions;

/** Indicates the requested class couldn't be loaded/instantiated by the runtime */
public class ClassNotFound extends NetSimException {
    public ClassNotFound(String cls) {
        super("Class "+cls+" was not found by the class loader.");
    }
}
