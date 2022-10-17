package applicationLayer;

import configurator.Logger;
import networkLayer.*;
public class SniffEditApp extends Application implements Runnable{
    String recvdMsg = "";
    String addrToEditMsgsOf;
    public void setEditable(){
        ((DVNetwork)getTransport().getNetworkLayer()).setAddrToEditMsgsOf(addrToEditMsgsOf);
    }

    @Override
    public void receiveFromTransport(byte[] data) {
    }

    @Override
    public void recvLaunchArgs(String args) {
        this.addrToEditMsgsOf = args;
    }

    public void bringUp(){
        super.bringUp();
        setEditable();
    }

    @Override
    public void run() {
    }
    
}
