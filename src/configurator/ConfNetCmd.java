package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfNetCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*conf-net\\s+(\\w+)(\\s+(.+[^\\s]))?\\s*$");
    private Graph graph;

    public ConfNetCmd(Graph g) {
        super(NET);
        graph = g;
    }
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
        String name = m.group(1);
        String args = m.group(3);
        Node n = graph.getNode(name);
        n.setNetConfig(args);
    }

    @Override
    public String toString() {
        return "conf-net <label> <args>";
    }

    @Override
    public String helpString() {
        return toString()+"\n  configures network layer on node <label> with <args>";
    }
}
