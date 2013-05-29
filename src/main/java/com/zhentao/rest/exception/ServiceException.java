package com.zhentao.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * A JAX-RS WebApplicationException that resolves to an HTTP response whose content type is text/plain and whose body
 * is a string that you can format the usual way with String.format.
 */
public class ServiceException extends WebApplicationException {

    private static final long serialVersionUID = -2880163255606535397L;

    public ServiceException(Response.Status status, String message, Object... args) {
        super(Response.status(status)
                .header("Content-type", "text/plain")
                .entity(String.format(message, args))
                .build());
    }
}
