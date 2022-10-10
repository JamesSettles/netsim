package configurator;

import applicationLayer.NoApplication;
import configurator.commands.ConfigCommand;
import exceptions.ClassNotFound;
import linkLayer.Link;
import linkLayer.NoLink;
import networkLayer.Network;
import networkLayer.NoNetwork;
import physicalLayer.PipeBackedPort;
import physicalLayer.Port;
import transportLayer.NoTransport;
import transportLayer.Transport;

import java.lang.reflect.InvocationTargetException;

/**
 * Singleton to contain the simulation state
 */
public class SimConfig {
    private static final SimConfig config = new SimConfig();

    // ms between interrupt calls
    // All layers must complete
    protected long interruptFrequency = 1000;

    // Default layer implementations
    protected Class defaultPhysLayer = PipeBackedPort.class;
    protected void setDefaultPhysLayer(String classname) {
        try {
            defaultPhysLayer = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFound(classname);
        }
    }
    protected Class defaultLinkLayer = NoLink.class;
    protected void setDefaultLinkLayer(String classname) {
        try {
            defaultLinkLayer = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFound(classname);
        }
    }
    protected Class defaultNetworkLayer = NoNetwork.class;
    protected void setDefaultNetworkLayer(String classname) {
        try {
            defaultNetworkLayer = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFound(classname);
        }
    }
    protected Class defaultTransportLayer   = NoTransport.class;
    protected void setDefaultTransportLayer(String classname) {
        try {
            defaultTransportLayer = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFound(classname);
        }
    }

    // Logical representation of the network
    private Graph network = new Graph();
    protected Graph getGraph() { return network; }

    private SimConfig() {
        // When making the singleton register the available configurator commands
        ConfigCommand.registerCommand(new AddNodeCmd(network));
        ConfigCommand.registerCommand(new AddWireCmd(network));
        ConfigCommand.registerCommand(new DumpNetworkCmd(network));
        ConfigCommand.registerCommand(new PowerOnCmd(network));
        ConfigCommand.registerCommand(new PowerOffCmd(network));
        ConfigCommand.registerCommand(new SetPhysCmd());
        ConfigCommand.registerCommand(new SetLinkCmd());
        ConfigCommand.registerCommand(new SetNetCmd());
        ConfigCommand.registerCommand(new SetTransCmd());
        ConfigCommand.registerCommand(new LaunchAppCmd(network));
        ConfigCommand.registerCommand(new ConfPhysCmd(network));
        ConfigCommand.registerCommand(new ConfLinkCmd(network));
        ConfigCommand.registerCommand(new ConfNetCmd(network));
        ConfigCommand.registerCommand(new ConfTransCmd(network));
    }

    public static SimConfig getConfig() {
        return config;
    }

    /**
     * Generate a new Port of the currently set class
     * @return a new Port instance
     */
    public Port newPort() {
        try {
            return (Port) defaultPhysLayer.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | RuntimeException
                 | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a new Link of the currently set class
     * @return a new Link instance
     */
    public Link newLink() {
        try {
            return (Link) defaultLinkLayer.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | RuntimeException
                 | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a new Network of the currently set class
     * @return a new Network instance
     */
    public Network newNetwork() {
        try {
            return (Network) defaultNetworkLayer.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | RuntimeException
                 | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a new Transport of the currently set class
     * @return a new Transport instance
     */
    public Transport newTransport() {
        try {
            return (Transport) defaultTransportLayer.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | RuntimeException
                 | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
