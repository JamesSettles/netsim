package networkLayer;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import configurator.Configurable;
import configurator.Logger;
import exceptions.BadDestAddress;


/*
     * Wake-up
    Step 1:
    -- Wake up and introspect (learn my address, my network, # of links)
        - send out addr on all links on wake up
        - listen for addrs 
            - now have immediate addrs
        - broadcast immediate addrs 
    Step 2:
    -- Broadcast that "I'm alive" (This is my address and these are the addresses I know how to get to)
    Step 3:
    -- Declare victory since everything else is event driven...

    Receive Broadcast
    Step 1:
    -- Add to the routing table what ever networks were sent that I don't know abo
    ut
    Step 2:
    -- Be friendly and reciprocate (On all links, send my table)
    Step 3:
    -- Go to step 1...
     */

public class DVNetwork extends Network implements Configurable  {
    // Routing table that maps address to an array containing port and hop count
    HashMap<String, int[]> routingTable;
    int numLinks;
    String addr; // addr is a two char string
    
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
            // Send out addr
            links[linkNum].receiveFromNetwork(toRawBytes(np));
            // Send out immediate addrs and hop count
        }

    }
    @Override
    public void bringUp(){
        numLinks = links.length;
        broadCastRoutingTable();
    }
    
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

    @Override
    public void recieveFromLink(byte[] f) {
        // TODO Auto-generated method stub
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
            forwardPacket(dest, np);
        }
    }

    public void forwardPacket(String dest, NetworkPacket np){
        if(routingTable.containsKey(dest)){
            int port = routingTable.get(dest)[0];
            links[port].receiveFromNetwork(toRawBytes(np));
        }
        else{
            throw new BadDestAddress(dest);
        }
    }

    public void processReceivedBroadCast(String receivedBroadcast){
        // Some of this code is technically redundant because the broadcast message no longer needs to
        // contain the source of the msg
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
                // This means each routing table only contains the fastest route
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

    @Override
    public void configureWith(String s) {
        routingTable = new HashMap<String,int[]>();
        try {
            File f = new File(s);
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
            Logger.log("Could not read routing table from "+s);
        }
    }
    public void processLine(String line){
        String[] broken_line = line.split(" ");
        String addr = broken_line[0];
        int[] portAndHopCount  = new int[]{Integer.valueOf(broken_line[1]),1};
        routingTable.put(addr,portAndHopCount); 
    }

}
