package com.project.app.exception;

public class InvalidStrategyException extends RuntimeException {
    /**
     * thrown when invalid strategy is provided.
     *
     * @param message message
     */
    public InvalidStrategyException(String message) {
        super(message);
    }
}
