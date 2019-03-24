package com.mfava.financetask.exception;

import lombok.Getter;

public class ClientNotFoundException extends RuntimeException {

    @Getter
    private final Long clientId;

    public ClientNotFoundException(Long clientId) {
        super("Client with ID - "+clientId+" not found");
        this.clientId = clientId;
    }
}

