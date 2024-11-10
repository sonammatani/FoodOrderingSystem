package com.project.app.exception;

public class UndeliverableOrderException extends Exception {

    /**
     * thrown when the order cannot be delivered.
     *
     * @param message message
     */
    public UndeliverableOrderException(String message) {
        super(message);
    }
}

