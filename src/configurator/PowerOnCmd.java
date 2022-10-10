package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigCommand to power on nodes
 */
public class PowerOnCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*power-on\\s+(\\w+)\\s*$");
    private Graph graph;

    public PowerOnCmd(Graph g) {
        graph = g;
    }

    @Override
    public boolean matches(String inp) {
        Matcher m = ptrn.matcher(inp);
        return m.matches();
    }

    @Override
    public void execute(String inp) {
        Matcher m = ptrn.matcher(inp);
        if(!m.matches()) {
            throw new BadCommandRouting(this.getClass().toString(), inp);
        }
        if(m.group(1).equals("ALL")) {
            Collection<Node> nodes = graph.getAllNodes();
            for(Node n:nodes) {
                n.powerOn();
            }
            return;
        }
        Node n = graph.getNode(m.group(1));
        n.powerOn();
    }

    @Override
    public String toString() {
        return "power-on <label>";
    }

    @Override
    public String helpString() {
        return toString()+"\n  turns on node <label>";
    }
}
