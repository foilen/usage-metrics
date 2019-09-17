/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent;

import com.foilen.smalltools.tools.AbstractBasics;

public class DatabaseInfo extends AbstractBasics {

    private String host;
    private int port = 3306;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    public DatabaseInfo() {
    }

    public DatabaseInfo(String host, int port, String dbName, String dbUser, String dbPassword) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public DatabaseInfo setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public DatabaseInfo setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
        return this;
    }

    public DatabaseInfo setDbUser(String dbUser) {
        this.dbUser = dbUser;
        return this;
    }

    public DatabaseInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public DatabaseInfo setPort(int port) {
        this.port = port;
        return this;
    }

}
