/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent;

import com.foilen.smalltools.tools.AbstractBasics;

public class AgentConfig extends AbstractBasics {

    private String diskSpaceRootFs = "/";

    public String getDiskSpaceRootFs() {
        return diskSpaceRootFs;
    }

    public void setDiskSpaceRootFs(String diskSpaceRootFs) {
        this.diskSpaceRootFs = diskSpaceRootFs;
    }

}
