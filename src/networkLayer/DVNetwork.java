package networkLayer;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import configurator.Configurable;
import configurator.Logger;
import exceptions.BadDestAddress;

public class DVNetwork extends Network implements Configurable  {
    // Routing table that maps address to an array containing port and hop count
    HashMap<String, int[]> routingTable;
    int numLinks;
    String addr;
    // Variables that concern packet sniffing
    Boolean isSniffable = false;
    String addrToEditMsgsOf = " ";
    
    /**
     * Sends the routing table out on all ports
     */
    public void broadCastRoutingTable(){
        // Wake up msg formatted as myAddress:connection1,hopCount:conection2,hopcount etc 
        String wakeUpMsg = addr;
        for (HashMap.Entry<String, int[]> entry : routingTable.entrySet()) {
            wakeUpMsg+=":";
            String addr = entry.getKey();
            int hopCount = entry.getValue()[1];
            wakeUpMsg += addr + "," + hopCount;
        }
        byte[] msg = wakeUpMsg.getBytes();
        // Msg w destination 
        DVMeta meta = new DVMeta("None",this.addr);
        NetworkPacket np = new NetworkPacket(meta, msg);
        for(int linkNum = 0;linkNum < numLinks;linkNum++){
            links[linkNum].receiveFromNetwork(toRawBytes(np));
        }
    }
    /**
     * Parses a received routing table broadcast message
     * @param receivedBroadcast broadcast routing table message
     */
    public void processReceivedBroadCast(String receivedBroadcast){
        Boolean receivedNewEntry = false;
        String addr;
        int port;
        int hopCount;
        String[] splitBroadcast = receivedBroadcast.split(":");
        // gets the port from the routing table
        port = routingTable.get(splitBroadcast[0])[0];
        for (int i = 1; i <splitBroadcast.length;i++){
            addr =  splitBroadcast[i].split(",")[0];
            // Hop count should be +1 of whatever the hop was to your neighbors
            hopCount =  Integer.valueOf(splitBroadcast[i].split(",")[1]) + 1;
            // put into routing table if not yoursel
            if(!addr.equals(this.addr)){
                // Check for addr already added to routing table
                // If the addr is already present, only replace that entry with new entry if the hopCount is lower
                // This means each routing table only contains the fastest route to each addr
                if(routingTable.containsKey(addr) && routingTable.get(addr)[1] <= hopCount){
                    continue;
                }
                routingTable.put(addr,new int[]{port,hopCount}); 
                receivedNewEntry = true;
            }
        }
        if(receivedNewEntry){
            broadCastRoutingTable();
        }
    }

    /**
     * When the network layer is brought up it introspects about the number of links and then
     * broadcasts the routing table
     */
    @Override
    public void bringUp(){
        numLinks = links.length;
        broadCastRoutingTable();
    }
    /**
     * Powering off a node prints out its routing table. This is largley for testing purposes.
     */
    @Override
    public void bringDown() {
        // TODO Auto-generated method stub
        // Print routing table on bring down 
        System.out.println(this.addr + "'s routing table:");
        for (HashMap.Entry<String, int[]> entry : routingTable.entrySet()) {
            System.out.println("Address: " + entry.getKey() + " Port: " + entry.getValue()[0] + " Hop Count: " + entry.getValue()[1]);
        }

    }

    @Override
    public void interrupt() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void receiveFromTransport(byte[] p) {
        // TODO Auto-generated method stub
        recieveFromLink(p);        
    }
    /**
     * Receive bytes from the link layer, 
     * parse them into network packets, 
     * forward them or send them to application layer,
     * or process recieved routing tables
     */
    @Override
    public void recieveFromLink(byte[] f) {
        NetworkPacket np = fromRawBytes(f);
        String dest = ((DVMeta)np.meta()).getDest();
        String source = ((DVMeta)np.meta()).getSource();
        byte[] data = np.data();
        if(dest.equals("None"))
            processReceivedBroadCast(new String(data));
        else if(dest.equals(this.addr)){
            getTransportLayer().receiveFromNetwork(toRawBytes(np));
        }else if(!dest.equals(this.addr)){
            // forward packet
            if(isSniffable){
                getTransportLayer().receiveFromNetwork(toRawBytes(np));
            }
            if(this.addrToEditMsgsOf.equals(dest)){
                np = editSniffedMsg(np);
            }
            forwardPacket(dest, np);
            
        }
    }
    /**
     * Forwards a packet to given addr
     * @param dest addr to forward to 
     * @param np packet to forward
     */
    public void forwardPacket(String dest, NetworkPacket np){
        if(routingTable.containsKey(dest)){
            int port = routingTable.get(dest)[0];
            links[port].receiveFromNetwork(toRawBytes(np));
        }
        else{
            throw new BadDestAddress(dest);
        }
    }
    /**
     * Converts from networkPacket to raw bytes
     * Converts a string containing {dest + ";" source + ";" + data} to bytes
     * @return result of calling .getBytes on the string version of the network packet
     */
    @Override
    public byte[] toRawBytes(NetworkPacket np) {
        // just construct a string and turn it into raw bytes
        // dest + ; source + ; + data
        if (((DVMeta)np.meta()).getSource() == null){
            ((DVMeta)np.meta()).setSource(this.addr);
        }
        byte[] processedNP = (((DVMeta)np.meta()).getDest() + ";" + ((DVMeta)np.meta()).getSource() + ";").getBytes();
        byte[] result = Arrays.copyOf(processedNP, processedNP.length + np.data().length);
        System.arraycopy(np.data(), 0, result, processedNP.length, np.data().length);
        return result;
    }
    /**
     * Converts the bits to a string and parses that string to form the network packet
     * @param bits bit representation of the network packets
     * @return fully formed network packet
     */
    @Override
    public NetworkPacket fromRawBytes(byte[] bits) {
        String strMsg = new String(bits);
        String data = "";
        String dest = "";
        String source = "";
        String[] splitMsg = strMsg.split(";");
        for(int i = 0; i < splitMsg.length;i++){
            if(i==0)
                dest = splitMsg[i];
            else if (i == 1)
                source = splitMsg[i];
            else
                data+=splitMsg[i];
        }
        DVMeta meta =  new DVMeta(dest,source);
        NetworkPacket np = new NetworkPacket(meta, data.getBytes());
        return np;
    }
    /**
     * Reads in a file containing a node's addr and its immediate neighbors ports and addresses,
     * adds them to its inital routing table
     * @param filename name of the file to readFrom
     */
    @Override
    public void configureWith(String filename) {
        routingTable = new HashMap<String,int[]>();
        try {
            File f = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            // First line should be our addr
            this.addr = line;
            line = br.readLine();
            // Next lines are all immediate port connections
            while (line!=null) {
                processLine(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            Logger.log("Could not read routing table from "+ filename);
        }
    }
    /**
     * Parses a line in the config file and adds its information to the routing table
     * @param line line to be parsed
     */
    public void processLine(String line){
        String[] broken_line = line.split(" ");
        String addr = broken_line[0];
        int[] portAndHopCount  = new int[]{Integer.valueOf(broken_line[1]),1};
        routingTable.put(addr,portAndHopCount); 
    }

    /******************
     * PACKET SNIFFING
     ******************/

    /**
     * Allows packets to be sniffed by the application layer
     */
    public void setIsSniffable(){
        this.isSniffable = true;
    }
    /**
     * Part of packet sniffing.
     * Allows messages to certain addresses to be edited by the application layer. 
     */
    public void setAddrToEditMsgsOf(String addrToEditMsgsOf){
        this.addrToEditMsgsOf = addrToEditMsgsOf;
    }

    /**
     * Replaces each char of sniffed msg with "z"
     * @param np original NetworkPacket to be edited 
     * @return edited network packet
     */
    public NetworkPacket editSniffedMsg(NetworkPacket np){
        // null char shouldn't be edited
        if(new String(np.data()).equals("\0")){
            return np;
        }
        NetworkPacket newNp = new NetworkPacket(np.meta(),"z".getBytes());
        return newNp;
    }

}
