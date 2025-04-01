package com.example.teamcity.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton class responsible for loading and providing access to configuration properties
 * from the {@code config.properties} file located in the classpath.
 */
public class Config {
    private static Config config;
    private Properties properties;
    private static final String CONFIG_PROPERTIES = "config.properties";


    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES);
    }

    /**
     * Returns the singleton instance of the Config class.
     *
     * @return singleton Config instance
     */
    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }


    public static String getProperty(String key) {
        return getConfig().properties.getProperty(key);
    }

    private void loadProperties(String filename) {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(filename)) {
            if (stream == null) {
                System.err.println("File not found: " + filename);
                return;
            }
            properties.load(stream);
        } catch (IOException e) {
            System.err.println("Error while reading file " + filename + e);
            throw new RuntimeException(e);
        }
    }
}
