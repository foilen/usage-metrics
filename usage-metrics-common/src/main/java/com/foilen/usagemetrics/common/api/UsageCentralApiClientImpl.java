/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.common.api;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.common.api.form.AbstractAuthApiBase;
import com.foilen.usagemetrics.common.api.form.ResourcesAddForm;
import com.foilen.usagemetrics.common.api.model.UsageResource;

public class UsageCentralApiClientImpl extends AbstractBasics {

    private String centralUri;
    private String hostname;
    private String hostnameKey;

    private RestTemplate restTemplate = new RestTemplate();

    public UsageCentralApiClientImpl(String centralUri, String hostname, String hostnameKey) {
        this.centralUri = centralUri;
        this.hostname = hostname;
        this.hostnameKey = hostnameKey;

        if (this.centralUri.endsWith("/")) {
            this.centralUri = this.centralUri.substring(0, this.centralUri.length() - 1);
        }
    }

    private <F extends AbstractAuthApiBase> F addAuth(F auth) {
        auth.setHostname(hostname);
        auth.setHostnameKey(hostnameKey);
        return auth;
    }

    public FormResult resourceAdd(List<UsageResource> usageResources) {
        ResourcesAddForm form = addAuth(new ResourcesAddForm()) //
                .setUsageResources(usageResources);
        return restTemplate.postForObject(centralUri + "/resource/", form, FormResult.class);
    }

}
