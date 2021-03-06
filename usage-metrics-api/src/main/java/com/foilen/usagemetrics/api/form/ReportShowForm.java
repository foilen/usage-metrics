/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.form;

import java.util.Date;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class ReportShowForm extends AbstractApiBase {

    private Date forDate = new Date();

    public Date getForDate() {
        return forDate;
    }

    public ReportShowForm setForDate(Date forDate) {
        this.forDate = forDate;
        return this;
    }

}
