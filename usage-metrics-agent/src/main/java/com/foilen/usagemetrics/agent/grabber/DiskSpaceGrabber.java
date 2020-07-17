/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.grabber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.foilen.smalltools.consolerunner.ConsoleRunner;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.usagemetrics.agent.AgentApp;
import com.foilen.usagemetrics.agent.services.UnixUsersAndGroupsUtils;
import com.foilen.usagemetrics.agent.services.UnixUsersAndGroupsUtilsImpl;
import com.foilen.usagemetrics.api.model.UsageResource;

public class DiskSpaceGrabber extends AbstractBasics implements Grabber {

    private static final String RESOURCE_TYPE = "localDisk";

    public static long convertdu(String duText) {
        int position = duText.indexOf('\t');
        if (position < 0) {
            position = duText.indexOf(' ');
        }

        return Long.valueOf(duText.substring(0, position));
    }

    private long lastGradDiskSpaceInBytes;

    private long calculateRootDisksSpace() {
        long total = 0;
        for (File root : File.listRoots()) {

            long usedSpace = root.getTotalSpace() - root.getFreeSpace();
            logger.debug("Space for {} is {}", root.getAbsolutePath(), usedSpace);
            total += usedSpace;
        }

        logger.debug("All root space: {}", total);

        return total;
    }

    @Override
    public List<UsageResource> grab() {

        lastGradDiskSpaceInBytes = calculateRootDisksSpace();

        List<UsageResource> usageResources = new ArrayList<>();
        String rootFs = AgentApp.getAgentConfig().getDiskSpaceRootFs();
        UnixUsersAndGroupsUtils unixUsersAndGroupsUtils = new UnixUsersAndGroupsUtilsImpl(rootFs);

        unixUsersAndGroupsUtils.userGetAll().stream() //
                .filter(user -> user.getHomeFolder() != null && new File(rootFs + '/' + user.getHomeFolder()).isDirectory()) //
                .filter(user -> user.getHomeFolder().startsWith("/home/")) //
                .forEach(user -> {
                    String owner = user.getName();
                    String home = rootFs + '/' + user.getHomeFolder();

                    logger.debug("Processing owner {} with home {}", owner, home);

                    UsageResource usageResource = new UsageResource() //
                            .setUsageResourceType(RESOURCE_TYPE) //
                            .setOwner(owner) //
                            .setDetails(user.getHomeFolder());
                    ConsoleRunner consoleRunner = new ConsoleRunner();
                    consoleRunner.setCommand("/usr/bin/du");
                    consoleRunner.addArguments("-B1");
                    consoleRunner.addArguments("-s", home);
                    Tuple2<String, String> out = consoleRunner.executeForStrings();
                    String duText = out.getA();
                    if (consoleRunner.getStatusCode() != 0) {
                        logger.error("Could not retrieve the usage of {} . Got exit code: {}", home, consoleRunner.getStatusCode());
                        return;
                    }

                    logger.debug("Got for owner {} with home {} : {}", owner, home, duText);
                    long total = convertdu(duText);
                    logger.debug("Space for owner {} with home {} is {}", owner, home, total);

                    usageResource.setSize(total);
                    usageResources.add(usageResource);

                });

        return usageResources;
    }

    @Override
    public boolean shouldRun() {
        return lastGradDiskSpaceInBytes + 1_000_000_000 < calculateRootDisksSpace();
    }

}
