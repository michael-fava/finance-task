package com.mfava.financetask.service.impl;

import com.mfava.financetask.bo.ClientBo;
import com.mfava.financetask.exception.AccountCreateException;
import com.mfava.financetask.exception.ClientCreationException;
import com.mfava.financetask.exception.ClientNotFoundException;
import com.mfava.financetask.model.*;
import com.mfava.financetask.repo.AccountRepository;
import com.mfava.financetask.repo.AddressRepository;
import com.mfava.financetask.repo.ClientRepository;
import com.mfava.financetask.request.AccountRequest;
import com.mfava.financetask.request.ClientRequest;
import com.mfava.financetask.service.ClientService;
import com.mfava.financetask.utils.CustomObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository, AccountRepository accountRepository, CustomObjectMapper mapper) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    private ClientRepository clientRepository;

    private AddressRepository addressRepository;

    private AccountRepository accountRepository;

    private CustomObjectMapper mapper;


    @Transactional
    @Override
    public ClientBo addNewClient(ClientRequest request) {
        Client client;

        if (validateClientRequest(request)) {
            Account account = accountRepository.save(Account.builder()
                    .accountType(mapper.map(request.getAccount().getAccountType(), AccountType.class))
                    .balance(0.0)
                    .balanceStatus(BalanceStatus.DR)
                    .build());

            if (account != null) {
                List<Account> accounts = new ArrayList<>();
                accounts.add(account);

                client = clientRepository.save(Client.builder()
                        .name(request.getName())
                        .surname(request.getSurname())
                        .primaryAddress(addressRepository.save(mapper.map(request.getPrimaryAddress(), Address.class)))
                        .secondaryAddress(request.getSecondaryAddress() != null ?   addressRepository.save(mapper.map(request.getSecondaryAddress(), Address.class)) : null)
                        .accountList(accounts)
                        .build());
            } else {
                throw new AccountCreateException(request.getAccount(), "Failed to create account");
            }
        } else {
            throw new ClientCreationException(request);
        }
        return mapper.map(client, ClientBo.class);
    }


    @Override
    public List<ClientBo> getClients() {
        return mapper.mapAsList(clientRepository.findAll(), ClientBo.class);
    }

    @Override
    public ClientBo getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        client.orElseThrow(() -> new ClientNotFoundException(id));
        return mapper.map(client.get(), ClientBo.class);
    }

    private boolean validateClientRequest(ClientRequest request) {
        return isNotEmpty(request.getName())
                && isNotEmpty(request.getSurname())
                && isValidAddress(request.getPrimaryAddress(), AddressType.PRIMARY)
                && isValidAddress(request.getSecondaryAddress(), AddressType.SECONDARY)
                && isValidAccountRequest(request.getAccount());
    }

    private boolean isValidAccountRequest(AccountRequest accountRequest) {
        return  accountRequest != null && accountRequest.getAccountType() != null;
    }

    private boolean isValidAddress(com.mfava.financetask.bo.Address address, AddressType addressType) {
        return addressType != AddressType.PRIMARY || isNotEmpty(address.getAddressLine1())
                && isNotEmpty(address.getCity())
                && isNotEmpty(address.getCountry().getCountryName());
    }


    private enum AddressType {
        PRIMARY, SECONDARY;
    }
}
