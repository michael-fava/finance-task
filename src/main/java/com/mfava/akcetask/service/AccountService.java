package com.mfava.akcetask.service;

import com.mfava.akcetask.bo.AccountBo;
import com.mfava.akcetask.bo.TransactionBo;
import com.mfava.akcetask.bo.TransferResultBo;
import com.mfava.akcetask.request.AccountRemoveRequest;
import com.mfava.akcetask.request.AccountRequest;
import com.mfava.akcetask.request.InterAccountTransferRequest;
import com.mfava.akcetask.response.RemoveAccountResult;

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
