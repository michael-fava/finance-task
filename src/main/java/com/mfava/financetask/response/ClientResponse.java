package com.mfava.financetask.response;

import com.mfava.financetask.bo.ClientBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse implements Serializable {

    private ClientBo clientBo;


}
