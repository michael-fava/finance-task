package com.mfava.financetask.service;

import com.mfava.financetask.bo.ClientBo;
import com.mfava.financetask.request.ClientRequest;

import java.util.List;

public interface ClientService {

    ClientBo addNewClient(ClientRequest request);

    List<ClientBo> getClients();

    ClientBo getClientById(Long id);


}
