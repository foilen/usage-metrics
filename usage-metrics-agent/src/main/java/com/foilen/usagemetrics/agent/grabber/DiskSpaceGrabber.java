/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.grabber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.DirectoryTools;
import com.foilen.usagemetrics.agent.AgentApp;
import com.foilen.usagemetrics.agent.services.UnixUsersAndGroupsUtils;
import com.foilen.usagemetrics.agent.services.UnixUsersAndGroupsUtilsImpl;
import com.foilen.usagemetrics.common.api.model.UsageResource;
import com.google.common.util.concurrent.RateLimiter;

public class DiskSpaceGrabber extends AbstractBasics implements Grabber {

    private static final String RESOURCE_TYPE = "localDisk";

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
                    RateLimiter progressRateLimiter = RateLimiter.create(1.0 / 5.0);
                    progressRateLimiter.tryAcquire();

                    UsageResource usageResource = new UsageResource() //
                            .setUsageResourceType(RESOURCE_TYPE) //
                            .setOwner(owner) //
                            .setDetails(user.getHomeFolder());
                    // TODO Use "du"
                    AtomicLong total = new AtomicLong();
                    DirectoryTools.visitFilesAndFoldersRecursively(home, file -> {
                        if (file.isFile()) {
                            total.addAndGet(file.length());
                        }

                        if (progressRateLimiter.tryAcquire()) {
                            logger.debug("... {}", total.get());
                        }

                    });
                    logger.debug("Space for owner {} with home {} is {}", owner, home, total.get());

                    usageResource.setSize(total.get());
                    usageResources.add(usageResource);

                });

        return usageResources;
    }

    @Override
    public boolean shouldRun() {
        return lastGradDiskSpaceInBytes + 1_000_000_000 < calculateRootDisksSpace();
    }

}
