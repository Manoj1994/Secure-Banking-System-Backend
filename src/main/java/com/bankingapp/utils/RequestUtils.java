package com.bankingapp.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RequestUtils {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_MOBILE_NUMBER = Pattern.compile("^(1\\-)?[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$");

    public boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public Boolean validateContact(String mobile) {
        Matcher matcher = VALID_MOBILE_NUMBER.matcher(mobile);
        return matcher.find();
    }

}