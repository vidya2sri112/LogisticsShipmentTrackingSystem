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
 * CheckoutWithValidAddressTest.java
 * TC_027 - Enter valid delivery address for shipment checkout
 * TC_028 - Verify delivery confirmation is shown after checkout
 * TC_029 - Verify shipment status changes to 'Out for Delivery' after checkout
 * TC_030 - Validate incorrect delivery address is rejected
 */
public class CheckoutWithValidAddressTest extends BaseTest {

    private static final String LOGIN_URL    = "https://www.logistics-tracking-demo.com/login";
    private static final String CHECKOUT_URL = "https://www.logistics-tracking-demo.com/shipments/checkout";

    @BeforeMethod
    public void loginBeforeCheckout() {
        driver.get(LOGIN_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("admin@logistics.com");
        driver.findElement(By.id("password")).sendKeys("Admin@123");
        driver.findElement(By.id("loginBtn")).click();
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }

    @Test(priority = 1, description = "TC_027 - Enter Valid Delivery Address for Checkout")
    public void checkoutWithValidDeliveryAddress() {
        driver.get(CHECKOUT_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Fill delivery address form
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipientName")))
            .sendKeys("Ananya Verma");

        driver.findElement(By.id("streetAddress")).sendKeys("789 Lake View Road");
        driver.findElement(By.id("city")).sendKeys("Hyderabad");
        driver.findElement(By.id("state")).sendKeys("Telangana");
        driver.findElement(By.id("pinCode")).sendKeys("500081");
        driver.findElement(By.id("contactNumber")).sendKeys("9988776655");

        // Select delivery time slot
        Select timeSlot = new Select(driver.findElement(By.id("deliverySlot")));
        timeSlot.selectByVisibleText("10:00 AM - 12:00 PM");

        driver.findElement(By.id("confirmDeliveryBtn")).click();
        System.out.println("Clicked Confirm Delivery button");

        // Verify success
        WebElement successBanner = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("deliveryConfirmation"))
        );
        Assert.assertTrue(successBanner.isDisplayed(), "Delivery confirmation banner should appear");

        String confirmText = successBanner.getText();
        System.out.println("Confirmation: " + confirmText);
        Assert.assertTrue(
            confirmText.toLowerCase().contains("confirmed") || confirmText.toLowerCase().contains("success"),
            "Confirmation text should indicate success"
        );

        System.out.println("PASS: Checkout completed successfully with valid delivery address.");
    }

    @Test(priority = 2, description = "TC_028 - Verify Delivery Confirmation Details Are Correct")
    public void verifyDeliveryConfirmationDetails() {
        driver.get(CHECKOUT_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String expectedRecipient = "Karthik Nair";
        String expectedPinCode   = "600002";

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipientName")))
            .sendKeys(expectedRecipient);
        driver.findElement(By.id("streetAddress")).sendKeys("12 Marina Beach Road");
        driver.findElement(By.id("city")).sendKeys("Chennai");
        driver.findElement(By.id("state")).sendKeys("Tamil Nadu");
        driver.findElement(By.id("pinCode")).sendKeys(expectedPinCode);
        driver.findElement(By.id("contactNumber")).sendKeys("9876001122");
        new Select(driver.findElement(By.id("deliverySlot"))).selectByVisibleText("2:00 PM - 4:00 PM");
        driver.findElement(By.id("confirmDeliveryBtn")).click();

        // Verify confirmation shows correct recipient
        WebElement confirmedRecipient = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("confirmedRecipient"))
        );
        Assert.assertTrue(confirmedRecipient.getText().contains(expectedRecipient),
            "Confirmation should show correct recipient name");

        WebElement confirmedPin = driver.findElement(By.id("confirmedPinCode"));
        Assert.assertTrue(confirmedPin.getText().contains(expectedPinCode),
            "Confirmation should show correct pin code");

        System.out.println("PASS: Delivery confirmation details verified correctly.");
    }

    @Test(priority = 3, description = "TC_029 - Verify Shipment Status Changes After Delivery Checkout")
    public void verifyShipmentStatusAfterCheckout() {
        driver.get(CHECKOUT_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipientName")))
            .sendKeys("Status Test Receiver");
        driver.findElement(By.id("streetAddress")).sendKeys("50 Tech Park, Whitefield");
        driver.findElement(By.id("city")).sendKeys("Bangalore");
        driver.findElement(By.id("state")).sendKeys("Karnataka");
        driver.findElement(By.id("pinCode")).sendKeys("560066");
        driver.findElement(By.id("contactNumber")).sendKeys("9000111222");
        new Select(driver.findElement(By.id("deliverySlot"))).selectByVisibleText("6:00 PM - 8:00 PM");
        driver.findElement(By.id("confirmDeliveryBtn")).click();

        // Navigate to track the shipment
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("viewTrackingLink"))).click();

        WebElement statusBadge = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("shipmentStatus"))
        );
        String status = statusBadge.getText().trim();
        System.out.println("Shipment status after checkout: " + status);

        Assert.assertEquals(status, "Out for Delivery",
            "Status should change to 'Out for Delivery' after checkout");

        System.out.println("PASS: Shipment status correctly updated to 'Out for Delivery'.");
    }

    @Test(priority = 4, description = "TC_030 - Validate Incorrect Delivery Address Is Rejected")
    public void validateIncorrectDeliveryAddressRejected() {
        driver.get(CHECKOUT_URL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Enter invalid pin code
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipientName")))
            .sendKeys("Invalid Address Test");
        driver.findElement(By.id("streetAddress")).sendKeys("XYZ Street");
        driver.findElement(By.id("city")).sendKeys("InvalidCity");
        driver.findElement(By.id("state")).sendKeys("InvalidState");
        driver.findElement(By.id("pinCode")).sendKeys("000000"); // Invalid pin
        driver.findElement(By.id("contactNumber")).sendKeys("1234567890");
        driver.findElement(By.id("confirmDeliveryBtn")).click();

        // Verify error message
        WebElement pinError = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("pinCodeError"))
        );
        Assert.assertTrue(pinError.isDisplayed(), "Pin code error should be shown for invalid pin");

        System.out.println("PASS: Invalid delivery address correctly rejected.");
    }
}
