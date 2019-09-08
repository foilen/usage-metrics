/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ApiUser {

    @Id
    private String authUser;
    private String authPasswordHash;

    private String comment;

    public String getAuthPasswordHash() {
        return authPasswordHash;
    }

    public String getAuthUser() {
        return authUser;
    }

    public String getComment() {
        return comment;
    }

    public ApiUser setAuthPasswordHash(String authPasswordHash) {
        this.authPasswordHash = authPasswordHash;
        return this;
    }

    public ApiUser setAuthUser(String authUser) {
        this.authUser = authUser;
        return this;
    }

    public ApiUser setComment(String comment) {
        this.comment = comment;
        return this;
    }

}
