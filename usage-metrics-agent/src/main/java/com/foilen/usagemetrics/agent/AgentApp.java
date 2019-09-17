/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.foilen.smalltools.restapi.model.AbstractApiBaseWithError;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.smalltools.tools.SecureRandomTools;
import com.foilen.smalltools.tools.ThreadNameStateTool;
import com.foilen.smalltools.tools.ThreadTools;
import com.foilen.usagemetrics.agent.dao.UsageResourcesToSendDao;
import com.foilen.usagemetrics.agent.grabber.DiskSpaceGrabber;
import com.foilen.usagemetrics.agent.grabber.DockerGrabber;
import com.foilen.usagemetrics.agent.grabber.Grabber;
import com.foilen.usagemetrics.agent.grabber.JamesGrabber;
import com.foilen.usagemetrics.api.UsageCentralApiClient;
import com.foilen.usagemetrics.api.UsageCentralApiClientImpl;
import com.foilen.usagemetrics.api.model.UsageResource;

public class AgentApp extends AbstractBasics {

    private static AgentConfig agentConfig = new AgentConfig();

    public static AgentConfig getAgentConfig() {
        return agentConfig;
    }

    public static void main(String[] args) {

        String configFileName = null;
        if (args.length == 1) {
            configFileName = args[0];
        }
        if (args.length > 1) {
            System.err.println("Usage: [configFileName]");
            System.err.println("eg: config.json");
            System.exit(1);
        }
        new AgentApp().execute(configFileName);

    }

    private void execute(String configFileName) {

        // Load the config file
        if (configFileName == null) {
            logger.info("[CONFIG] No config file specified. Using default values");
        } else {
            logger.info("[CONFIG] Load config file {}", configFileName);
            try {
                agentConfig = JsonTools.readFromFile(configFileName, AgentConfig.class);
            } catch (Exception e) {
                logger.error("Could not load the config file {}", configFileName, e);
                System.exit(1);
            }

        }

        UsageResourcesToSendDao usageResourcesToSendDao = new UsageResourcesToSendDao(new File("_usageResourcesToSend.json"));
        UsageCentralApiClient usageCentralApiClient = new UsageCentralApiClientImpl(agentConfig.getCentralUri(), agentConfig.getHostname(), agentConfig.getHostnameKey());

        // Start the grabbers
        List<Grabber> grabbers = new ArrayList<>();
        grabbers.add(new DiskSpaceGrabber());
        grabbers.add(new DockerGrabber());
        agentConfig.getJamesDatabases().forEach(db -> grabbers.add(new JamesGrabber(db)));

        boolean shouldRun = true;
        long lastRun = 0;

        while (true) {

            // Check if it makes an hour since last run
            shouldRun |= lastRun + 60 * 60000 < System.currentTimeMillis();

            // Check if one grabber thinks there were enough changes to execute now
            shouldRun |= grabbers.stream().anyMatch(g -> g.shouldRun());
            logger.info("Should run? {}", shouldRun);
            if (shouldRun) {

                // Run all
                for (Grabber grabber : grabbers) {

                    String txId = SecureRandomTools.randomBase64String(5);

                    ThreadNameStateTool threadNameStateTool = ThreadTools.nameThread() //
                            .clear().setSeparator("-") //
                            .appendObjectClassSimple(grabber) //
                            .appendDate(new Date()) //
                            .appendText(txId) //
                            .change();
                    try {
                        logger.info("Execute");
                        List<UsageResource> usageResources = grabber.grab();

                        // Store it in the sending queue
                        usageResources.forEach(ur -> {
                            ur.setBatchId(txId);
                            logger.info("Got {}", ur);
                        });
                        usageResourcesToSendDao.loadInTransaction(l -> l.getUsageResources().addAll(usageResources));
                    } catch (Exception e) {
                        logger.error("Got an unexpected error in the loop", e);
                    } finally {
                        threadNameStateTool.revert();
                    }
                }

                lastRun = System.currentTimeMillis();
            }

            // Send any pending metrics
            usageResourcesToSendDao.loadInTransaction(l -> {
                if (l.getUsageResources().isEmpty()) {
                    logger.info("No usage to send");
                    return;
                }

                logger.info("Got {} usage to send", l.getUsageResources().size());
                try {
                    AbstractApiBaseWithError result = usageCentralApiClient.resourceAdd(l.getUsageResources());
                    if (result.isSuccess()) {
                        logger.info("Sending usage successfully");
                        l.getUsageResources().clear();
                    } else {
                        logger.error("Got an error when sending usage: {}", result.getError());
                    }
                } catch (Exception e) {
                    logger.error("Problem sending usage", e);
                }

            });

            // Done
            shouldRun = false;

            // Wait
            logger.info("Wait a minute");
            ThreadTools.sleep(60000);
        }

    }

}
