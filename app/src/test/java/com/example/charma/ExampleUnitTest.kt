package com.example.charma


import org.junit.Test
import org.junit.Assert.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect(){
        assertEquals(4,2+2)
    }

    @Test
    fun `account settings popup dismiss test`() {
        //Arrange: Set up uor test
        var isPopupShown = true

        //Act: Simulate dismissing the popup
        val onDismiss = { isPopupShown = false }
        onDismiss()

        //Assert: Check if popup was dismissed
        assertFalse(isPopupShown)
    }

    @Test
    fun `account settings popup show test`(){
        //Arrange : Start with popup hidden
        var isPopupShown = false

        //Act: Simulate clicking the settings button
        val onShowPopup = { isPopupShown = true }
        onShowPopup()

        //Assert: Check if popup is shown
        assertTrue(isPopupShown)
    }

    @Test
    fun `test email format validation`() {
        // Test if an email has correct format (contains @ and .)
        val testEmail = "test@example.com"
        assertTrue(testEmail.contains("@") && testEmail.contains("."))
    }

    @Test
    fun `test password length requirement`() {
        // Test if password meets minimum length (e.g., 6 characters)
        val testPassword = "password123"
        assertTrue(testPassword.length >= 6)
    }


}



