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

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.agent.AgentApp;
import com.foilen.usagemetrics.agent.services.DockerUtils;
import com.foilen.usagemetrics.agent.services.DockerUtilsImpl;
import com.foilen.usagemetrics.api.model.UsageResource;

public class DockerGrabber extends AbstractBasics implements Grabber {

    private static final String RESOURCE_TYPE = "dockerContainer";

    private DockerUtils dockerUtils = new DockerUtilsImpl();

    private int lastProcFilesCount;

    @Override
    public List<UsageResource> grab() {
        List<UsageResource> usageResources = new ArrayList<>();

        // Check docker containers
        dockerUtils.containerPsFindAll().forEach(container -> {
            UsageResource usageResource = new UsageResource() //
                    .setUsageResourceType(RESOURCE_TYPE) //
                    .setOwner(container.getName()) //
                    .setDetails(container.getName()) //
                    .setSize(container.getTotalSize());

            usageResources.add(usageResource);
        });

        return usageResources;
    }

    @Override
    public boolean shouldRun() {
        String procFs = AgentApp.getAgentConfig().getDiskSpaceRootFs() + "/proc";
        int procFilesCount = new File(procFs).list().length;
        if (Math.abs(lastProcFilesCount - procFilesCount) > 5) {
            lastProcFilesCount = procFilesCount;
            return true;
        }

        return false;
    }
}
