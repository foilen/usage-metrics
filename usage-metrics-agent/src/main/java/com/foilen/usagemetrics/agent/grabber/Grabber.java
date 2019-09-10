/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.grabber;

import java.util.List;

import com.foilen.usagemetrics.api.model.UsageResource;

/**
 * A usage grabber.
 */
public interface Grabber {

    List<UsageResource> grab();

    /**
     * Make very light checks to see if it is worth it to execute the full test now.
     *
     * @return
     */
    boolean shouldRun();

}
