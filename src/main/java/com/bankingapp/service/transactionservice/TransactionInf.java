package com.bankingapp.service.transactionservice;

import com.bankingapp.model.transaction.Transaction;

import java.util.List;

public interface TransactionInf {

    public Boolean save(Transaction transaction, String type);

    public List<Transaction> getById(int ide);

    public void update(Transaction employer);

    public boolean deleteById(int id,String type);
}
