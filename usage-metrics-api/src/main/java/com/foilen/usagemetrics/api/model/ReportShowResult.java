/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.model;

import java.util.Map;
import java.util.TreeMap;

import com.foilen.smalltools.restapi.model.AbstractApiBaseWithError;

public class ReportShowResult extends AbstractApiBaseWithError {

    private Map<String, ReportForOwner> reportByOwner = new TreeMap<>();

    public Map<String, ReportForOwner> getReportByOwner() {
        return reportByOwner;
    }

    public ReportShowResult setReportByOwner(Map<String, ReportForOwner> reportByOwner) {
        this.reportByOwner = reportByOwner;
        return this;
    }

}
