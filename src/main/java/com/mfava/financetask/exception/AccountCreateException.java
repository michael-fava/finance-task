package com.mfava.financetask.exception;

import com.mfava.financetask.request.AccountRequest;
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
