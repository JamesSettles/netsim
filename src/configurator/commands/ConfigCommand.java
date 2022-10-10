package configurator.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Encaspulates a command for the simulator and static methods for working with the current set of
 * commands. (In retrospect these concerns should not have been mixed)
 */
public abstract class ConfigCommand {
    public static final String APP = "Application";
    public static final String TRN = "Transport";
    public static final String NET = "Network";
    public static final String LNK = "Link";
    public static final String PHY = "Physical";
    public static final String GEN = "General";
    private final String cmd_section;

    public ConfigCommand() {
        cmd_section = GEN;
    }
    public ConfigCommand(String s) {
        cmd_section = s;
    }

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
    public final String commandSection() {
        return cmd_section;
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
