package applicationLayer;

import networkLayer.NetworkPacket;
import networkLayer.StaticMeta;
import networkLayer.DVMeta;


import configurator.Logger;

public class UnicastChatApp extends Application implements Runnable {
    /** thread for sending the characters */
    Thread execThread;
    /** message text to send */
    String myMsg;
    String dest;
    String source;
    String recvdMsg = "";


    /**
     * Does nothing
     * @param data byte[] of the Application data
     */
    @Override
    public void receiveFromTransport(byte[] data) {
        NetworkPacket np = getTransport().getNetworkLayer().fromRawBytes(data);
        String dest = ((DVMeta)np.meta()).getDest();
        String source = ((DVMeta)np.meta()).getSource();
        if(new String(np.data()).equals("\0")){
            Logger.log(dest+ ": " + "Msg from "+ source + ":" + recvdMsg);
            recvdMsg = "";
        }else{
            recvdMsg += new String(np.data());
        }

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
        if(args != null){
            String[] parts = args.split("\\|");
            dest = parts[0];
            myMsg = parts[1];
        }
        
    }
    public NetworkPacket getNullCharNetworkPacket(){
        byte[] msg = "\0".getBytes();
        DVMeta meta = new DVMeta(dest,source);
        NetworkPacket np = new NetworkPacket(meta, msg);
        return np;
    }
    /**
     * Thread to send the characters ~500ms apart
     */
    @Override
    public void run() {
        if(myMsg != null){
            byte[] msg = myMsg.getBytes();
            byte[] buf = new byte[1];
            DVMeta meta = new DVMeta(dest);
            NetworkPacket np = new NetworkPacket(meta, buf);
            for(int i=0; i<msg.length; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {}
                buf[0] = msg[i];
                getTransport().receiveFromApplication(this, getTransport().getNetworkLayer().toRawBytes(np));
            }
            getTransport().receiveFromApplication(this, getTransport().getNetworkLayer().toRawBytes(getNullCharNetworkPacket()));
            //getTransport().removeApplication(this);
        }
    }
}
