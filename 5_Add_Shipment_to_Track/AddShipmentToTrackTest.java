package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * AddShipmentToTrackTest.java
 * TC_016 - Add a new shipment with valid details
 * TC_017 - Verify shipment lifecycle status transitions
 * TC_018 - Verify duplicate shipment creation is rejected
 * TC_019 - Add shipment with missing required fields shows validation
 */
public class AddShipmentToTrackTest extends BaseTest {

    private static final String LOGIN_URL    = "https://www.logistics-tracking-demo.com/login";
    private static final String SHIPMENT_URL = "https://www.logistics-tracking-demo.com/shipments/create";

    @BeforeMethod
    public void loginAndNavigate() {
        driver.get(LOGIN_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("admin@logistics.com");
        driver.findElement(By.id("password")).sendKeys("Admin@123");
        driver.findElement(By.id("loginBtn")).click();
        wait.until(ExpectedConditions.urlContains("dashboard"));
        driver.get(SHIPMENT_URL);
    }

    @Test(priority = 1, description = "TC_016 - Create a New Shipment with Valid Details")
    public void createNewShipmentWithValidDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Fill sender details
        WebElement senderName = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("senderName"))
        );
        senderName.clear();
        senderName.sendKeys("Rajesh Kumar");

        driver.findElement(By.id("senderAddress")).sendKeys("123 Main Street, Mumbai - 400001");
        driver.findElement(By.id("senderPhone")).sendKeys("9876543210");

        // Fill receiver details
        driver.findElement(By.id("receiverName")).sendKeys("Priya Sharma");
        driver.findElement(By.id("receiverAddress")).sendKeys("456 Park Avenue, Delhi - 110001");
        driver.findElement(By.id("receiverPhone")).sendKeys("9123456789");

        // Fill shipment details
        driver.findElement(By.id("shipmentWeight")).sendKeys("2.5");

        Select shipmentType = new Select(driver.findElement(By.id("shipmentType")));
        shipmentType.selectByVisibleText("Standard");

        driver.findElement(By.id("createShipmentBtn")).click();
        System.out.println("Clicked Create Shipment button");

        // Verify success message
        WebElement successMsg = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("successMessage"))
        );
        Assert.assertTrue(successMsg.isDisplayed(), "Success message should appear after shipment creation");

        String successText = successMsg.getText();
        System.out.println("Success message: " + successText);
        Assert.assertTrue(
            successText.toLowerCase().contains("created") || successText.toLowerCase().contains("success"),
            "Success message should confirm shipment was created"
        );

        System.out.println("PASS: New shipment created successfully.");
    }

    @Test(priority = 2, description = "TC_017 - Verify Shipment Status is 'Created' After Creation")
    public void verifyShipmentStatusAfterCreation() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Create shipment
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("senderName"))).sendKeys("Test Sender");
        driver.findElement(By.id("senderAddress")).sendKeys("Test Address, Chennai - 600001");
        driver.findElement(By.id("senderPhone")).sendKeys("9000000001");
        driver.findElement(By.id("receiverName")).sendKeys("Test Receiver");
        driver.findElement(By.id("receiverAddress")).sendKeys("Test Dest, Hyderabad - 500001");
        driver.findElement(By.id("receiverPhone")).sendKeys("9000000002");
        driver.findElement(By.id("shipmentWeight")).sendKeys("1.0");
        new Select(driver.findElement(By.id("shipmentType"))).selectByVisibleText("Express");
        driver.findElement(By.id("createShipmentBtn")).click();

        // Navigate to tracking
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewTrackingLink"))).click();

        WebElement statusBadge = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("shipmentStatus"))
        );
        String status = statusBadge.getText().trim();
        System.out.println("Initial shipment status: " + status);

        Assert.assertEquals(status, "Created",
            "Initial shipment status should be 'Created'");

        System.out.println("PASS: Shipment status correctly set to 'Created' after creation.");
    }

    @Test(priority = 3, description = "TC_018 - Verify Duplicate Shipment Creation Is Rejected")
    public void verifyDuplicateShipmentRejected() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String duplicateRef = "DUPREF001";

        // Create first shipment
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("senderName"))).sendKeys("Dup Sender");
        driver.findElement(By.id("referenceNumber")).sendKeys(duplicateRef);
        driver.findElement(By.id("senderAddress")).sendKeys("Dup Address, Pune - 411001");
        driver.findElement(By.id("senderPhone")).sendKeys("9001112222");
        driver.findElement(By.id("receiverName")).sendKeys("Dup Receiver");
        driver.findElement(By.id("receiverAddress")).sendKeys("Dup Dest, Bangalore - 560001");
        driver.findElement(By.id("receiverPhone")).sendKeys("9002223333");
        driver.findElement(By.id("shipmentWeight")).sendKeys("0.5");
        driver.findElement(By.id("createShipmentBtn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successMessage")));

        // Try creating duplicate
        driver.get(SHIPMENT_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("senderName"))).sendKeys("Dup Sender");
        driver.findElement(By.id("referenceNumber")).sendKeys(duplicateRef);
        driver.findElement(By.id("senderAddress")).sendKeys("Dup Address, Pune - 411001");
        driver.findElement(By.id("senderPhone")).sendKeys("9001112222");
        driver.findElement(By.id("receiverName")).sendKeys("Dup Receiver");
        driver.findElement(By.id("receiverAddress")).sendKeys("Dup Dest, Bangalore - 560001");
        driver.findElement(By.id("receiverPhone")).sendKeys("9002223333");
        driver.findElement(By.id("shipmentWeight")).sendKeys("0.5");
        driver.findElement(By.id("createShipmentBtn")).click();

        WebElement dupError = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("duplicateError"))
        );
        Assert.assertTrue(dupError.isDisplayed(), "Duplicate shipment error should be shown");

        System.out.println("PASS: Duplicate shipment creation correctly rejected.");
    }

    @Test(priority = 4, description = "TC_019 - Create Shipment Without Required Fields Shows Validation")
    public void createShipmentWithMissingRequiredFields() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Click submit without filling any field
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("createShipmentBtn"))).click();

        // Verify validation messages
        WebElement validationSenderName = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("senderNameError"))
        );
        Assert.assertTrue(validationSenderName.isDisplayed(),
            "Validation error for Sender Name should appear");

        System.out.println("PASS: Validation shown for missing required fields.");
    }
}
