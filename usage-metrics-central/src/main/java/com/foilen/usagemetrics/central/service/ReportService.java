/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import java.util.Date;

import com.foilen.usagemetrics.common.api.model.ReportShowResult;

public interface ReportService {

    ReportShowResult getReport(Date forDate);

}
