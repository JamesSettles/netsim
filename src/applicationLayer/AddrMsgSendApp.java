package applicationLayer;

import networkLayer.NetworkPacket;
import networkLayer.StaticMeta;

import java.nio.ByteBuffer;

public class AddrMsgSendApp extends Application implements Runnable {
    /** thread for sending the characters */
    Thread execThread;
    /** message text to send */
    String myMsg;
    short dest;

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
        String[] parts = args.split("\\|");
        dest = Short.parseShort(parts[0]);
        myMsg = parts[1];
    }

    /**
     * Thread to send the characters ~500ms apart
     */
    @Override
    public void run() {
        byte[] msg = myMsg.getBytes();
        byte[] buf = new byte[1];
        StaticMeta meta = new StaticMeta(dest);
        NetworkPacket np = new NetworkPacket(meta, buf);
        for(int i=0; i<msg.length; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
            buf[0] = msg[i];
            getTransport().receiveFromApplication(this, getTransport().getNetworkLayer().toRawBytes(np));
        }
        getTransport().removeApplication(this);
    }
}
