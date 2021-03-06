/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.api.form.ReportShowForm;
import com.foilen.usagemetrics.api.model.ReportShowResult;
import com.foilen.usagemetrics.central.security.ApiUsersUserDetailsService;
import com.foilen.usagemetrics.central.service.ReportService;

@RestController
@RequestMapping("report")
@Secured(ApiUsersUserDetailsService.ROLE_API_USER)
public class ReportController extends AbstractBasics {

    @Autowired
    private ReportService reportService;

    @PostMapping("/showReport")
    public ReportShowResult showReport(@RequestBody ReportShowForm form) {
        return reportService.getReport(form.getForDate());
    }

}
