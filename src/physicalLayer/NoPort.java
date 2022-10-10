package physicalLayer;

import configurator.Wire;

/**
 * A port without a wire must be allowed to exist... but without a wire no data can ever
 * traverse the port. This port does nothing.
 */
public class NoPort extends Port {
    @Override
    public void bringDown() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void receiveFromLink(byte[] bits) {

    }

    @Override
    public void connectWire(Wire w) {

    }
}
