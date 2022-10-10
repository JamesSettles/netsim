package linkLayer;

import exceptions.LayerNotConfigured;
import networkLayer.Network;
import physicalLayer.Port;
import transportLayer.TransportMessage;

/**
 * Link layer representatino
 */
public abstract class Link {
    Port physicalLayer;
    Network networkLayer;

    public Port getPhysicalLayer() { return physicalLayer; }
    public void setPhysicalLayer(Port p) { physicalLayer = p; }
    public Network getNetworkLayer() { return networkLayer; }
    public void setNetworkLayer(Network n) { networkLayer = n; }

    public void bringUp() {
        if(physicalLayer==null || networkLayer==null) {
            throw new LayerNotConfigured("link");
        }
    }
    public abstract void bringDown();

    public abstract void interrupt();

    public abstract void receiveFromNetwork(byte[] data);

    public abstract void receiveFromPhysical(byte[] bits);

    public abstract byte[] toRawBytes(LinkFrame m);
    public abstract LinkFrame fromRawBytes(byte[] bits);
}
