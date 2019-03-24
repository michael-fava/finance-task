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
public class TransactionBo extends ResourceSupport {

    private Long transactionId;
    private Long debitAccount;
    private Long creditAccount;
    private BalanceStatus transactionType;
    private Double amount;
    private String message;
    private Date dateCreated;

}
