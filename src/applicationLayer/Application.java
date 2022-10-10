package applicationLayer;

import exceptions.LayerNotConfigured;
import networkLayer.NetworkPacket;
import transportLayer.Transport;

/**
 * Classes that can be used as applications
 */
public abstract class Application {
    /** The Transport instance used by this Application instance */
    private Transport transportLayer;

    /**
     * Method called to start the application
     * <p>Overrides of this method should call super.bringUp() to connect the
     * application to the Transport. If an override starts threads, these
     * threads should be daemon threads to avoid a deadlock on exit.</p>
     */
    public void bringUp() {
        if(transportLayer==null) {
            throw new LayerNotConfigured("application");
        }
        getTransport().addApplication(this);
    }

    /**
     * Method colled to stop the application
     * <p>Overrides of this method should call super.bringDown() to disconnect
     * the application from the Transport. If threads were started in
     * bringUp(), these threads should join(0) to avoid a broken pipe.</p>
     */
    public void bringDown() {
        transportLayer.removeApplication(this);
    }

    /**
     * Sets the Transport instance to access the network with
     * @param t Transport instance
     */
    public void setTransport(Transport t) { transportLayer=t; }

    /**
     * Gets the Transport instance to access the network with
     * @return
     */
    public Transport getTransport() { return transportLayer; }

    /**
     * Called by the Transport when data is available for the Application
     * @param data byte[] of the Application data
     */
    public abstract void receiveFromTransport(byte[] data);

    /**
     * Method to allow optional arguments passed at launch time to be processed
     * @param args all the arguments to this application
     */
    public void recvLaunchArgs(String args) {
    }
}
