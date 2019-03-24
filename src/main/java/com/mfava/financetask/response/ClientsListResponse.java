package com.mfava.financetask.response;

import com.mfava.financetask.bo.ClientBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientsListResponse {

    private List<ClientBo> clientBoList;
}
