/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api;

import java.util.Date;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.api.form.AbstractAuthApiBase;
import com.foilen.usagemetrics.api.form.ReportShowForm;
import com.foilen.usagemetrics.api.form.ResourcesAddForm;
import com.foilen.usagemetrics.api.model.ReportShowResult;
import com.foilen.usagemetrics.api.model.UsageResource;

public class UsageCentralApiClientImpl extends AbstractBasics implements UsageCentralApiClient {

    private String centralUri;
    private String authUser;
    private String authKey;

    private RestTemplate restTemplate = new RestTemplate();

    public UsageCentralApiClientImpl(String centralUri, String authUser, String authKey) {
        this.centralUri = centralUri;
        this.authUser = authUser;
        this.authKey = authKey;

        if (this.centralUri.endsWith("/")) {
            this.centralUri = this.centralUri.substring(0, this.centralUri.length() - 1);
        }
    }

    private <F extends AbstractAuthApiBase> F addAuth(F auth) {
        auth.setAuth(authUser, authKey);
        return auth;
    }

    @Override
    public ReportShowResult reportShow(Date forDate) {
        ReportShowForm form = addAuth(new ReportShowForm()) //
                .setForDate(forDate);
        return restTemplate.postForObject(centralUri + "/report/showReport", form, ReportShowResult.class);
    }

    @Override
    public FormResult resourceAdd(List<UsageResource> usageResources) {
        ResourcesAddForm form = addAuth(new ResourcesAddForm()) //
                .setUsageResources(usageResources);
        return restTemplate.postForObject(centralUri + "/resource/", form, FormResult.class);
    }

}
