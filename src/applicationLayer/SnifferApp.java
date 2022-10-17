package applicationLayer;

import configurator.Logger;
import networkLayer.NetworkPacket;
import networkLayer.DVMeta;
public class SnifferApp extends Application{

    @Override
    public void receiveFromTransport(byte[] data) {
        NetworkPacket np = getTransport().getNetworkLayer().fromRawBytes(data);
        String dest = ((DVMeta)np.meta()).getDest();
        String source = ((DVMeta)np.meta()).getSource();
        Logger.log("Msg sniffed: Source: "+ source + ", Dest: " +dest);
    }
    
}
