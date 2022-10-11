package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogTargetCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*log-target\\s+(\\w+)\\s*$");

    public LogTargetCmd() {
        super(GEN);
    }
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
        String path = m.group(1);
        try {
            Logger.routeToFile(path);
            System.out.println("log redirection success");
        } catch (FileNotFoundException e) {
            System.out.println("log redirection failure");
        }
    }

    @Override
    public String toString() {
        return "log-target <path>";
    }

    @Override
    public String helpString() {
        return toString()+"\n  configures the logger to log the file <path>";
    }
}
