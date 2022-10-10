package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNodeCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*add-node\\s+(\\w+)\\s+(\\d+)\\s*$");
    private Graph graph;

    public AddNodeCmd(Graph g) {
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
        int d = Integer.parseInt(m.group(2));
        graph.newNode(m.group(1),d);
    }

    @Override
    public String toString() {
        return "add-node <label> <degree>";
    }

    @Override
    public String helpString() {
        return toString()+"\n  Adds a new node to the network with name <label> and <degree> ports";
    }
}
