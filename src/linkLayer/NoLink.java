package linkLayer;

/**
 * A link layer that doesn't add any processing services... which is ok for physical layers with no
 * data loss or corruption.
 */
public class NoLink extends Link
{
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
    public void receiveFromNetwork(byte[] bits) {
        getPhysicalLayer().receiveFromLink(bits);
    }

    @Override
    public void receiveFromPhysical(byte[] bits) {
        LinkFrame lf = fromRawBytes(bits);
        getNetworkLayer().recieveFromLink(lf.data());
    }

    @Override
    public byte[] toRawBytes(LinkFrame lf) {
        return lf.data();
    }

    @Override
    public LinkFrame fromRawBytes(byte[] bits) {
        return new LinkFrame(null,bits);
    }
}
