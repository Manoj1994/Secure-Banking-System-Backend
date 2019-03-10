package com.bankingapp.utils;

import org.springframework.stereotype.Component;

@Component
public class AmountUtils {

    public boolean isValidAmount(String amount) {
        return (amount.replaceAll(",", "").matches("^(\\d+\\.)?\\d+$"))
                || amount.isEmpty();
    }

    public boolean isCritical(String amount, Double criticalAmount) {

        Double doubleAmount = convertToDouble(amount);

        return doubleAmount > criticalAmount;
    }

    public Double convertToDouble(String amount) {
        amount = amount.replaceAll(",", "");
        Double doubleAmount = Double.parseDouble(amount);
        return doubleAmount;
    }
}
