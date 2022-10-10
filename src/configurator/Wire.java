package configurator;

import exceptions.PortNotAvailable;
import physicalLayer.Port;

/**
 * Representation of an edge in the graph
 */
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
     * @param a side A
     * @param ap port on A side
     * @param b side B
     * @param bp port on B side
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

    /**
     * Gets the port on the A side
     * @return port A
     */
    public Port getPortA() { return aPort; }

    /**
     * Gets the port on the B side
     * @return port B
     */
    public Port getPortB() { return bPort; }

    /**
     * Simulates cutting the wire
     */
    public void cutWire() { isCut = true; }

    /**
     * Simulates repairing the wire
     */
    public void fixWire() { isCut = false; }

    /**
     * Checks if the wire is currently cut
     * @return true if cut
     */
    public boolean isCut() { return isCut; }

    public String toString() {
        return sideA.toString()+"["+aPnum+"] -> "+sideB.toString()+"["+bPnum+"]";
    }
}
