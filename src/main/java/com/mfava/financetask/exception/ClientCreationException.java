package com.mfava.financetask.exception;

import com.mfava.financetask.request.ClientRequest;
import lombok.Getter;

public class ClientCreationException extends RuntimeException {

    @Getter
    private final ClientRequest request;

    public ClientCreationException(ClientRequest request) {
        this.request = request;
    }
}
