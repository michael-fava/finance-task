package com.mfava.financetask.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterAccountTransferRequest {

    private Long sourceAccount;
    private Long destinationAccount;
    private Double amount;
    private String message;

}


    
    
    
    