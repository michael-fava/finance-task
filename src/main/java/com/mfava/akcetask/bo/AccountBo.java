package com.mfava.akcetask.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBo extends ResourceSupport {

    private Long accountId;
    private AccountType accountType;
    private Double balance;
    private BalanceStatus balanceStatus;
    private Date dateCreated;

}
