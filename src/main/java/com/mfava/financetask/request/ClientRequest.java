package com.mfava.financetask.request;

import com.mfava.financetask.bo.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

    private String name;
    private String surname;
    private Address primaryAddress;
    private Address secondaryAddress;
    private AccountRequest account;

}
