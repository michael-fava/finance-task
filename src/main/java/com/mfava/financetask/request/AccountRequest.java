package com.mfava.financetask.request;

import com.mfava.financetask.bo.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private Long clientId;
    private AccountType accountType;

}
