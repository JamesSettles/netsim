package networkLayer;

import exceptions.LayerNotConfigured;
import linkLayer.Link;
import transportLayer.Transport;

/**
 * Network Layer representation
 */
public abstract class Network {
    Link[] links;
    Transport transportLayer;

    public void setLinks(Link[] l) { links=l; }
    public void setTransportLayer(Transport t) { transportLayer=t; }
    public Link[] getLinkLayer() { return links; }
    public Transport getTransportLayer() { return transportLayer; }

    public void bringUp() {
        if(links==null || transportLayer==null) {
            throw new LayerNotConfigured("network");
        }
        for(Link l:links) {
            if(l==null) {
                throw new LayerNotConfigured("network");
            }
        }
    }
    public abstract void bringDown();

    public abstract void interrupt();

    public abstract void receiveFromTransport(byte[] p);

    public abstract void recieveFromLink(byte[] f);

    public abstract byte[] toRawBytes(NetworkPacket p);
    public abstract NetworkPacket fromRawBytes(byte[] bits);
}
