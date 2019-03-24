package com.mfava.akcetask.exception;

import com.mfava.akcetask.request.ClientRequest;
import lombok.Getter;

public class ClientCreationException extends RuntimeException {

    @Getter
    private final ClientRequest request;

    public ClientCreationException(ClientRequest request) {
        this.request = request;
    }
}
