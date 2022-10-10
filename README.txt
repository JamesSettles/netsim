#CC NetSim
This is a network simulator written in JAVA. The simulator is intended to provide an easy way to play with network layer concerns.

The simulator is an interactive program that allows configuration of nodes and links while the simulation is running. Nodes may be exterior nodes or interior nodes. Interior nodes provide multiple links but cannot be connected to applications. Exterior nodes provide a single link and an interface to connect to an application. Links support setting a propagation delay and bandwidth.

Simulation timing is not precise. Rather than using a discrete event simulator approach, this simulator uses threads, pipes, and some sleeps.

Network layer logic is introduced to the simulator via dynamic loading. Include your NetworkLayer implementation in the class path.

#Running NetSim
Execution is started with configurator.Main

This is an interactive CLI application. When started, Main will present a menu of commands. To reprint the menu of commands, is the command "help"

#Wiring the Network
A network can be thought of as a graph, nodes and edges.
  - The "add-node" command adds a node to the graph.
  - The "add-wire" command adds an edge to the graph.
  - The "dump-network" command shows the current network topology.

The nodes connected by an edge must be created prior to creating the edge.

When the edge is created, the wire will also be instantiated and started between the two nodes. The physical layer is the only layer instantiated at started prior to nodes being "powered on".

#Configuring the Stack
Network layers, other than the physical layer and application layer, are instantiated when a node is powered on. The bringUp() methods for each node are called from bottom to top: links, network, and transport. When powered down, the bringDown() methods for each node are called from top to bottom: transport, network, links.
  - The "power-on" command powers on a specific node "power-on ALL" can be used to power on all of the nodes.
  - The "power-off" command powers off a specific node "power-off ALL" can be used to power off all of the nodes.

 Dynamic class loading is used to select the specific implementation at runtime, without needing to modify any existing code. Put the layer implementation class somewhere in the classpath and use the class name for loading.
   - The "set-physical" command sets the physical layer implementation to ues
   - The "set-link" command sets the link layer implementation to use
   - The "set-network" command sets the network layer implementation to use
   - The "set-transport" command sets the transport layer implementation to use

 Dynamic class loading is also used to start applications on nodes. Put the application layer implementation class somewhere in the classpath and use the class name for loading
   -- The "launch" starts an instance of a specefic app on a specific node

 #Example
 The following is a network with two nodes. lisa send characters to bob and bob logs them as they are received.

        set-transport transportLayer.NullTransport
        add-node bob 1
        add-node lisa 1
        add-wire bob 0 lisa 0
        power-on bob
        power-on lisa
        launch bob applicationLayer.LogSpoolApp
        launch lisa applicationLayer.MsgSendApp Hello