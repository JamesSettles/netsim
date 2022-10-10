package configurator;

import configurator.commands.ConfigCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private SimConfig conf;
    private boolean running;

    public Main() {
        conf = SimConfig.getConfig();
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.run();
    }

    public void quit() {
        running = false;
    }

    private void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        running = true;
        ConfigCommand.registerCommand(new QuitCmd(this));
        ConfigCommand.showCommands();
        while(running) {
            System.out.print("cmd > ");
            try {
                ConfigCommand.process(br.readLine());
            } catch (IOException e) {
                Logger.log("Input stream is broken! Exiting...");
                quit();
                continue;
            }
        }
    }
}
