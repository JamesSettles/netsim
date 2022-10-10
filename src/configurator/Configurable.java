package configurator;

public interface Configurable {
    /**
     * Arbitrary part configuration
     * @param configOptions how the instance should change configuration
     */
    public void doConfiguration(String[] configOptions);
}
