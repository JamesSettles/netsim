package networkLayer;

import configurator.Configurable;
import configurator.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Network layer that uses a routing table read from disk */
public class StaticNetwork extends Network implements Configurable {
    HashMap<Integer, Integer> table;

    @Override
    public void bringDown() {

    }

    @Override
    public void interrupt() {

    }

    /**
     * This network layer requires the Transport to produce the NetworkPacket so that the
     * destination for the data is available for routing
     * @param p The byte[] representation of a NetworkPacket
     */
    @Override
    public void receiveFromTransport(byte[] p) {
        // Transport needs to pass in a NetworkPacket so that we know the dest
        NetworkPacket np = fromRawBytes(p);
        int addr = ((StaticMeta)np.meta()).getDest();
        int port = table.get(addr);
        if(port<0) {
            // This actually goes to localhost and should be unwrapped
            getTransportLayer().receiveFromNetwork(np.data());
            return;
        }
        if(port>=links.length) {
            Logger.log("Table references non-existant port!");
            return;
        }
        // This goes out a port and needs to be sent with it's dest info
        links[port].receiveFromNetwork(p);
    }

    @Override
    public void recieveFromLink(byte[] f) {
        receiveFromTransport(f);
    }

    @Override
    public byte[] toRawBytes(NetworkPacket np) {
        ByteBuffer bb = ByteBuffer.allocate(2 + np.data().length);
        short addr = (short)((StaticMeta)np.meta()).getDest();
        bb.asShortBuffer().put(0, addr);
        bb.position(2);
        bb.put(np.data());
        return bb.array();
    }

    @Override
    public NetworkPacket fromRawBytes(byte[] bits) {
        ByteBuffer bb = ByteBuffer.wrap(bits);
        short addr = bb.asShortBuffer().get();
        StaticMeta meta = new StaticMeta(addr);
        byte[] data = new byte[bits.length-2];
        bb.position(2);
        bb.get(data);
        NetworkPacket np = new NetworkPacket(meta, data);
        return np;
    }

    /**
     * This network opens the file s and reads in the routing table when configuredWith
     * is called.
     * @param s the file to read from
     */
    @Override
    public void configureWith(String s) {
        table = new HashMap<Integer,Integer>();
        try {
            File f = new File(s);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line!=null) {
                processLine(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            Logger.log("Could not read routing table from "+s);
        }
    }
    private static final Pattern ptrnTable = Pattern.compile("^\\s*(\\d+)\\s*,\\s*(-?\\d+)\\s*$");
    private void processLine(String line) {
        Matcher m = ptrnTable.matcher(line);
        if(m.matches()) {
            int addr = Integer.parseInt(m.group(1));
            int port = Integer.parseInt(m.group(2));
            table.put(addr,port);
            return;
        }
        Logger.log("StaticNetwork could not parse '"+line+"'");
    }
}
