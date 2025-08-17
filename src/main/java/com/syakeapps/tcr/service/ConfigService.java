package com.syakeapps.tcr.service;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * ConfigService is responsible for loading and managing configuration
 * settings for the Twitch Chat Relay application using environment variables.
 * It uses the Dotenv library to load environment variables from a .env file
 * or system environment variables.
 */
public class ConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);
    private static Dotenv env;

    /**
     * Configures the application by loading environment variables.
     * This method should be called before any other methods to ensure
     * that the configuration is available.
     *
     * @return this ConfigService instance for method chaining
     */
    public ConfigService configure() {
        if (env == null) {
            env = Dotenv.configure().ignoreIfMissing().load();
            LOG.debug("Environment variables loaded.");
            if (LOG.isTraceEnabled()) {
                env.entries().forEach(e -> LOG.trace("{}: {}", e.getKey(), e.getValue()));
            }
        }

        return this;
    }

    /**
     * Loads the properties from the environment variables.
     * This method should be called after configure() to ensure
     * that the environment variables are available.
     *
     * @return a Properties object containing the environment variables
     */
    public Properties loadProperties() {
        requiredConfigured();

        Properties properties = new Properties();
        env.entries().forEach(e -> properties.setProperty(e.getKey(), e.getValue()));

        return properties;
    }

    /**
     * Retrieves the value of the specified environment variable.
     * If the variable is not set, null is returned.
     *
     * @param key the name of the environment variable
     * @return the value of the environment variable
     */
    public String get(String key) {
        requiredConfigured();

        return env.get(key);
    }

    /**
     * Retrieves the value of the specified environment variable.
     * If the variable is not set, the provided default value is returned.
     *
     * @param key          the name of the environment variable
     * @param defaultValue the default value to return if the variable is not set
     * @return the value of the environment variable or the default value
     */
    public String get(String key, String defaultValue) {
        requiredConfigured();

        return env.get(key, defaultValue);
    }

    private void requiredConfigured() {
        if (env == null) {
            throw new IllegalStateException("ConfigService is not configured. Call configure() first.");
        }
    }
}