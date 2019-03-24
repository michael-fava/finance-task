package com.mfava.akcetask.response;

import com.mfava.akcetask.bo.ClientBo;
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
