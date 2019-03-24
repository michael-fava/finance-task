package com.mfava.akcetask.service;

import com.mfava.akcetask.bo.ClientBo;
import com.mfava.akcetask.request.ClientRequest;

import java.util.List;

public interface ClientService {

    ClientBo addNewClient(ClientRequest request);

    List<ClientBo> getClients();

    ClientBo getClientById(Long id);


}
