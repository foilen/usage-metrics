/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.foilen.smalltools.restapi.spring.MvcJsonSpringConfig;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.JsonTools;

@SpringBootApplication
public class CentralApp extends AbstractBasics {

    private static final Logger logger = LoggerFactory.getLogger(CentralApp.class);

    private static CentralConfig centralConfig = new CentralConfig();

    public static CentralConfig getCentralConfig() {
        return centralConfig;
    }

    public static void main(String[] args) {

        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        List<String> springArguments = new ArrayList<>();

        if (arguments.remove("--debug")) {
            springArguments.add("--debug");
        }

        String configFileName = null;
        if (arguments.size() == 1) {
            configFileName = args[0];
        }
        if (arguments.size() > 1) {
            System.err.println("Usage: [configFileName]");
            System.err.println("eg: config.json");
            System.exit(1);
        }

        // Load the config file
        if (configFileName == null) {
            logger.info("[CONFIG] No config file specified. Using default values");
        } else {
            logger.info("[CONFIG] Load config file {}", configFileName);
            try {
                centralConfig = JsonTools.readFromFile(configFileName, CentralConfig.class);
            } catch (Exception e) {
                logger.error("Could not load the config file {}", configFileName, e);
                System.exit(1);
            }

        }

        // Start the application
        System.setProperty("spring.data.mongodb.uri", centralConfig.getMongoUri());
        SpringApplication application = new SpringApplication( //
                CentralApp.class, //
                MvcJsonSpringConfig.class //
        );
        application.run(springArguments.toArray(new String[springArguments.size()]));
    }

}
