/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.grabber;

import java.util.ArrayList;
import java.util.List;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.api.model.UsageResource;

// TODO JamesGrabber
public class JamesGrabber extends AbstractBasics implements Grabber {

    @Override
    public List<UsageResource> grab() {
        List<UsageResource> usageResources = new ArrayList<>();
        return usageResources;
    }

    @Override
    public boolean shouldRun() {
        return false;
    }

}
