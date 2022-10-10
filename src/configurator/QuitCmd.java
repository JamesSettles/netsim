package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigCommand to cause NetSim to exit
 */
public class QuitCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*(quit)|(exit)\\s*$");
    private Main prog;

    public QuitCmd(Main m) {
        prog = m;
    }

    @Override
    public boolean matches(String inp) {
        Matcher m = ptrn.matcher(inp);
        return m.matches();
    }

    @Override
    public void execute(String inp) {
        if(!matches(inp)) {
            throw new BadCommandRouting(this.getClass().toString(), inp);
        }
        prog.quit();
    }

    @Override
    public String toString() {
        return "exit";
    }

    @Override
    public String helpString() {
        return toString()+"\n  exits/quits the simulator";
    }
}
