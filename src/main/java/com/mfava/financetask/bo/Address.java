package com.mfava.financetask.bo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String addressLine1;
    private String addressLine2;
    private String city;
    private CountryBo country;
}
