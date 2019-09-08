/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.foilen.smalltools.consolerunner.ConsoleRunner;
import com.foilen.smalltools.iterable.FileLinesIterable;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.SpaceConverterTool;
import com.foilen.usagemetrics.agent.services.model.DockerPs;
import com.foilen.usagemetrics.agent.services.model.DockerPsStatus;

public class DockerUtilsImpl extends AbstractBasics implements DockerUtils {

    private static final ThreadLocal<SimpleDateFormat> createdAtSdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        }
    };

    @Override
    public List<DockerPs> containerPsFindAll() {

        // Get details
        ConsoleRunner consoleRunner = new ConsoleRunner();
        consoleRunner.setCommand("/usr/bin/docker");
        consoleRunner.addArguments("ps", "-a", "--no-trunc", "-s", "--format", "{{.ID}}\t{{.Names}}\t{{.CreatedAt}}\t{{.RunningFor}}\t{{.Status}}\t{{.Size}}");
        String output = consoleRunner.executeForString();
        logger.debug("Docker ps output: {}", output);

        List<DockerPs> containers = convertToDockerPs(output);

        // Sort
        Collections.sort(containers, (a, b) -> a.getName().compareTo(b.getName()));

        return containers;
    }

    protected List<DockerPs> convertToDockerPs(String output) {
        List<DockerPs> results = new ArrayList<>();
        FileLinesIterable fileLinesIterable = new FileLinesIterable();
        fileLinesIterable.openString(output);
        for (String line : fileLinesIterable) {
            String[] parts = line.split("\t");
            if (parts.length < 6) {
                continue;
            }
            DockerPs dockerPs = new DockerPs();
            int i = 0;
            dockerPs.setId(parts[i++]);
            dockerPs.setName(parts[i++].split(",", 2)[0]);
            try {
                dockerPs.setCreatedAt(createdAtSdf.get().parse(parts[i++]));
            } catch (ParseException e) {
            }
            dockerPs.setRunningFor(parts[i++]);
            String fullStatus = parts[i++];
            int spacePos = fullStatus.indexOf(' ');
            if (spacePos == -1) {
                spacePos = fullStatus.length();
            }
            dockerPs.setStatus(DockerPsStatus.valueOf(fullStatus.substring(0, spacePos)));
            String sizePart = parts[i++];
            String[] sizeParts = sizePart.split(" \\(virtual ");

            String instanceSizePart = sizeParts[0];
            try {
                long instanceSize = SpaceConverterTool.convertToBytes(instanceSizePart);
                dockerPs.setSize(instanceSize);
            } catch (Exception e) {
                logger.warn("Could not convert {}", instanceSizePart, e);
            }

            String totalSizePart = sizeParts[1].split("\\)")[0];
            try {
                long totalSize = SpaceConverterTool.convertToBytes(totalSizePart);
                dockerPs.setTotalSize(totalSize);
            } catch (Exception e) {
                logger.warn("Could not convert {}", totalSizePart, e);
            }

            results.add(dockerPs);
        }

        return results;
    }

}
