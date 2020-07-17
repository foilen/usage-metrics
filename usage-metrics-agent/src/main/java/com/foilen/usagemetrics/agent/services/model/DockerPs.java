/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.services.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foilen.smalltools.tools.AbstractBasics;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DockerPs extends AbstractBasics {

    private String id;
    private String name;
    private Date createdAt;
    private String runningFor;
    private DockerPsStatus status;
    private long size;
    private long totalSize;

    public DockerPs() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRunningFor() {
        return runningFor;
    }

    public long getSize() {
        return size;
    }

    public DockerPsStatus getStatus() {
        return status;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRunningFor(String runningFor) {
        this.runningFor = runningFor;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setStatus(DockerPsStatus status) {
        this.status = status;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

}
