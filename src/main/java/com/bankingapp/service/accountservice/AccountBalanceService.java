//package com.bankingapp.service.accountservice;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class AccountBalanceService {
//
//    public boolean validateAmount(int payerAccountNumber, Double amount) {
//
//        String sql = "Select balance,hold from bank_accounts b where b.account_number = :account_numer";
//
//        BankAccount ba = (BankAccount) jdbcTemplate.queryForObject(sql,
//                new Object[] { payerAccountNumber }, new BankAccountMapper());
//
//        if (amount.compareTo((ba.getBalance().subtract(ba.getHold()))) == 1
//                || amount.compareTo(new BigDecimal("0")) == -1) {
//            return false;
//        }
//        return true;
//    }
//}
