package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * BaseTest.java
 * Base class for all test classes.
 * Handles WebDriver initialization and teardown using TestNG annotations.
 */
public class BaseTest {

    protected WebDriver driver;

    // Base URL of the Logistics Tracking Portal
    protected static final String BASE_URL = "https://www.logistics-tracking-demo.com";

    @BeforeMethod
    public void setUp() {
        // Automatically manages ChromeDriver binary
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();

        System.out.println("=== Browser launched successfully ===");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("=== Browser closed successfully ===");
        }
    }

    /**
     * Navigate to the application base URL
     */
    public void openApplication() {
        driver.get(BASE_URL);
        System.out.println("Navigated to: " + BASE_URL);
    }

    /**
     * Navigate to a specific URL
     */
    public void openURL(String url) {
        driver.get(url);
        System.out.println("Navigated to: " + url);
    }
}
