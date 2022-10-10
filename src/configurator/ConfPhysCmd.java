package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfPhysCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*conf-phys\\s+(\\w+)\\s+(\\d+)(\\s+(.+[^\\s]))?\\s*$");
    private Graph graph;

    public ConfPhysCmd(Graph g) {
        super(PHY);
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
        int port = Integer.parseInt(m.group(2));
        String args = m.group(4);
        Node n = graph.getNode(name);
        n.setPhysConfig(port, args);
    }

    @Override
    public String toString() {
        return "conf-phys <label> <port> <args>";
    }

    @Override
    public String helpString() {
        return toString()+"\n  configures port <port> on node <label> with <args>";
    }
}
