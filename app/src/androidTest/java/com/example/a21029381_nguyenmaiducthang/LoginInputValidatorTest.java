package com.example.a21029381_nguyenmaiducthang;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginInputValidatorTest {

    @Test
    public void testValidInput() {
        assertTrue(LoginInputValidator.isValid("admin", "123456"));
    }

    @Test
    public void testEmptyUsername() {
        assertFalse(LoginInputValidator.isValid("", "123456"));
    }

    @Test
    public void testEmptyPassword() {
        assertFalse(LoginInputValidator.isValid("admin", ""));
    }

    @Test
    public void testNullUsername() {
        assertFalse(LoginInputValidator.isValid(null, "123456"));
    }

    @Test
    public void testNullPassword() {
        assertFalse(LoginInputValidator.isValid("admin", null));
    }

    @Test
    public void testBothEmpty() {
        assertFalse(LoginInputValidator.isValid("", ""));
    }
}
