package transportLayer;

import applicationLayer.Application;
import configurator.Logger;

/**
 * No transport running; useful for routers where data should never reach the Application Layer
 */
public class NoTransport extends Transport {

    @Override
    public void addApplication(Application a) {}

    @Override
    public void removeApplication(Application a) {

    }

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
        Logger.log("Transport unexpectedly received information!\n"+Logger.hex(bits));
    }

    @Override
    public void receiveFromApplication(Application a, byte[] bits) {
        getNetworkLayer().receiveFromTransport(bits);
    }

    @Override
    public byte[] toRawBytes(TransportMessage m) {
        return m.data();
    }

    @Override
    public TransportMessage fromRawBytes(byte[] bits) {
        return new TransportMessage(null,bits);
    }
}
