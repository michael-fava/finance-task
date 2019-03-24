package com.mfava.akcetask;


import com.mfava.akcetask.model.*;
import com.mfava.akcetask.repo.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RepoTests {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Before
    public void populateCountryRepo() {
        countryRepository.save(Country.builder()
                .countryName("Malta")
                .build());

    }


    @Test
    public void createAddressTest(){
        Address testAddr = addressRepository.save(Address.builder()
                .addressLine1("1, First Street")
                .city("Valletta")
                .country(countryRepository.findAll().stream().findFirst().get())
                .build());

        assertNotNull(testAddr);
    }


    @Test
    public void createClientTest(){
        Client client = clientRepository.save(Client.builder()
                .name("TestName")
                .surname("TestSurname")
                .primaryAddress(addressRepository.save(Address.builder()
                        .addressLine1("1, First Street")
                        .city("Valletta")
                        .country(countryRepository.findAll().stream().findFirst().get())
                        .build()))
                .build());

        assertNotNull(client);
        assertEquals(1,client.getClientId());
        assertNull(client.getAccountList());

    }


    @Test
    public void createAccountTest(){
        Account account = accountRepository.save(Account.builder()
                .balance(100.0)
                .balanceStatus(BalanceStatus.DR)
                .accountType(AccountType.SAVINGS)
                .build());

        assertNotNull(account);
        assertEquals(100.0,account.getBalance(),0);
    }

    @Test
    public void createTransactionTest(){
        assertTrue(transactionRepository.findAll().isEmpty());

        Account account1 = accountRepository.save(Account.builder()
                .balance(100.0)
                .balanceStatus(BalanceStatus.DR)
                .accountType(AccountType.SAVINGS)
                .build());

        Account account2 = accountRepository.save(Account.builder()
                .balance(0.0)
                .balanceStatus(BalanceStatus.DR)
                .accountType(AccountType.SAVINGS)
                .build());

        assertNotNull(account1);
        assertNotNull(account2);

        Transaction transaction = transactionRepository.save(Transaction.builder()
                .debitAccount(account1)
                .creditAccount(account2)
                .amount(1.0)
                .message("Test transaction No actual balance shift in accounts")
                .build());


        assertNotNull(transaction);
        assertEquals(100.0,account1.getBalance(),0);
        assertEquals(0.0,account2.getBalance(),0);

    }



}
