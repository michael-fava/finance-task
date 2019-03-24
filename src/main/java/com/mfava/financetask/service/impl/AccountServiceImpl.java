package com.mfava.financetask.service.impl;

import com.google.common.collect.ImmutableList;
import com.mfava.financetask.bo.AccountBo;
import com.mfava.financetask.bo.TransactionBo;
import com.mfava.financetask.bo.TransferResultBo;
import com.mfava.financetask.exception.*;
import com.mfava.financetask.model.*;
import com.mfava.financetask.repo.AccountRepository;
import com.mfava.financetask.repo.ClientRepository;
import com.mfava.financetask.repo.TransactionRepository;
import com.mfava.financetask.request.AccountRemoveRequest;
import com.mfava.financetask.request.AccountRequest;
import com.mfava.financetask.request.InterAccountTransferRequest;
import com.mfava.financetask.response.RemoveAccountResult;
import com.mfava.financetask.service.AccountService;
import com.mfava.financetask.utils.CustomObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    public AccountServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, ClientRepository clientRepository, CustomObjectMapper mapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    TransactionRepository transactionRepository;

    AccountRepository accountRepository;

    ClientRepository clientRepository;

    CustomObjectMapper mapper;

    @Override
    public AccountBo addClientAccount(AccountRequest request){
        if(validAccountRequest(request)){
            Optional<Client> client = clientRepository.findById(request.getClientId());
            if(client.isPresent()){
                Client c = client.get();

                Account account = accountRepository.save(Account.builder()
                        .accountType(mapper.map(request.getAccountType(), AccountType.class))
                        .balance(0.0)
                        .balanceStatus(BalanceStatus.DR)
                        .build());

                if(account != null) {
                    if (c.getAccountList() != null) {
                        c.getAccountList().add(account);
                    } else {
                        c.setAccountList(ImmutableList.of(account));
                    }
                    clientRepository.save(c);
                    return mapper.map(account, AccountBo.class);
                }else{
                    throw new AccountCreateException(request,"Failed to create account");
                }

            }else {
                throw  new ClientNotFoundException(request.getClientId());

            }
        }else{
            throw new AccountCreateException(request,"Account Request Failed Validation");
        }
    }

    @Override
    public RemoveAccountResult removeClientAccount(AccountRemoveRequest request) {
         if(validateAccountRemoveRequest(request)){
             Optional<Client> client = clientRepository.findById(request.getClientId());
             if(client.isPresent()){
                 Optional<Account> account = accountRepository.findById(request.getAccountId());
                 if(account.isPresent()) {
                 //Assumption - Account will be detached from client but not deleted
                 client.ifPresent(c -> {
                     c.getAccountList().remove(account.get());
                     clientRepository.save(c);
                 });


                 return RemoveAccountResult.builder().result(Boolean.TRUE).build();

                 }else {
                     throw new AccountNotFoundException(request.getAccountId());
                 }
             }else {
                 throw  new ClientNotFoundException(request.getClientId());
             }
         }else{
             throw new AccountRemoveException("Failed validation");
         }
    }

    private boolean validateAccountRemoveRequest(AccountRemoveRequest request) {
        return request.getClientId() != null && request.getAccountId() != null;
    }


    @Transactional
    @Override
    public TransferResultBo interAccountTransfer(InterAccountTransferRequest request) {
        Optional<Account> destinationAccountOp = accountRepository.findById(request.getDestinationAccount());

        if(!destinationAccountOp.isPresent()){
            throw  new AccountNotFoundException(request.getDestinationAccount());
        }else{
            Account sourceAccount = debitAccount(request.getSourceAccount(), request.getAmount());
            Account destinationAccount = creditAccount(destinationAccountOp.get(),request.getAmount());

            Transaction transaction = transactionRepository.save(Transaction.builder()
                    .amount(request.getAmount())
                    .debitAccount(sourceAccount)
                    .creditAccount(destinationAccount)
                    .message(isNotBlank(request.getMessage()) ? request.getMessage() : null)
                    .build());

            return  TransferResultBo.builder()
                    .transactionId(transaction.getTransactionId())
                    .build();
        }
    }

    @Override
    public List<AccountBo> getClientAccounts(Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if(client.isPresent() && client.get().getAccountList() != null ){
            return  mapper.mapAsList(client.get().getAccountList(),AccountBo.class);
        }else {
            throw new ClientNotFoundException(clientId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionBo> getAccountTransactions(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isPresent()){
            return transactionRepository.findAllByDebitAccountEqualsOrCreditAccountEquals(account.get(),account.get())
                    .map(transaction -> TransactionBo.builder()
                            .transactionId(transaction.getTransactionId())
                            .creditAccount(transaction.getCreditAccount().getAccountId())
                            .debitAccount(transaction.getDebitAccount().getAccountId())
                            .transactionType(transaction.getDebitAccount().getAccountId() == accountId ? com.mfava.financetask.bo.BalanceStatus.DR
                                    : com.mfava.financetask.bo.BalanceStatus.CR)
                            .amount(transaction.getAmount())
                            .dateCreated(transaction.getDateCreated())
                            .message(transaction.getMessage())
                            .build())
                    .collect(Collectors.toList());

        } else{ throw new AccountNotFoundException(accountId);}
    }

    @Override
    public AccountBo getAccount(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isPresent()){
            return mapper.map(account.get(),AccountBo.class);
        }else{
            throw new AccountNotFoundException(accountId);
        }
    }

    @Override
    public List<AccountBo> getAllAccounts() {
        return mapper.mapAsList(accountRepository.findAll(),AccountBo.class);
    }

    public Account debitAccount(Long accountId, Double amount){
        Optional<Account> sourceAccount = accountRepository.findById(accountId);
        if(sourceAccount.isPresent()) {
            Account account = sourceAccount.get();

            if(account.getBalance() < amount || account.getBalanceStatus().equals(BalanceStatus.CR)){
                throw new InsufficientFundsException();
            }

            Double newAccBalance = account.getBalance() - amount;

            if (newAccBalance < 0) {
                account.setBalance(0 - newAccBalance);
                account.setBalanceStatus(BalanceStatus.CR);
            } else {
                account.setBalance(newAccBalance);
                account.setBalanceStatus(BalanceStatus.DR);
            }

            return accountRepository.save(account);

        }else{
                throw new AccountNotFoundException(accountId);
        }
    }


    public Account creditAccount(Account account, Double amount) {
            if(account.getBalanceStatus().equals(BalanceStatus.CR)){
                account.setBalance(0 - account.getBalance());
            }
            account.setBalance(account.getBalance() + amount);

            if(account.getBalance() < 0){
                account.setBalance(0 - account.getBalance());
                account.setBalanceStatus(BalanceStatus.CR);
            }else{
                account.setBalanceStatus(BalanceStatus.DR);
            }
            return accountRepository.save(account);
    }

    private boolean validAccountRequest(AccountRequest request) {
        return request.getAccountType() != null && request.getClientId() != null;

    }
}
