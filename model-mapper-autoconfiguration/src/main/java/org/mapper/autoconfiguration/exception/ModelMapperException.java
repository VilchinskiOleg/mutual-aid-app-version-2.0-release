package org.mapper.autoconfiguration.exception;

public class ModelMapperException extends RuntimeException{

    public ModelMapperException() {
    }

    public ModelMapperException(String message) {
        super(message);
    }

    public ModelMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelMapperException(Throwable cause) {
        super(cause);
    }

    public ModelMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
