package transportLayer;

import applicationLayer.Application;
import configurator.Logger;

import java.util.ArrayList;

/**
 * Transport layer that sends a copy of everything to every application
 */
public class NullTransport extends Transport {
    ArrayList<Application> apps = new ArrayList<>();

    @Override
    public void addApplication(Application a) {
        apps.add(a);
    }

    @Override
    public void removeApplication(Application a) {
        apps.remove(a);
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
        for(Application a:apps) {
            a.receiveFromTransport(bits);
        }
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
