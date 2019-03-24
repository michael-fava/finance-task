package com.mfava.akcetask.exception;

import lombok.Data;

@Data
public class AccountNotFoundException extends RuntimeException {

    private final Long accountId;

    public AccountNotFoundException(Long accountId) {
        this.accountId = accountId;
    }
}
