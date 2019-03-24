package com.mfava.akcetask.request;

import com.mfava.akcetask.bo.AccountType;
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
