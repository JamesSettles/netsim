package exceptions;

public class BadDestAddress extends NetSimException{
    public BadDestAddress(String destAddr) {
        super("Destination address " + destAddr + "does not exist in this routing table");
    }
    
}
