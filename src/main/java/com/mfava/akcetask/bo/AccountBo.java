package com.mfava.akcetask.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBo {

    private Long accountId;
    private Long clientId;
    private AccountType accountType;
    private Double balance;
    private BalanceStatus balanceStatus;
    private Date dateCreated;

}
