/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.common;

public class UsageMetricException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsageMetricException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsageMetricException(String message) {
        super(message);
    }

    public UsageMetricException(Throwable cause) {
        super(cause);
    }

}
