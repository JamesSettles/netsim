package applicationLayer;

import configurator.Logger;

public class MsgSendApp extends Application implements Runnable {
    Thread execThread;
    String myMsg;

    @Override
    public void receiveFromTransport(byte[] data) {
        String output = new String(data);
        Logger.log(output);
    }

    @Override
    public void bringUp() {
        super.bringUp();
        execThread = new Thread(this);
        execThread.setDaemon(true);
        execThread.start();
    }

    @Override
    public void recvLaunchArgs(String args) {
        myMsg = args;
    }

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
        try {
            execThread.join(); // Need to join because of an annoying problem with pipes if a writing thread ends
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
