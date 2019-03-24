package com.mfava.financetask;

import com.mfava.financetask.bo.*;
import com.mfava.financetask.exception.*;
import com.mfava.financetask.model.Account;
import com.mfava.financetask.model.Country;
import com.mfava.financetask.repo.AccountRepository;
import com.mfava.financetask.repo.CountryRepository;
import com.mfava.financetask.request.AccountRequest;
import com.mfava.financetask.request.ClientRequest;
import com.mfava.financetask.request.InterAccountTransferRequest;
import com.mfava.financetask.service.AccountService;
import com.mfava.financetask.service.ClientService;
import com.mfava.financetask.utils.CustomObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ServiceTests {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CustomObjectMapper mapper;

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void populateCountryRepo() {
        countryRepository.save(Country.builder()
                .countryName("Malta")
                .build());

    }


    @Test
    public void createClientSuccessTest() {

        Address testAddr = Address.builder()
                .addressLine1("TestHse, TestStreet")
                .city("TestCity")
                .country(mapper.map(countryRepository.findAll().stream().findFirst().get(), CountryBo.class))
                .build();

        ClientRequest clientRequest = new ClientRequest("TName", "TSurname", testAddr, null, new AccountRequest(null, AccountType.CURRENT));

        ClientBo clientBo = clientService.addNewClient(clientRequest);

        assertNotNull(clientBo);

    }

    @Test
    public void createClientFailNameTest() {

        Address testAddr = Address.builder()
                .addressLine1("TestHse, TestStreet")
                .city("TestCity")
                .country(mapper.map(countryRepository.findAll().stream().findFirst().get(), CountryBo.class))
                .build();

        //Name set to null to fail in validation
        ClientRequest clientRequest = new ClientRequest(null, "TSurname", testAddr, null, new AccountRequest(null, AccountType.CURRENT));

        try {
            clientService.addNewClient(clientRequest);
        } catch (ClientCreationException x) {
            assertNotNull(x);
        }
    }

    @Test
    public void createClientFailAccountTest() {

        Address testAddr = Address.builder()
                .addressLine1("TestHse, TestStreet")
                .city("TestCity")
                .country(mapper.map(countryRepository.findAll().stream().findFirst().get(), CountryBo.class))
                .build();

        //AccountRequest created with no AccountType set to fail in validation
        ClientRequest clientRequest = new ClientRequest("Tname", "TSurname", testAddr, null, new AccountRequest());

        try {
            clientService.addNewClient(clientRequest);
        } catch (ClientCreationException x) {
            assertNotNull(x);
        }
    }


    @Test
    public void getClientsNullTest(){
        List<ClientBo> clients = clientService.getClients();

        assertNotNull(clients);

    }


    @Test
    public void getClientsPopulatedTest(){
        generateClientBo();

        List<ClientBo> clients = clientService.getClients();

        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }


    @Test
    public void getClientsByIdSuccessTest(){
        ClientBo clientBo = generateClientBo();

        ClientBo client = clientService.getClientById(clientBo.getClientId());

        assertNotNull(client);
        assertEquals(client.getClientId(),clientBo.getClientId());
    }

    @Test
    public void getClientsByIdFailTest(){
        Long maxClientId = clientService.getClients().stream()
                                        .mapToLong(ClientBo::getClientId)
                                        .max()
                                        .orElse(0);

        try {
            clientService.getClientById(maxClientId + 1);
        }catch (ClientNotFoundException c) {
            assertNotNull(c);
        }
    }



    @Test
    public void addClientAccountSuccessTest(){
        ClientBo clientBo = generateClientBo();

        AccountRequest accountRequest = new AccountRequest(clientBo.getClientId(),AccountType.CURRENT);

        AccountBo account = accountService.addClientAccount(accountRequest);

        assertNotNull(account);
        assertEquals(0.0,account.getBalance(),0);
    }

    @Test
    public void addAccountFailureNoAccTypeTest(){
        ClientBo clientBo = generateClientBo();

        //Should fail validation due to null accountType
        AccountRequest accountRequest = new AccountRequest(clientBo.getClientId(), null);
        try {
            accountService.addClientAccount(accountRequest);
        }catch (AccountCreateException a){
            assertNotNull(a);
        }
    }

    @Test
    public void addAccountFailureNoClientTest(){
        ClientBo clientBo = generateClientBo();

        //Should fail validation due to Client Not Found
        AccountRequest accountRequest = new AccountRequest(clientBo.getClientId()+100, AccountType.CURRENT);
        try {
            accountService.addClientAccount(accountRequest);
        }catch (ClientNotFoundException c){
            assertNotNull(c);
        }
    }

    @Test
    public void interAccountTransferSuccessTest(){

        List<AccountBo> clientAccounts = generateAccountBos();

        accountRepository.saveAll(mapper.mapAsList(clientAccounts, Account.class).stream()
                                                    .peek(acc -> acc.setBalance(100.0))
                                                    .collect(Collectors.toList()));


        //Both created accounts have a balance of 100.0

        InterAccountTransferRequest transferRequest = new InterAccountTransferRequest(clientAccounts.get(0).getAccountId(),
                                                                                        clientAccounts.get(1).getAccountId(),
                                                                                8.82,
                                                                                "Test InterAccountTransfer");

        TransferResultBo transferResult = accountService.interAccountTransfer(transferRequest);

        assertNotNull(transferResult);
        assertNotNull(transferResult.getTransactionId());

    }

    @Test
    public void interAccountTransferInsufficientFundsTest(){

        List<AccountBo> clientAccounts = generateAccountBos();

        accountRepository.saveAll(mapper.mapAsList(clientAccounts, Account.class).stream()
                .peek(acc -> acc.setBalance(100.0))
                .collect(Collectors.toList()));


        //Both created accounts have a balance of 100.0

        InterAccountTransferRequest transferRequest = new InterAccountTransferRequest(clientAccounts.get(0).getAccountId(),
                clientAccounts.get(1).getAccountId(),
                108.82,
                "Test InterAccountTransfer more than available funds");

        try {
            accountService.interAccountTransfer(transferRequest);
        }catch (InsufficientFundsException i){
            assertNotNull(i);
        }
    }


    @Test
    public void interAccountTransferNoDestAccountTest(){
        List<AccountBo> clientAccounts = generateAccountBos();

        accountRepository.saveAll(mapper.mapAsList(clientAccounts, Account.class).stream()
                .peek(acc -> acc.setBalance(100.0))
                .collect(Collectors.toList()));


        //Both created accounts have a balance of 100.0

        InterAccountTransferRequest transferRequest = new InterAccountTransferRequest(clientAccounts.get(0).getAccountId(),
                clientAccounts.get(1).getAccountId()+100,
                8.82,
                "Test InterAccountTransfer no dest account");

        try {
            accountService.interAccountTransfer(transferRequest);
        }catch (AccountNotFoundException i){
            assertNotNull(i);
        }
    }


    @Test
    public void interAccountTransferNoSourceAccountTest(){
        List<AccountBo> clientAccounts = generateAccountBos();

        accountRepository.saveAll(mapper.mapAsList(clientAccounts, Account.class).stream()
                .peek(acc -> acc.setBalance(100.0))
                .collect(Collectors.toList()));


        //Both created accounts have a balance of 100.0

        InterAccountTransferRequest transferRequest = new InterAccountTransferRequest(clientAccounts.get(0).getAccountId()+100,
                clientAccounts.get(1).getAccountId(),
                8.82,
                "Test InterAccountTransfer no source account");

        try {
            accountService.interAccountTransfer(transferRequest);
        }catch (AccountNotFoundException i){
            assertNotNull(i);
        }
    }


    @Test
    public void getClientAccountsSuccessTest(){
        ClientBo clientBo = generateClientBo();

        List<AccountBo> clientAccounts = accountService.getClientAccounts(clientBo.getClientId());

        assertNotNull(clientAccounts);
        assertFalse(clientAccounts.isEmpty());

    }

    @Test
    public void getClientAccountsNoClientTest(){
        ClientBo clientBo = generateClientBo();
        try {
            accountService.getClientAccounts(clientBo.getClientId() + 100);
        }catch (ClientNotFoundException c) {
            assertNotNull(c);
        }

    }


    @Test
    public void  getAccountTransactionsSuccessTest(){
        List<AccountBo> clientAccounts = generateAccountBos();

        accountRepository.saveAll(mapper.mapAsList(clientAccounts, Account.class).stream()
                .peek(acc -> acc.setBalance(100.0))
                .collect(Collectors.toList()));


        //Both created accounts have a balance of 100.0

        AccountBo sourceAcc = clientAccounts.get(0);
        AccountBo destAcc = clientAccounts.get(1);

        InterAccountTransferRequest transferRequest = new InterAccountTransferRequest(sourceAcc.getAccountId(),
                destAcc.getAccountId(),
                8.82,
                "Test InterAccountTransfer");

        TransferResultBo transferResult = accountService.interAccountTransfer(transferRequest);

        assertNotNull(transferResult);
        assertNotNull(transferResult.getTransactionId());

        List<TransactionBo> srcAccTr = accountService.getAccountTransactions(sourceAcc.getAccountId());

        assertNotNull(srcAccTr);
        assertEquals(srcAccTr.stream().findFirst().map(TransactionBo::getTransactionId).orElse(0L),transferResult.getTransactionId());

        List<TransactionBo> destAccTr = accountService.getAccountTransactions(destAcc.getAccountId());

        assertNotNull(destAccTr);
        assertEquals(destAccTr.stream().findFirst().map(TransactionBo::getTransactionId).orElse(0L),transferResult.getTransactionId());


    }

    @Test
    public void  getAccountTransactionsNoTransactionTest(){
        List<AccountBo> clientAccounts = generateAccountBos();

        accountRepository.saveAll(mapper.mapAsList(clientAccounts, Account.class).stream()
                .peek(acc -> acc.setBalance(100.0))
                .collect(Collectors.toList()));


        //Both created accounts have a balance of 100.0

        AccountBo sourceAcc = clientAccounts.get(0);
        AccountBo destAcc = clientAccounts.get(1);


        List<TransactionBo> srcAccTr = accountService.getAccountTransactions(sourceAcc.getAccountId());

        assertNotNull(srcAccTr);
        assertTrue(srcAccTr.isEmpty());

        List<TransactionBo> destAccTr = accountService.getAccountTransactions(destAcc.getAccountId());

        assertNotNull(destAccTr);
        assertTrue(destAccTr.isEmpty());
    }

    @Test
    public void  getAccountTransactionsNoAccountTest(){
        List<AccountBo> clientAccounts = generateAccountBos();
        AccountBo destAcc = clientAccounts.get(1);

        try {
            accountService.getAccountTransactions(destAcc.getAccountId() + 100);
        }catch (AccountNotFoundException a) {
            assertNotNull(a);
        }
    }

    @Test
    public void  getAccountSuccessTest(){
        List<AccountBo> clientAccounts = generateAccountBos();
        AccountBo acc = clientAccounts.get(0);

        AccountBo account = accountService.getAccount(acc.getAccountId());

        assertNotNull(account);
        assertEquals(account.getAccountId(),acc.getAccountId());

    }

    @Test
    public void  getAccountNotFoundTest(){
        List<AccountBo> clientAccounts = generateAccountBos();
        AccountBo acc = clientAccounts.get(0);

        try {
            accountService.getAccount(acc.getAccountId()+100);
        }catch (AccountNotFoundException a){
            assertNotNull(a);
        }
    }

    @Test
    public void  getAllAccountSuccessTest(){
        generateAccountBos();

        List<AccountBo> allAccounts = accountService.getAllAccounts();

        assertNotNull(allAccounts);
        assertFalse(allAccounts.isEmpty());
    }

    private ClientBo generateClientBo() {
        Address testAddr = Address.builder()
                .addressLine1("TestHse, TestStreet")
                .city("TestCity")
                .country(mapper.map(countryRepository.findAll().stream().findFirst().get(), CountryBo.class))
                .build();

        ClientRequest clientRequest = new ClientRequest("TName", "TSurname", testAddr, null, new AccountRequest(null, AccountType.CURRENT));

        return clientService.addNewClient(clientRequest);
    }

    private List<AccountBo> generateAccountBos() {
        ClientBo clientBo = generateClientBo();

        AccountRequest accountRequest = new AccountRequest(clientBo.getClientId(), AccountType.CURRENT);

        accountService.addClientAccount(accountRequest);

        return accountService.getClientAccounts(clientBo.getClientId());
    }




}
