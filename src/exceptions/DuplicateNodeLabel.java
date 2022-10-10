package exceptions;

public class DuplicateNodeLabel extends NetSimException {
    public DuplicateNodeLabel(String lbl) {
        super(lbl+" already in use");
    }
}
