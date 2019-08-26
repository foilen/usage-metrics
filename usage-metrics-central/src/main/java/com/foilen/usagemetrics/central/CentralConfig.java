/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central;

import com.foilen.smalltools.tools.AbstractBasics;

public class CentralConfig extends AbstractBasics {

    private String hostKeySalt;

    private String mongoUri = "mongodb://localhost:27017/usage";

    public String getHostKeySalt() {
        return hostKeySalt;
    }

    public String getMongoUri() {
        return mongoUri;
    }

    public void setHostKeySalt(String hostKeySalt) {
        this.hostKeySalt = hostKeySalt;
    }

    public void setMongoUri(String mongoUri) {
        this.mongoUri = mongoUri;
    }

}
