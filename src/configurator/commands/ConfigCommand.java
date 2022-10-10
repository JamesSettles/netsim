package configurator.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Encaspulates a command for the simulator and static methods for working with the current set of
 * commands. (In retrospect these concerns should not have been mixed)
 */
public abstract class ConfigCommand {
    private static final CommandSet commandSet = new CommandSet();

    /**
     * Registers a new command
     * @param c ConfigCommand to register
     */
    public static void registerCommand(ConfigCommand c) {
        commandSet.add(c);
    }

    /**
     * Unregisters a command
     * @param c ConfigCommand to deregister
     */
    public static void unregisterCommand(ConfigCommand c) {
        commandSet.remove(c);
    }

    /**
     * Processes a string as a command
     * @param inp command to execute
     */
    public static void process(String inp) {
        if(inp.matches("^\\s*(help)|(\\?)\\s*$")) {
            showCommands();
            return;
        }
        commandSet.process(inp);
    }

    /**
     * Display the usage for all known commands
     */
    public static void showCommands() {
        commandSet.usage();
    }

    /** returns the section this ConfigCommand should be displayed in
     *
     * @return section to display in
     */
    public String commandSection() {
        return "General";
    }

    /**
     * Checks if this command matches a line enterred in the interface
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
