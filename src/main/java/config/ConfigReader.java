package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String PROPERTIES_FILE = "config.properties";
    private static final String SECRET_PROPERTIES_FILE = "secret.properties";
    private final Properties properties;

    public ConfigReader() {
        properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Failed to load properties file: " + PROPERTIES_FILE);
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(SECRET_PROPERTIES_FILE)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Failed to load secret properties file: " + PROPERTIES_FILE);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getOauth2Token() {
        return getProperty("PRIVATE_ACCESS_TOKEN");
    }
}
