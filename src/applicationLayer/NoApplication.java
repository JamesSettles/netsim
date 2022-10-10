package applicationLayer;

import configurator.Logger;

/**
 * No application running; useful for routers where data should never reach the Application Layer
 */
public class NoApplication extends Application {
    @Override
    public void bringUp() {
        super.bringUp();
    }

    @Override
    public void receiveFromTransport(byte[] data) {
        Logger.log("Application unexpectedly received information!\n"+Logger.hex(data));
    }
}
