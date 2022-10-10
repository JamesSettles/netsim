package transportLayer;

import applicationLayer.Application;
import exceptions.LayerNotConfigured;
import networkLayer.Network;

import java.util.Set;

/**
 * Transport Layer representation
 */
public abstract class Transport {
    private Network networkLayer;

    public void setNetworkLayer(Network n) { networkLayer=n; }
    public Network getNetworkLayer() { return networkLayer; }

    /**
     * Called by the Application during bringUp() to register itself with the transport layer
     * @param a the Application to be registered
     */
    public abstract void addApplication(Application a);
    public abstract void removeApplication(Application a);

    public void bringUp() {
        if(networkLayer==null) { throw new LayerNotConfigured("transport"); }
    }
    public abstract void bringDown();

    public abstract void interrupt();

    public abstract void receiveFromNetwork(byte[] p);

    public abstract void receiveFromApplication(Application a, byte[] m);

    public abstract byte[] toRawBytes(TransportMessage m);
    public abstract TransportMessage fromRawBytes(byte[] bits);
}
