package exceptions;

/** Indicates a ConfigCommand matched but now can't run */
public class BadCommandRouting extends NetSimException {
    public BadCommandRouting(String classname, String inp) {
        super(classname+" can't process the input "+inp);
    }
}
