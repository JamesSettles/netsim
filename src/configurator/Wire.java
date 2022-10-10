package configurator;

import exceptions.PortNotAvailable;
import physicalLayer.Port;

public class Wire {
    Node sideA;
    Node sideB;

    Port aPort;
    int aPnum;
    Port bPort;
    int bPnum;

    boolean isCut;

    /**
     * Wire creation
     * <p>Creation of a wire also creates the ports in both nodes and connects the wire and activates it.</p>
     * @param a
     * @param ap
     * @param b
     * @param bp
     */
    public Wire(Node a, int ap, Node b, int bp) {
        sideA = a;
        sideB = b;
        isCut = false;

        // Try to plug the plug the wire into the ports
        if(sideA.portAvail(ap) && sideB.portAvail(bp)) {
            aPort = sideA.connect(ap, this);
            aPnum = ap;
            bPort = sideB.connect(bp, this);
            bPnum = bp;
        } else {
            if(!sideA.portAvail(ap)) {
                throw new PortNotAvailable(ap);
            } else {
                throw new PortNotAvailable(bp);
            }
        }
        // Uses the physical layer's connectWire to activate the wire
        aPort.connectWire(this);
    }

    public Port getPortA() { return aPort; }
    public Port getPortB() { return bPort; }

    public void cutWire() { isCut = true; }
    public void fixWire() { isCut = false; }
    public boolean isCut() { return isCut; }

    public String toString() {
        return sideA.toString()+"["+aPnum+"] -> "+sideB.toString()+"["+bPnum+"]";
    }
}
