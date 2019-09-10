/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.form;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public abstract class AbstractAuthApiBase extends AbstractApiBase {

    private String authUser;
    private String authKey;

    public String getAuthKey() {
        return authKey;
    }

    public String getAuthUser() {
        return authUser;
    }

    public void setAuth(String authUser, String authKey) {
        this.authUser = authUser;
        this.authKey = authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

}
