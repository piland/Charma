package com.example.charma

import com.example.charma.authenticationmanager.isValidEmail
import org.junit.Test
import org.junit.Assert.*

class BDfunctionsTest {

    @Test
    fun testIsValidEmail_ValidEmail(){
        val validEmail = "test@gmail.com"
        val result = isValidEmail(validEmail)
        assertTrue("Expected the email to be valid", result)
    }

    @Test
    fun testIsValidEmail_InvalidEmail(){
        val invalidEmail = "invalid-email"
        val result = isValidEmail(invalidEmail)
        assertFalse("Expected the email to be invalid", result)
    }
}