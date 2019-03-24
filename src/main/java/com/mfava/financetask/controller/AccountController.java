package com.mfava.financetask.controller;

import com.mfava.financetask.bo.AccountBo;
import com.mfava.financetask.bo.TransactionBo;
import com.mfava.financetask.bo.TransferResultBo;
import com.mfava.financetask.request.AccountRemoveRequest;
import com.mfava.financetask.request.AccountRequest;
import com.mfava.financetask.request.InterAccountTransferRequest;
import com.mfava.financetask.response.RemoveAccountResult;
import com.mfava.financetask.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AccountController {

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    AccountService accountService;

    @PostMapping(value = "/v1/accounts")
    public Resource<AccountBo> createClientAccount(@RequestBody AccountRequest request) {
        AccountBo clientAccount = accountService.addClientAccount(request);
        return new Resource<>(clientAccount, getAccountLink(clientAccount.getAccountId()));
    }

    @DeleteMapping(value = "/v1/accounts")
    public RemoveAccountResult deleteClientAccount(@RequestBody AccountRemoveRequest request) {
        return accountService.removeClientAccount(request);
    }

    @PutMapping(value = "/v1/accounts/transfer")
    public TransferResultBo interAccountTransfer(@RequestBody InterAccountTransferRequest request) {
        return accountService.interAccountTransfer(request);
    }

    @GetMapping(value = "/v1/accounts/{accountId}")
    public Resource<AccountBo> getAccountById(@PathVariable Long accountId) {
        return new Resource<>(accountService.getAccount(accountId), getAccountLink(accountId));
    }

    @GetMapping(value = "/v1/accounts")
    public Resources<AccountBo> getAccounts() {
        List<AccountBo> accountsList = accountService.getAllAccounts();

        return new Resources<>(accountsList.stream()
                .peek(accountBo -> accountBo.add(getAccountLink(accountBo.getAccountId())))
                .collect(Collectors.toList()), getAllAccountsLink());
    }


    @GetMapping(value = "/v1/accounts/clients/{clientId}")
    public Resources<AccountBo> getClientAccounts(@PathVariable Long clientId) {
        List<AccountBo> accountsList = accountService.getClientAccounts(clientId);

        return new Resources<>(accountsList.stream()
                .peek(accountBo -> accountBo.add(getAccountLink(accountBo.getAccountId())))
                .collect(Collectors.toList()), getClientAccountsLink(clientId));
    }

    @GetMapping(value = "/v1/accounts/{accountId}/transactions")
    public Resources<TransactionBo> getAccountTransaction(@PathVariable Long accountId) {
        return new Resources<>(accountService.getAccountTransactions(accountId),getAccountTransactionsLink(accountId));
    }



    private Link getClientAccountsLink(Long accountId) {
        Link selfLink = null;
        try {
            selfLink = ControllerLinkBuilder.linkTo(AccountController.class.getMethod("getClientAccounts", Long.class), accountId).withSelfRel();
        } catch (NoSuchMethodException e) {
            log.warn("Could not generate accounts Link  - {}",e.getMessage());
        }
        return selfLink;
    }

    private Link getAllAccountsLink() {
        Link selfLink = null;
        try {
            selfLink = ControllerLinkBuilder.linkTo(AccountController.class.getMethod("getAccounts"),new HashMap<String,Object>()).withSelfRel();
        } catch (NoSuchMethodException e) {
            log.warn("Could not generate allAccounts Link  - {}",e.getMessage());
        }
        return selfLink;
    }
    private Link getAccountLink(Long clientId) {
        Link selfLink = null;
        try {
            selfLink = ControllerLinkBuilder.linkTo(AccountController.class.getMethod("getAccountById", Long.class), clientId).withSelfRel();
        } catch (NoSuchMethodException e) {
            log.warn("Could not generate account Link  - {}",e.getMessage());
        }
        return selfLink;
    }

    private Link getAccountTransactionsLink(Long accountId) {
        Link selfLink = null;
        try {
            selfLink = ControllerLinkBuilder.linkTo(AccountController.class.getMethod("getAccountTransaction", Long.class), accountId).withSelfRel();
        } catch (NoSuchMethodException e) {
            log.warn("Could not generate account transactions Link  - {}",e.getMessage());
        }
        return selfLink;
    }
}
