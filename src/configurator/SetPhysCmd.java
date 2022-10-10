package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigCommand to set the Port to instantiate
 */
public class SetPhysCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*set-physical\\s+([\\w\\.]+)\\s*$");

    public SetPhysCmd() {
        super(PHY);
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
        SimConfig.getConfig().setDefaultPhysLayer(m.group(1));
    }

    @Override
    public String toString() {
        return "set-physical <PortClass>";
    }

    @Override
    public String helpString() {
        return this.toString();
    }
}
