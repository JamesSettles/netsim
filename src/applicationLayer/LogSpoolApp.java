package applicationLayer;

import configurator.Logger;
import exceptions.LayerNotConfigured;

public class LogSpoolApp extends Application {
    @Override
    public void receiveFromTransport(byte[] data) {
        String output = new String(data);
        Logger.log(output);
    }

}
