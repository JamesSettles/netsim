package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigCommand to set the Transport to instantiate
 */
public class SetTransCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*set-transport\\s+([\\w\\.]+)\\s*$");

    public SetTransCmd() {
        super(TRN);
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
        SimConfig.getConfig().setDefaultTransportLayer(m.group(1));
    }

    @Override
    public String toString() {
        return "set-transport <TransportClass>";
    }

    @Override
    public String helpString() {
        return this.toString();
    }
}
