package configurator.commands;

import configurator.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Keeps ConfigCommand instances for display/selection
 */
public class CommandSet {
    /** Section headers for commands */
    private static final String[] sectionOrder = {"General","Application","Transport","Network","Link","Physical"};
    /** Commands organized by section */
    HashMap<String,ArrayList<ConfigCommand>> commands = new HashMap<String,ArrayList<ConfigCommand>>();
    /** All of the known commands */
    ArrayList<ConfigCommand> allCommands = new ArrayList<ConfigCommand>();

    /** Creates an empty CommandSet */
    public CommandSet() {
        for(String s:sectionOrder) {
            commands.put(s, new ArrayList<ConfigCommand>());
        }
    }

    /** Adds a ConfigCommand instance
     *
     * @param c new command to add
     */
    public void add(ConfigCommand c) {
        commands.get(c.commandSection()).add(c);
        allCommands.add(c);
    }

    /** removes a ConfigCommand instance
     *
     * @param c command to remove
     */
    public void remove(ConfigCommand c) {
        commands.get(c.commandSection()).remove(c);
        allCommands.remove(c);
    }

    /**
     * Finds and executes a command that can run the input
     * @param inp String representing what should be done
     */
    public void process(String inp) {
        for(ConfigCommand c:allCommands) {
            if(c.matches(inp)) {
                c.execute(inp);
                return;
            }
        }
        Logger.log("Could not match command "+inp);
    }

    /** Display the usage messages
     *
     */
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
