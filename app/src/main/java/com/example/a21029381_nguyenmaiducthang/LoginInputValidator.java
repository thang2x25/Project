package com.example.a21029381_nguyenmaiducthang;

public class LoginInputValidator {
    public static boolean isValid(String username, String password) {
        return username != null && !username.trim().isEmpty()
                && password != null && !password.trim().isEmpty();
    }
}
