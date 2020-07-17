/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent;

import java.util.ArrayList;
import java.util.List;

import com.foilen.smalltools.JavaEnvironmentValues;
import com.foilen.smalltools.tools.AbstractBasics;

public class AgentConfig extends AbstractBasics {

    private String centralUri = "http://localhost:8080";
    private String hostname = JavaEnvironmentValues.getHostName();
    private String hostnameKey;

    private String diskSpaceRootFs = "/";

    private List<DatabaseInfo> jamesDatabases = new ArrayList<>();

    public String getCentralUri() {
        return centralUri;
    }

    public String getDiskSpaceRootFs() {
        return diskSpaceRootFs;
    }

    public String getHostname() {
        return hostname;
    }

    public String getHostnameKey() {
        return hostnameKey;
    }

    public List<DatabaseInfo> getJamesDatabases() {
        return jamesDatabases;
    }

    public void setCentralUri(String centralUri) {
        this.centralUri = centralUri;
    }

    public void setDiskSpaceRootFs(String diskSpaceRootFs) {
        this.diskSpaceRootFs = diskSpaceRootFs;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setHostnameKey(String hostnameKey) {
        this.hostnameKey = hostnameKey;
    }

    public void setJamesDatabases(List<DatabaseInfo> jamesDatabases) {
        this.jamesDatabases = jamesDatabases;
    }

}
