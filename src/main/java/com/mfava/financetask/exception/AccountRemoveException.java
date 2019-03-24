package com.mfava.financetask.exception;

import lombok.Data;

@Data
public class AccountRemoveException extends RuntimeException {
    public AccountRemoveException(String message) {
        super(message);
    }
}
