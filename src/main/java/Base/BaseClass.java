package Base;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;

public class BaseClass {
    
    public WebDriver getDriver() {
        return DriverManager.getDriver(); 
    }

    @AfterSuite
    public void tearDown() {
        DriverManager.quitDriver();
        System.out.println("BaseClass: Driver quit after all tests for " + Thread.currentThread().getId());
    }
    public static WebDriverWait createWait(WebDriver driver) {
        int explicitWaitTime = configReader.getIntProperty("explicitWait");
        return new WebDriverWait(driver, Duration.ofSeconds(explicitWaitTime));
    }
}