package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * LoginWithValidCredentialsTest.java
 * TC_007 - Login with valid admin credentials
 * TC_008 - Verify dashboard loads after successful login
 * TC_009 - Verify username is displayed after login
 */
public class LoginWithValidCredentialsTest extends BaseTest {

    private static final String LOGIN_URL = "https://www.logistics-tracking-demo.com/login";
    private static final String VALID_EMAIL = "admin@logistics.com";
    private static final String VALID_PASSWORD = "Admin@123";

    @Test(priority = 1, description = "TC_007 - Login with Valid Admin Credentials")
    public void loginWithValidCredentials() {
        driver.get(LOGIN_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Enter valid email
        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("email"))
        );
        emailField.clear();
        emailField.sendKeys(VALID_EMAIL);
        System.out.println("Entered email: " + VALID_EMAIL);

        // Enter valid password
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(VALID_PASSWORD);
        System.out.println("Entered password: ********");

        // Click login button
        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        loginButton.click();
        System.out.println("Clicked Login button");

        // Wait for dashboard
        wait.until(ExpectedConditions.urlContains("dashboard"));

        String currentURL = driver.getCurrentUrl();
        System.out.println("Redirected to: " + currentURL);

        Assert.assertTrue(currentURL.contains("dashboard"),
            "Should redirect to dashboard after login. Actual URL: " + currentURL);

        System.out.println("PASS: Login with valid credentials successful.");
    }

    @Test(priority = 2, description = "TC_008 - Verify Dashboard Loads After Login",
          dependsOnMethods = "loginWithValidCredentials")
    public void verifyDashboardAfterLogin() {
        driver.get(LOGIN_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.findElement(By.id("email")).sendKeys(VALID_EMAIL);
        driver.findElement(By.id("password")).sendKeys(VALID_PASSWORD);
        driver.findElement(By.id("loginBtn")).click();

        // Verify dashboard header
        WebElement dashboardHeader = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.dashboard-title, .welcome-header, #dashboardHeading"))
        );
        Assert.assertTrue(dashboardHeader.isDisplayed(), "Dashboard header should be visible after login");

        System.out.println("PASS: Dashboard loaded successfully after login.");
    }

    @Test(priority = 3, description = "TC_009 - Verify Logged-In User Name Is Displayed")
    public void verifyLoggedInUserName() {
        driver.get(LOGIN_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.findElement(By.id("email")).sendKeys(VALID_EMAIL);
        driver.findElement(By.id("password")).sendKeys(VALID_PASSWORD);
        driver.findElement(By.id("loginBtn")).click();

        wait.until(ExpectedConditions.urlContains("dashboard"));

        // Check for user display element
        WebElement userNameDisplay = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".user-name, #loggedInUser, .profile-name"))
        );
        String displayedName = userNameDisplay.getText();
        System.out.println("Logged-in user displayed as: " + displayedName);

        Assert.assertFalse(displayedName.isEmpty(), "Logged-in username should not be empty");

        System.out.println("PASS: Logged-in user name is displayed correctly.");
    }

    @DataProvider(name = "validCredentials")
    public Object[][] provideValidCredentials() {
        return new Object[][] {
            {"admin@logistics.com", "Admin@123"},
            {"manager@logistics.com", "Manager@456"}
        };
    }

    @Test(dataProvider = "validCredentials", description = "TC_010 - Data-Driven Login with Multiple Valid Credentials")
    public void loginWithMultipleValidCredentials(String email, String password) {
        driver.get(LOGIN_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("email"))
        );
        emailField.clear();
        emailField.sendKeys(email);

        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("loginBtn")).click();

        wait.until(ExpectedConditions.urlContains("dashboard"));

        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"),
            "Login should succeed for: " + email);

        System.out.println("PASS: Login successful for user: " + email);
    }
}
