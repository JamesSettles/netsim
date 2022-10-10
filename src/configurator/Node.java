package configurator;

import applicationLayer.Application;
import exceptions.ClassNotFound;
import exceptions.PortNotAvailable;
import exceptions.ReservedWordUsed;
import linkLayer.Link;
import networkLayer.Network;
import physicalLayer.NoPort;
import physicalLayer.Port;
import transportLayer.Transport;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Representation of a node in the graph
 */
public class Node implements Runnable {
    String label;

    SimConfig conf;

    private Wire[] edges;

    ArrayList<Application> apps = new ArrayList<>();
    Transport transLayer;
    private String transConfig;
    Network netLayer;
    private String netConfig;
    Link[] linkLayer;
    private String[] linkConfig;
    private Port[] ports;
    private String[] portConfig;

    Thread interruptor;
    boolean running = false;

    public Node(String n, int degree) {
        if(n.equals("ALL")) {
            throw new ReservedWordUsed("ALL may not be a node label!");
        }
        label=n;
        edges = new Wire[degree];
        ports = new Port[degree];

        portConfig = new String[degree];
        linkConfig = new String[degree];
    }

    public int getDegree() { return edges.length; }

    public String getLabel() { return label; }

    public boolean portAvail(int ap) {
        return edges[ap]==null;
    }

    /**
     * Connects a wire but does not start it
     * @param ap port to connect to
     * @param wire wire to connect
     * @return the port instance the wire is connected to
     */
    public Port connect(int ap, Wire wire) {
        if(edges[ap] != null) {
            throw new PortNotAvailable(ap);
        }
        edges[ap] = wire;
        ports[ap] = SimConfig.getConfig().newPort();
        return ports[ap];
    }

    /**
     * Launches an application on this node
     * @param app name of the Application class to be instantiated
     * @param args any additional information for the application
     */
    public void launchApplication(String app, String args) {
        Application a = null;
        try {
            a = (Application)Class.forName(app).getConstructor().newInstance();
            a.recvLaunchArgs(args);
            apps.add(a);
            a.setTransport(transLayer);
            a.bringUp();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException | ClassNotFoundException e) {
            throw new ClassNotFound(app);
        }
    }

    /**
     * Starts the node
     */
    public void powerOn() {
        // Instantiate all the parts
        // instantiate link
        linkLayer = new Link[getDegree()];
        for(int i=0; i<getDegree(); i++) {
            // A port without a wire might exist...
            if(ports[i]==null) {
                Logger.warn(getLabel()+" port"+i+" is not wired and NoPort instance will be used");
                ports[i] = new NoPort();
            }
            linkLayer[i] = SimConfig.getConfig().newLink();
            linkLayer[i].setPhysicalLayer(ports[i]);
            ports[i].setLinkLayer(linkLayer[i]);
        }
        // instantiate network
        netLayer = SimConfig.getConfig().newNetwork();
        netLayer.setLinks(linkLayer);
        // instantiate transport
        transLayer = SimConfig.getConfig().newTransport();
        transLayer.setNetworkLayer(netLayer);
        netLayer.setTransportLayer(transLayer);

        // Pass in additional startup arguments
        Configurable c;
        for(int i=0; i<ports.length; i++) {
            Port p = ports[i];
            if(p!=null && portConfig[i]!=null) {
                try {
                    c = (Configurable) p;
                    c.configureWith(portConfig[i]);
                } catch (ClassCastException e) {
                    // It's not configurable... skip...
                }
            }
        }
        for(int i=0; i<linkLayer.length; i++) {
            Link l = linkLayer[i];
            if(l!=null && linkConfig[i]!=null) {
                try {
                    c = (Configurable) l;
                    c.configureWith(linkConfig[i]);
                } catch (ClassCastException e) {
                    // It's not configurable... skip...
                }
            }
        }
        if(netLayer!=null && netConfig!=null) {
            try {
                c = (Configurable) netLayer;
                c.configureWith(netConfig);
            } catch (ClassCastException e) {
                // It's not configurable... skip...
            }
        }
        if(transLayer!=null && transConfig!=null) {
            try {
                c = (Configurable) transLayer;
                c.configureWith(transConfig);
            } catch (ClassCastException e) {
                // It's not configurable... skip...
            }
        }

        // Bring up the layers
        for(int i=0; i<getDegree(); i++) {
            ports[i].bringUp();
            linkLayer[i].setNetworkLayer(netLayer);
            linkLayer[i].bringUp();
        }
        netLayer.bringUp();
        transLayer.bringUp();

        running = true;
        interruptor = new Thread(this);
    }

    /**
     * Stops the node
     */
    public void powerOff() {
        if(!running) { return; } // If the node isn't up, there's nothing to bring down
        running = false;
        interruptor = null;

        // Bring down the layers
        transLayer.bringDown();
        netLayer.bringDown();
        for(int i=0; i<getDegree(); i++) {
            linkLayer[i].bringDown();
        }
    }

    /**
     * Logic to call interrupts on all layers
     */
    public void run() {
        while(running) {
            long st = System.currentTimeMillis();
            for(Port p:ports) { p.interrupt(); }
            for(Link l:linkLayer) { l.interrupt(); }
            netLayer.interrupt();
            transLayer.interrupt();
            long et = System.currentTimeMillis();
            long dt = et-st;
            if(dt<0) {
                Logger.log("Interrupt time window exceeded for realtime operation!");
                continue;
            }
            while(dt>0) {
                try {
                    Thread.sleep(conf.interruptFrequency - dt);
                } catch (InterruptedException e) {
                }
                dt = System.currentTimeMillis() - et;
            }
        }
    }

    public String toString() {
        return label;
    }

    /**
     * Information about the node
     * @return additional node info
     */
    public String stateInfo() {
        if(running) {
            return "R";
        } else {
            return "H";
        }
    }

    public void setPhysConfig(int port, String args) {
        portConfig[port] = args;
    }
    public void setLinkConfig(int link, String args) {
        linkConfig[link] = args;
    }
    public void setNetConfig(String args) {
        netConfig = args;
    }
    public void setTransConfig(String args) {
        transConfig = args;
    }
}
