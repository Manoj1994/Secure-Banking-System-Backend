package com.bankingapp.utils;

import org.springframework.stereotype.Component;

@Component
public class AmountUtils {

    public boolean isValidAmount(String amount) {
        return (amount.replaceAll(",", "").matches("^(\\d+\\.)?\\d+$"))
                || amount.isEmpty();
    }

    public boolean isCritical(String amount, Double criticalAmount) {

        amount = amount.replaceAll(",", "");
        Double doubleAmount = Double.parseDouble(amount);

        return doubleAmount > criticalAmount;
    }
}
