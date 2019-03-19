package com.mfava.akcetask.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private long clientId;
    private String name;
    private String surname;
    private Address primaryAddress;
    private Address secondaryAddress;
    private List<AccountBo> accountList;

}
