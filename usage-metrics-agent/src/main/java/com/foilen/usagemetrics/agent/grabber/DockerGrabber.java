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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.FileTools;
import com.foilen.usagemetrics.agent.AgentApp;
import com.foilen.usagemetrics.agent.services.DockerUtils;
import com.foilen.usagemetrics.agent.services.DockerUtilsImpl;
import com.foilen.usagemetrics.common.api.model.UsageResource;

public class DockerGrabber extends AbstractBasics implements Grabber {

    private static final String RESOURCE_TYPE = "dockerContainer";
    private static final String UNKNOWN_OWNER = "UNKNOWN_OWNER";

    private DockerUtils dockerUtils = new DockerUtilsImpl();

    private int lastProcFilesCount;

    @Override
    public List<UsageResource> grab() {
        List<UsageResource> usageResources = new ArrayList<>();

        // Get containers ownership
        String dockerSudoDirectory = AgentApp.getAgentConfig().getDiskSpaceRootFs() + "/etc/docker-sudo/";
        Map<String, String> ownerByContainer = new HashMap<>();
        for (File file : new File(dockerSudoDirectory).listFiles()) {
            if (file.getName().startsWith("containers-") && file.getName().endsWith(".conf")) {
                int start = "containers-".length();
                int end = file.getName().length() - ".conf".length();
                String owner = file.getName().substring(start, end);
                FileTools.readFileLinesStream(file).forEach(container -> ownerByContainer.put(container, owner));
            }
        }

        // Check docker containers
        dockerUtils.containerPsFindAll().forEach(container -> {
            String owner = ownerByContainer.get(container.getName());
            if (owner == null) {
                owner = UNKNOWN_OWNER;
            }
            UsageResource usageResource = new UsageResource() //
                    .setUsageResourceType(RESOURCE_TYPE) //
                    .setOwner(owner) //
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
