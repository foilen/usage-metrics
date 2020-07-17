/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.grabber;

import org.junit.Assert;
import org.junit.Test;

public class DiskSpaceGrabberTest {

    @Test
    public void test() {
        Assert.assertEquals(17816L, DiskSpaceGrabber.convertdu("17816   usage-metrics-agent"));
        Assert.assertEquals(28L, DiskSpaceGrabber.convertdu("28   /home/rslsync/"));
    }

}
