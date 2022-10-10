package configurator;

import exceptions.DuplicateNodeLabel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Arbitrary graph representation with a few specializations for representing networking
 */
public class Graph {
    /** Graph nodes */
    private HashMap<String,Node> nodes = new HashMap<String,Node>();
    /** Graph edges */
    private ArrayList<Wire> edges = new ArrayList<Wire>();

    /**
     * Yield a collection of all the graph nodes
     * @return all the nodes
     */
    public Collection<Node> getAllNodes() { return nodes.values(); }

    /** Yield a specific node
     *
     * @param lbl name of the node to yield
     * @return node with matching label
     */
    public Node getNode(String lbl) {
        return nodes.get(lbl);
    }

    /** Create a new node in the graph
     *
     * @param lbl name of the new node
     * @param degree number of ports for the node
     * @throws DuplicateNodeLabel if the name has already been used
     */
    public void newNode(String lbl, int degree) throws DuplicateNodeLabel {
        if(nodes.containsKey(lbl)) {
            throw new DuplicateNodeLabel(lbl);
        }
        Node n = new Node(lbl, degree);
        nodes.put(lbl,n);
    }

    /**
     * Adds an edge to the graph
     * @param a node on A side
     * @param ap port on A side
     * @param b node on B side
     * @param bp port on B side
     */
    public void addEdge(Node a, int ap, Node b, int bp) {
        Wire w = new Wire(a, ap, b, bp);
        edges.add(w);
    }

    /**
     * All of the edges in the graph
     * @return all the edges
     */
    public Collection<Wire> getAllEdges() { return new ArrayList<Wire>(edges); }
}
