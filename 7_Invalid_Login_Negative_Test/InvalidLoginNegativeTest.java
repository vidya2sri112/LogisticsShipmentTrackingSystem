package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * InvalidLoginNegativeTest.java
 * TC_020 - Login with invalid email and valid password
 * TC_021 - Login with valid email and invalid password
 * TC_022 - Login with empty email field
 * TC_023 - Login with empty password field
 * TC_024 - Login with both fields empty
 * TC_025 - Data-driven negative login tests
 */
public class InvalidLoginNegativeTest extends BaseTest {

    private static final String LOGIN_URL = "https://www.logistics-tracking-demo.com/login";

    @BeforeMethod
    public void navigateToLogin() {
        driver.get(LOGIN_URL);
    }

    @Test(priority = 1, description = "TC_020 - Login with Invalid Email and Valid Password Shows Error")
    public void loginWithInvalidEmail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("email"))
        );
        emailField.clear();
        emailField.sendKeys("wronguser@test.com");

        driver.findElement(By.id("password")).sendKeys("Admin@123");
        driver.findElement(By.id("loginBtn")).click();

        WebElement errorMsg = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("loginError"))
        );
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message should appear for invalid email");

        String errorText = errorMsg.getText();
        System.out.println("Error message: " + errorText);
        Assert.assertTrue(
            errorText.toLowerCase().contains("invalid") || errorText.toLowerCase().contains("incorrect"),
            "Error should indicate invalid credentials"
        );

        // Verify user is NOT redirected to dashboard
        Assert.assertFalse(driver.getCurrentUrl().contains("dashboard"),
            "User should not be redirected to dashboard with invalid email");

        System.out.println("PASS: Correct error shown for invalid email login.");
    }

    @Test(priority = 2, description = "TC_021 - Login with Valid Email and Invalid Password Shows Error")
    public void loginWithInvalidPassword() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")))
            .sendKeys("admin@logistics.com");
        driver.findElement(By.id("password")).sendKeys("wrongpass");
        driver.findElement(By.id("loginBtn")).click();

        WebElement errorMsg = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("loginError"))
        );
        Assert.assertTrue(errorMsg.isDisplayed(), "Error message should appear for invalid password");

        Assert.assertFalse(driver.getCurrentUrl().contains("dashboard"),
            "User should not reach dashboard with invalid password");

        System.out.println("PASS: Correct error shown for invalid password login.");
    }

    @Test(priority = 3, description = "TC_022 - Login with Empty Email Shows Validation Error")
    public void loginWithEmptyEmail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Leave email empty, fill only password
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")))
            .sendKeys("Admin@123");
        driver.findElement(By.id("loginBtn")).click();

        WebElement emailValidation = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("emailError"))
        );
        Assert.assertTrue(emailValidation.isDisplayed(), "Email validation error should appear");

        System.out.println("PASS: Validation shown for empty email field.");
    }

    @Test(priority = 4, description = "TC_023 - Login with Empty Password Shows Validation Error")
    public void loginWithEmptyPassword() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")))
            .sendKeys("admin@logistics.com");
        // Leave password empty
        driver.findElement(By.id("loginBtn")).click();

        WebElement passwordValidation = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("passwordError"))
        );
        Assert.assertTrue(passwordValidation.isDisplayed(), "Password validation error should appear");

        System.out.println("PASS: Validation shown for empty password field.");
    }

    @Test(priority = 5, description = "TC_024 - Login with Both Fields Empty Shows Validation Errors")
    public void loginWithBothFieldsEmpty() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginBtn"))).click();

        WebElement emailValidation = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("emailError"))
        );
        WebElement passwordValidation = driver.findElement(By.id("passwordError"));

        Assert.assertTrue(emailValidation.isDisplayed(), "Email validation error should appear");
        Assert.assertTrue(passwordValidation.isDisplayed(), "Password validation error should appear");

        System.out.println("PASS: Both field validations shown when form submitted empty.");
    }

    @Test(priority = 6, description = "TC_026 - Login with SQL Injection in Email Field - Should Be Rejected")
    public void loginWithSQLInjection() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")))
            .sendKeys("' OR '1'='1");
        driver.findElement(By.id("password")).sendKeys("' OR '1'='1");
        driver.findElement(By.id("loginBtn")).click();

        // Should NOT redirect to dashboard
        Assert.assertFalse(driver.getCurrentUrl().contains("dashboard"),
            "SQL injection should not bypass authentication");

        System.out.println("PASS: SQL injection login attempt correctly blocked.");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] provideInvalidCredentials() {
        return new Object[][] {
            {"wronguser@test.com", "wrongpass",   "Invalid email and password"},
            {"admin@logistics.com", "wrong123",   "Valid email, wrong password"},
            {"notexist@test.com",   "Admin@123",  "Non-existent email, valid password"},
            {"",                    "Admin@123",  "Empty email"},
            {"admin@logistics.com", "",           "Empty password"},
            {"ADMIN@LOGISTICS.COM", "admin@123",  "Case sensitivity check"},
        };
    }

    @Test(dataProvider = "invalidCredentials",
          description = "TC_025 - Data-Driven Negative Login Tests")
    public void dataDrivenNegativeLogin(String email, String password, String testDescription) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        System.out.println("Running test: " + testDescription);

        if (!email.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(email);
        }
        if (!password.isEmpty()) {
            driver.findElement(By.id("password")).sendKeys(password);
        }

        driver.findElement(By.id("loginBtn")).click();

        // Should not reach dashboard for any of these
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assert.assertFalse(driver.getCurrentUrl().contains("dashboard"),
            "User should NOT reach dashboard for: " + testDescription);

        System.out.println("PASS: Login correctly blocked for: " + testDescription);
    }
}
