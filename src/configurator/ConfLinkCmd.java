package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfLinkCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*conf-link\\s+(\\w+)\\s+(\\d+)(\\s+(.+[^\\s]))?\\s*$");
    private Graph graph;

    public ConfLinkCmd(Graph g) {
        super(LNK);
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
        int link = Integer.parseInt(m.group(2));
        String args = m.group(4);
        Node n = graph.getNode(name);
        n.setLinkConfig(link, args);
    }

    @Override
    public String toString() {
        return "conf-link <label> <port> <args>";
    }

    @Override
    public String helpString() {
        return toString()+"\n  configures link <port> on node <label> with <args>";
    }
}
