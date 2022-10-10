package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigCommand that starts an application on a node
 */
public class LaunchAppCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*launch\\s+(\\w+)\\s+([\\w\\.]+)(\\s+(.+[^\\s]))?\\s*$");
    private Graph graph;

    public LaunchAppCmd(Graph g) {
        super(APP);
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
        String name = m.group(1);
        String clazz = m.group(2);
        String args = m.group(4);
        Node n = graph.getNode(name);
        n.launchApplication(clazz, args);
    }

    @Override
    public String toString() {
        return "launch <label> <AppClassname> [args]";
    }

    @Override
    public String helpString() {
        return toString()+"\n  launches <AppClassname> on node <label> with [args]";
    }
}
