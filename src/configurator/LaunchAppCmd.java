package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LaunchAppCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*launch\\s+(\\w+)\\s+([\\w\\.]+)(\\s+(\\w+))?\\s*$");
    private Graph graph;

    @Override
    public String commandSection() {
        return "Application";
    }

    public LaunchAppCmd(Graph g) {
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
        String args = m.group(3);
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
