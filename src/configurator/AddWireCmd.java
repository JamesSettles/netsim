package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddWireCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*add-wire\\s+(\\w+)\\s+(\\d+)\\s+(\\w+)\\s+(\\d+)\\s*$");
    private Graph graph;

    /**
     * AddWireCmd attach to a particular graph
     * @param g Graph to place nodes in
     */
    public AddWireCmd(Graph g) {
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
        String aLbl = m.group(1);
        int aDeg = Integer.parseInt(m.group(2));
        String bLbl = m.group(3);
        int bDeg = Integer.parseInt(m.group(4));

        Node a = graph.getNode(aLbl);
        Node b = graph.getNode(bLbl);

        graph.addEdge(a, aDeg, b, bDeg);
    }

    @Override
    public String toString() {
        return "add-wire <A label> <A port> <B label> <B port>";
    }

    @Override
    public String helpString() {
        return toString()+"\n  Adds a wire between nodes <A label> and <B label> using ports <A port> and <B port> respectively";
    }
}
