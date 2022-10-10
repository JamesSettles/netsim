package applicationLayer;

import configurator.Logger;
import exceptions.LayerNotConfigured;

public class LogSpoolApp extends Application {
    /**
     * Spools what ever is received from the transport to the log stream
     * @param data byte[] of the Application data
     */
    @Override
    public void receiveFromTransport(byte[] data) {
        String output = new String(data);
        Logger.log(output);
    }

}
