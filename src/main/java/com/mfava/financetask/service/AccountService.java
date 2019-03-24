package com.mfava.financetask.service;

import com.mfava.financetask.bo.AccountBo;
import com.mfava.financetask.bo.TransactionBo;
import com.mfava.financetask.bo.TransferResultBo;
import com.mfava.financetask.request.AccountRemoveRequest;
import com.mfava.financetask.request.AccountRequest;
import com.mfava.financetask.request.InterAccountTransferRequest;
import com.mfava.financetask.response.RemoveAccountResult;

import java.util.List;

public interface AccountService {

    AccountBo addClientAccount(AccountRequest request);

    RemoveAccountResult removeClientAccount(AccountRemoveRequest request);

    TransferResultBo interAccountTransfer(InterAccountTransferRequest request);

    List<AccountBo> getClientAccounts(Long clientId);

    List<TransactionBo> getAccountTransactions(Long accountId);

    AccountBo getAccount(Long accountId);

    List<AccountBo> getAllAccounts();
}
