package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * LoginTest.java
 * TC_001 - Verify that the login page loads successfully.
 * TC_002 - Verify UI elements are visible on the login page.
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "TC_001 - Verify Login Page Loads Successfully")
    public void verifyLoginPageLoads() {
        openApplication();

        String actualTitle = driver.getTitle();
        System.out.println("Page Title: " + actualTitle);

        Assert.assertNotNull(actualTitle, "Page title should not be null");
        Assert.assertFalse(actualTitle.isEmpty(), "Page title should not be empty");

        System.out.println("PASS: Login page loaded successfully.");
    }

    @Test(priority = 2, description = "TC_002 - Verify Login Page UI Elements Are Visible")
    public void verifyLoginPageUIElements() {
        openApplication();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait and verify Email field
        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("email"))
        );
        Assert.assertTrue(emailField.isDisplayed(), "Email field should be visible");

        // Verify Password field
        WebElement passwordField = driver.findElement(By.id("password"));
        Assert.assertTrue(passwordField.isDisplayed(), "Password field should be visible");

        // Verify Login button
        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        Assert.assertTrue(loginButton.isDisplayed(), "Login button should be visible");
        Assert.assertTrue(loginButton.isEnabled(), "Login button should be enabled");

        System.out.println("PASS: All login page UI elements are visible.");
    }

    @Test(priority = 3, description = "TC_003 - Verify Login Page URL")
    public void verifyLoginPageURL() {
        openApplication();

        String currentURL = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentURL);

        Assert.assertNotNull(currentURL, "URL should not be null");
        Assert.assertTrue(currentURL.contains("login") || currentURL.contains("logistics"),
            "URL should contain expected keywords");

        System.out.println("PASS: Login page URL verified.");
    }
}
