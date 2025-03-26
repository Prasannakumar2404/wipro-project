package Base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<String> browserType = new ThreadLocal<>();

    public static void setBrowserType(String browser) {
        browserType.set(browser);
    }

    public static void initializeDriver() {
        if (driver.get() == null) {
            String browser = getBrowserType();
            if (browser == null) {
                throw new IllegalStateException("Browser type not set for this thread!");
            }
            WebDriver webDriver;
            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver();
            } else if (browser.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            driver.set(webDriver);
            
            webDriver.get(configReader.getProperty("url")); 
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static String getBrowserType() {
        return browserType.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}