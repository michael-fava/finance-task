package com.mfava.akcetask.response;

import com.mfava.akcetask.bo.ClientBo;
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
