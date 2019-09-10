/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.form;

import java.util.Date;

public class ReportShowForm extends AbstractAuthApiBase {

    private Date forDate = new Date();

    public Date getForDate() {
        return forDate;
    }

    public ReportShowForm setForDate(Date forDate) {
        this.forDate = forDate;
        return this;
    }

}
