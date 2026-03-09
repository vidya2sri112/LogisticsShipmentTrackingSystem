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
 * SearchForShipmentTest.java
 * TC_011 - Search shipment using a valid tracking ID
 * TC_012 - Verify shipment details are displayed after search
 * TC_013 - Search with invalid tracking ID returns appropriate error
 * TC_014 - Search with empty tracking ID shows validation message
 */
public class SearchForShipmentTest extends BaseTest {

    private static final String TRACKING_PAGE_URL = "https://www.logistics-tracking-demo.com/track";
    private static final String VALID_TRACKING_ID = "TRK001234";
    private static final String INVALID_TRACKING_ID = "INVALID_XYZ";

    @BeforeMethod
    public void navigateToTrackingPage() {
        driver.get(TRACKING_PAGE_URL);
    }

    @Test(priority = 1, description = "TC_011 - Search Shipment with Valid Tracking ID")
    public void searchShipmentWithValidTrackingID() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement trackingInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("trackingId"))
        );
        trackingInput.clear();
        trackingInput.sendKeys(VALID_TRACKING_ID);
        System.out.println("Entered tracking ID: " + VALID_TRACKING_ID);

        WebElement searchButton = driver.findElement(By.id("searchBtn"));
        searchButton.click();
        System.out.println("Clicked Search button");

        // Wait for results
        WebElement resultsSection = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("trackingResults"))
        );
        Assert.assertTrue(resultsSection.isDisplayed(), "Tracking results should be displayed");

        System.out.println("PASS: Shipment found for tracking ID: " + VALID_TRACKING_ID);
    }

    @Test(priority = 2, description = "TC_012 - Verify Shipment Details Are Displayed After Search")
    public void verifyShipmentDetailsAfterSearch() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.findElement(By.id("trackingId")).sendKeys(VALID_TRACKING_ID);
        driver.findElement(By.id("searchBtn")).click();

        // Verify key shipment detail fields
        WebElement shipmentStatus = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("shipmentStatus"))
        );
        String status = shipmentStatus.getText();
        System.out.println("Shipment Status: " + status);

        Assert.assertFalse(status.isEmpty(), "Shipment status should not be empty");

        // Verify tracking ID displayed in results
        WebElement displayedTrackingID = driver.findElement(By.id("displayTrackingId"));
        Assert.assertTrue(displayedTrackingID.getText().contains(VALID_TRACKING_ID),
            "Results should display the searched tracking ID");

        System.out.println("PASS: Shipment details verified for tracking ID: " + VALID_TRACKING_ID);
    }

    @Test(priority = 3, description = "TC_013 - Search with Invalid Tracking ID Shows Error Message")
    public void searchWithInvalidTrackingID() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement trackingInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("trackingId"))
        );
        trackingInput.clear();
        trackingInput.sendKeys(INVALID_TRACKING_ID);

        driver.findElement(By.id("searchBtn")).click();

        // Expect error message
        WebElement errorMessage = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("errorMsg"))
        );
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message should be displayed for invalid ID");

        String errorText = errorMessage.getText();
        System.out.println("Error message displayed: " + errorText);

        Assert.assertTrue(
            errorText.toLowerCase().contains("not found") || errorText.toLowerCase().contains("invalid"),
            "Error message should indicate tracking ID not found"
        );

        System.out.println("PASS: Correct error shown for invalid tracking ID.");
    }

    @Test(priority = 4, description = "TC_014 - Search with Empty Tracking ID Shows Validation Message")
    public void searchWithEmptyTrackingID() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement trackingInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("trackingId"))
        );
        trackingInput.clear(); // Leave empty

        driver.findElement(By.id("searchBtn")).click();

        // Expect validation message
        WebElement validationMsg = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("validationMsg"))
        );
        Assert.assertTrue(validationMsg.isDisplayed(), "Validation message should appear for empty input");

        System.out.println("PASS: Validation message shown for empty tracking ID.");
    }

    @DataProvider(name = "trackingIDs")
    public Object[][] provideTrackingIDs() {
        return new Object[][] {
            {"TRK001", true},
            {"TRK002", true},
            {"TRK003", true},
            {"INVALID", false},
            {"000000", false},
            {"@#$%^", false}
        };
    }

    @Test(dataProvider = "trackingIDs", description = "TC_015 - Data-Driven Shipment Search")
    public void dataDrivenShipmentSearch(String trackingID, boolean shouldFind) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement trackingInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("trackingId"))
        );
        trackingInput.clear();
        trackingInput.sendKeys(trackingID);
        driver.findElement(By.id("searchBtn")).click();

        if (shouldFind) {
            WebElement results = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("trackingResults"))
            );
            Assert.assertTrue(results.isDisplayed(),
                "Results should show for valid tracking ID: " + trackingID);
            System.out.println("PASS: Shipment found for ID: " + trackingID);
        } else {
            WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("errorMsg"))
            );
            Assert.assertTrue(errorMsg.isDisplayed(),
                "Error should show for invalid tracking ID: " + trackingID);
            System.out.println("PASS: Correct error shown for invalid ID: " + trackingID);
        }
    }
}
