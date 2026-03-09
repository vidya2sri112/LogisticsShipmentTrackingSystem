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
 * OpenShipmentPortalTest.java
 * TC_004 - Verify the Shipment Tracking Portal opens correctly.
 * TC_005 - Verify portal home page elements are visible.
 */
public class OpenShipmentPortalTest extends BaseTest {

    private static final String PORTAL_URL = "https://www.logistics-tracking-demo.com";

    @Test(priority = 1, description = "TC_004 - Verify Shipment Portal Opens Successfully")
    public void verifyPortalOpens() {
        driver.get(PORTAL_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.urlToBe(PORTAL_URL + "/"));

        String currentURL = driver.getCurrentUrl();
        System.out.println("Portal URL: " + currentURL);

        Assert.assertTrue(currentURL.contains("logistics"),
            "URL should contain 'logistics'. Actual: " + currentURL);

        System.out.println("PASS: Shipment Tracking Portal opened successfully.");
    }

    @Test(priority = 2, description = "TC_005 - Verify Portal Home Page Loads and Key Elements Exist")
    public void verifyPortalHomePageElements() {
        driver.get(PORTAL_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Verify the page title/header
        WebElement headerLogo = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".navbar-brand, .logo, header"))
        );
        Assert.assertTrue(headerLogo.isDisplayed(), "Header/Logo should be visible on portal home");

        // Verify tracking input is present on home page
        WebElement trackingInput = driver.findElement(By.cssSelector("input[placeholder*='Track'], #trackingId, input[name='tracking']"));
        Assert.assertTrue(trackingInput.isDisplayed(), "Tracking input field should be visible on home page");

        System.out.println("PASS: Portal home page elements verified successfully.");
    }

    @Test(priority = 3, description = "TC_006 - Verify Page Title of Shipment Portal")
    public void verifyPortalPageTitle() {
        driver.get(PORTAL_URL);

        String title = driver.getTitle();
        System.out.println("Portal Page Title: " + title);

        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");

        System.out.println("PASS: Portal page title is: " + title);
    }
}
