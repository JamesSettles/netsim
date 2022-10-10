package configurator;

import applicationLayer.Application;
import exceptions.ClassNotFound;
import exceptions.PortNotAvailable;
import exceptions.ReservedWordUsed;
import linkLayer.Link;
import networkLayer.Network;
import physicalLayer.Port;
import transportLayer.Transport;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Node implements Runnable {
    String label;

    SimConfig conf;

    private Wire[] edges;

    ArrayList<Application> apps = new ArrayList<>();
    Transport transLayer;
    Network netLayer;
    Link[] linkLayer;
    private Port[] ports;

    Thread interruptor;
    boolean running = false;

    public Node(String n, int degree) {
        if(n.equals("ALL")) {
            throw new ReservedWordUsed("ALL may not be a node label!");
        }
        label=n;
        edges = new Wire[degree];
        ports = new Port[degree];
    }

    public int getDegree() { return edges.length; }

    public String getLabel() { return label; }

    public boolean portAvail(int ap) {
        return edges[ap]==null;
    }

    public Port connect(int ap, Wire wire) {
        if(edges[ap] != null) {
            throw new PortNotAvailable(ap);
        }
        edges[ap] = wire;
        ports[ap] = SimConfig.getConfig().newPort();
        return ports[ap];
    }

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

    public void powerOn() {
        // Instantiate all the parts
        // instantiate link
        linkLayer = new Link[getDegree()];
        for(int i=0; i<getDegree(); i++) {
            if(ports[i]!=null) {
                linkLayer[i] = SimConfig.getConfig().newLink();
                linkLayer[i].setPhysicalLayer(ports[i]);
                ports[i].setLinkLayer(linkLayer[i]);
            }
        }
        // instantiate network
        netLayer = SimConfig.getConfig().newNetwork();
        netLayer.setLinks(linkLayer);
        // instantiate transport
        transLayer = SimConfig.getConfig().newTransport();
        transLayer.setNetworkLayer(netLayer);
        netLayer.setTransportLayer(transLayer);

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

    public String stateInfo() {
        if(running) {
            return "R";
        } else {
            return "H";
        }
    }
}
