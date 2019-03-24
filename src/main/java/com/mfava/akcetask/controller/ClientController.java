package com.mfava.akcetask.controller;

import com.mfava.akcetask.bo.ClientBo;
import com.mfava.akcetask.request.ClientRequest;
import com.mfava.akcetask.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ClientController {

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    ClientService clientService;

    @PostMapping(value = "/v1/clients")
    public Resource<ClientBo> addNewClient(@RequestBody ClientRequest request) {
        ClientBo client = clientService.addNewClient(request);
        return new Resource<>(client,getClientLink(client.getClientId()));
    }

    @GetMapping(value = "/v1/clients")
    public Resources<ClientBo> getClients() {
        return new Resources<>(clientService.getClients().stream()
                .peek(clientBo -> clientBo.add(getClientLink(clientBo.getClientId())))
                .collect(Collectors.toList()), getAllClientsLink());
    }

    @GetMapping(value = "/v1/clients/{id}")
    public Resource<ClientBo> getClientDetailsById(@PathVariable Long id) {
        ClientBo client = clientService.getClientById(id);
        return new Resource<>(client,getClientLink(client.getClientId()));
    }


    private Link getClientLink(Long clientId) {
        Link selfLink = null;
        try {
            selfLink = ControllerLinkBuilder.linkTo(ClientController.class.getMethod("getClientDetailsById", Long.class), clientId).withSelfRel();
        } catch (NoSuchMethodException e) {
            log.warn("Could not generate client Link  - {}",e.getMessage());
        }
        return selfLink;
    }

    private Link getAllClientsLink() {
        Link selfLink = null;
        try {
            selfLink = ControllerLinkBuilder.linkTo(ClientController.class.getMethod("getClients"),new HashMap<String,Object>()).withSelfRel();
        } catch (NoSuchMethodException e) {
            log.warn("Could not generate AllClients Link  - {}",e.getMessage());
        }
        return selfLink;
    }
}
