package configurator.commands;

import configurator.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandSet {
    private static final String[] sectionOrder = {"General","Application","Transport","Network","Link","Physical"};
    HashMap<String,ArrayList<ConfigCommand>> commands = new HashMap<String,ArrayList<ConfigCommand>>();
    ArrayList<ConfigCommand> allCommands = new ArrayList<ConfigCommand>();

    public CommandSet() {
        for(String s:sectionOrder) {
            commands.put(s, new ArrayList<ConfigCommand>());
        }
    }

    public void add(ConfigCommand c) {
        commands.get(c.commandSection()).add(c);
        allCommands.add(c);
    }

    public void remove(ConfigCommand c) {
        commands.get(c.commandSection()).remove(c);
        allCommands.remove(c);
    }

    public void process(String inp) {
        for(ConfigCommand c:allCommands) {
            if(c.matches(inp)) {
                c.execute(inp);
                return;
            }
        }
        Logger.log("Could not match command "+inp);
    }

    public void usage() {
        for(String s:sectionOrder) {
            ArrayList<ConfigCommand> cmds = commands.get(s);
            if(cmds.size()==0) {
                continue;
            }
            System.out.println("-- "+s+" --");
            for(ConfigCommand c:cmds) {
                System.out.println(c);
            }
        }
    }
}
