package networkLayer;

import linkLayer.Link;

/**
 * A network layer that does no routing
 */
public class NoNetwork extends Network {
    @Override
    public void bringUp() {
        super.bringUp();
    }

    @Override
    public void bringDown() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void receiveFromTransport(byte[] bits) {
        Link[] links = getLinkLayer();
        for(Link l:links) {
            l.receiveFromNetwork(bits);
        }
    }

    @Override
    public void recieveFromLink(byte[] bits) {
        getTransportLayer().receiveFromNetwork(bits);
    }

    @Override
    public byte[] toRawBytes(NetworkPacket p) {
        return p.data();
    }

    @Override
    public NetworkPacket fromRawBytes(byte[] bits) {
        return new NetworkPacket(null,bits);
    }
}
