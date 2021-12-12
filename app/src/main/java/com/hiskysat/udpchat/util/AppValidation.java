package com.hiskysat.udpchat.util;

import java.util.regex.Pattern;

public class AppValidation {

    private static final Pattern NOT_NUMBER_PATTERN = Pattern.compile("\\D+");

    public static final int PORT_MIN_NUMBER = 1025;
    public static final int PORT_MAX_NUMBER = 65535;

    public static boolean isNotValidNumber(String value) {
        return NOT_NUMBER_PATTERN.matcher(value).matches();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isWithPortRange(String value) {
        try {
            int portValue = Integer.parseInt(value);
            return portValue >= PORT_MIN_NUMBER && portValue <= PORT_MAX_NUMBER;
        } catch (Exception e) {
            return false;
        }
    }

}
