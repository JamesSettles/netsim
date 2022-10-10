package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfTransCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*conf-trans\\s+(\\w+)(\\s+(.+[^\\s]))?\\s*$");
    private Graph graph;

    public ConfTransCmd(Graph g) {
        super(TRN);
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
        n.setTransConfig(args);
    }

    @Override
    public String toString() {
        return "conf-trans <label> <args>";
    }

    @Override
    public String helpString() {
        return toString()+"\n  configures transport layer on node <label> with <args>";
    }
}
