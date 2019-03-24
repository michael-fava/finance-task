package com.mfava.akcetask.exception;

import com.mfava.akcetask.request.AccountRequest;
import lombok.Data;

@Data
public class AccountCreateException extends RuntimeException {

    private final AccountRequest request;
    private final String message;

    public AccountCreateException(AccountRequest request, String message) {
        this.request = request;
        this.message = message;
    }
}
