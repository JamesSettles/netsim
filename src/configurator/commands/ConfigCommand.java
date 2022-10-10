package configurator.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Encaspulates a command for the simulator and static methods for working with the current set of
 * commands. (In retrospect these concerns should not have been mixed)
 */
public abstract class ConfigCommand {
    private static final CommandSet commandSet = new CommandSet();

    public static void registerCommand(ConfigCommand c) {
        commandSet.add(c);
    }
    public static void unregisterCommand(ConfigCommand c) {
        commandSet.remove(c);
    }
    public static void process(String inp) {
        if(inp.matches("^\\s*(help)|(\\?)\\s*$")) {
            showCommands();
            return;
        }
        commandSet.process(inp);
    }
    public static void showCommands() {
        commandSet.usage();
    }

    public String commandSection() {
        return "General";
    }

    /**
     * Checks if this command matches a line entered in the interface
     * @param inp raw command text
     * @return true if this command can execute this strng
     */
    public abstract boolean matches(String inp);

    /**
     * Executes this command
     */
    public abstract void execute(String inp);


    /**
     * One line command description
     * @return command template
     */
    public abstract String toString();

    /**
     * More verbose help message
     * @return help message text
     */
    public abstract String helpString();
}
