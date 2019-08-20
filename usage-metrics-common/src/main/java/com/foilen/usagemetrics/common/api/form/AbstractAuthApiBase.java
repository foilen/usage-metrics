/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.common.api.form;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public abstract class AbstractAuthApiBase extends AbstractApiBase {

    private String hostname;
    private String hostnameKey;

    public String getHostname() {
        return hostname;
    }

    public String getHostnameKey() {
        return hostnameKey;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setHostnameKey(String hostnameKey) {
        this.hostnameKey = hostnameKey;
    }

}
