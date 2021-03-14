package com.es.phoneshop.exception;

import java.util.Locale;

public class InvalidParamException extends Exception {
    public codes code;

    public InvalidParamException(String code) {
        this.code = codes.valueOf(code);
    }

    public enum codes {MIN, MAX}

    @Override
    public String toString() {
        return "Value in the field " + code.toString().toLowerCase(Locale.ROOT) + " price is not a number";
    }
}
