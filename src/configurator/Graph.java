package configurator;

import exceptions.DuplicateNodeLabel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Graph {
    HashMap<String,Node> nodes = new HashMap<String,Node>();
    ArrayList<Wire> edges = new ArrayList<Wire>();

    public Collection<Node> getAllNodes() { return nodes.values(); }
    public Node getNode(String lbl) {
        return nodes.get(lbl);
    }
    public void newNode(String lbl, int degree) throws DuplicateNodeLabel {
        if(nodes.containsKey(lbl)) {
            throw new DuplicateNodeLabel(lbl);
        }
        Node n = new Node(lbl, degree);
        nodes.put(lbl,n);
    }
    public void addEdge(Node a, int ap, Node b, int bp) {
        Wire w = new Wire(a, ap, b, bp);
        edges.add(w);
    }
    public Collection<Wire> getAllEdges() { return new ArrayList<Wire>(edges); }
}
