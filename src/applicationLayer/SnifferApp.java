package applicationLayer;

import configurator.Logger;
import networkLayer.*;
public class SnifferApp extends Application implements Runnable{
    String recvdMsg = "";
    public void setSniffable(){
        ((DVNetwork)getTransport().getNetworkLayer()).setIsSniffable();
    }

    @Override
    public void receiveFromTransport(byte[] data) {
        NetworkPacket np = getTransport().getNetworkLayer().fromRawBytes(data);
        String dest = ((DVMeta)np.meta()).getDest();
        String source = ((DVMeta)np.meta()).getSource();
        if(new String(np.data()).equals("\0")){
            Logger.log("Msg sniffed: Source: "+ source + ", Dest: " +dest+ ", Msg:" + recvdMsg);
            recvdMsg = "";
        }else{
            recvdMsg += new String(np.data());
        }
    }

    public void bringUp(){
        super.bringUp();
        setSniffable();
    }

    @Override
    public void run() {
    }
    
}
