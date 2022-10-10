package configurator;

import configurator.Graph;
import configurator.SimConfig;
import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spools the current network graph to the console
 */
public class DumpNetworkCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*dump-network\\s*$");
    private Graph graph;

    public DumpNetworkCmd(Graph g) {
        graph = g;
    }

    @Override
    public boolean matches(String inp) {
        Matcher m = ptrn.matcher(inp);
        return m.matches();
    }

    public void execute(String inp) {
        if(!matches(inp)) {
            throw new BadCommandRouting(this.getClass().toString(), inp);
        }
        Collection<Node> nodes = graph.getAllNodes();
        Collection<Wire> edges = graph.getAllEdges();

        System.out.println("--BEGIN DUMP--\nnodes\n-----------------");
        for(Node n:nodes) {
            System.out.println(n.toString()+" "+n.stateInfo());
        }
        System.out.println("\nedges\n-----------------");
        for(Wire w:edges) {
            System.out.println(w);
        }
        System.out.println("--END DUMP--");
    }

    @Override
    public String toString() {
        return "dump-network";
    }

    @Override
    public String helpString() {
        return toString()+"\nProduces a list of all nodes and edges in the current configuration.";
    }
}
