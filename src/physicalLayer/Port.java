package physicalLayer;

import configurator.Wire;
import exceptions.LayerNotConfigured;
import linkLayer.Link;

/**
 * Physical Layer representation
 */
public abstract class Port {
    Link linkLayer;

    public void setLinkLayer(Link l) { linkLayer = l; }
    public Link getLinkLayer() { return linkLayer; }

    public void bringUp() {
        if(linkLayer==null) { throw new LayerNotConfigured("physical"); }
    }
    public abstract void bringDown();

    public abstract void interrupt();

    public abstract void receiveFromLink(byte[] bits);

    public abstract void connectWire(Wire w);

}
