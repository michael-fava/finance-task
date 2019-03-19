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
public class TransactionBo {

    private long id;
    private AccountBo debitAccount;
    private AccountBo creditAccount;
    private Double amount;
    private String message;
    private Date dateCreated;

}
