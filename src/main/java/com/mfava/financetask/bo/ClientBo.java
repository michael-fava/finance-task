package com.mfava.financetask.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientBo extends ResourceSupport{

    private long clientId;
    private String name;
    private String surname;
    private Address primaryAddress;
    private Address secondaryAddress;

    @JsonIgnore
    private List<AccountBo> accountList;

}
