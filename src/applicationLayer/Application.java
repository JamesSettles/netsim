package applicationLayer;

import exceptions.LayerNotConfigured;
import networkLayer.NetworkPacket;
import transportLayer.Transport;

public abstract class Application {
    private Transport transportLayer;

    public void bringUp() {
        if(transportLayer==null) {
            throw new LayerNotConfigured("application");
        }
        getTransport().addApplication(this);
    }

    public void bringDown() {
        transportLayer.removeApplication(this);
    }

    public void setTransport(Transport t) { transportLayer=t; }
    public Transport getTransport() { return transportLayer; }

    public abstract void receiveFromTransport(byte[] data);

    /**
     * Method to allow optional arguments passed at launch time to be processed
     * @param args all the arguments to this application
     */
    public void recvLaunchArgs(String args) {
    }
}
