package configurator;

import configurator.commands.ConfigCommand;
import exceptions.BadCommandRouting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigCommand to change the Link to be instantiated
 */
public class SetLinkCmd extends ConfigCommand {
    private static Pattern ptrn = Pattern.compile("^\\s*set-link\\s+([\\w\\.]+)\\s*$");

    public SetLinkCmd() {
        super(LNK);
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
        SimConfig.getConfig().setDefaultLinkLayer(m.group(1));
    }

    @Override
    public String toString() {
        return "set-link <LinkClass>";
    }

    @Override
    public String helpString() {
        return this.toString();
    }
}
