package exceptions;

/** Indicates a node label was reused */
public class DuplicateNodeLabel extends NetSimException {
    public DuplicateNodeLabel(String lbl) {
        super(lbl+" already in use");
    }
}
