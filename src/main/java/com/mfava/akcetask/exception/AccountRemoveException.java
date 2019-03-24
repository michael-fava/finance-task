package com.mfava.akcetask.exception;

import lombok.Data;

@Data
public class AccountRemoveException extends RuntimeException {
    public AccountRemoveException(String message) {
        super(message);
    }
}
