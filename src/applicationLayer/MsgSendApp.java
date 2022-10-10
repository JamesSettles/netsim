package applicationLayer;

import configurator.Logger;

public class MsgSendApp extends Application implements Runnable {
    /** thread for sending the characters */
    Thread execThread;
    /** message text to send */
    String myMsg;

    /**
     * Does nothing
     * @param data byte[] of the Application data
     */
    @Override
    public void receiveFromTransport(byte[] data) {

    }

    /**
     * Starts a thread to send each message character
     */
    @Override
    public void bringUp() {
        super.bringUp();
        execThread = new Thread(this);
        execThread.start();
    }

    /**
     * Sets the message to be displayed by this application
     * @param args all the arguments to this application
     */
    @Override
    public void recvLaunchArgs(String args) {
        myMsg = args;
    }

    /**
     * Thread to send the characters ~500ms apart
     */
    @Override
    public void run() {
        byte[] msg = myMsg.getBytes();
        byte[] buf = new byte[1];
        for(int i=0; i<msg.length; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
            buf[0] = msg[i];
            getTransport().receiveFromApplication(this, buf);
        }
        getTransport().removeApplication(this);

    }
}
