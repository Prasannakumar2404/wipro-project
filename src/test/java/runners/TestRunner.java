package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import Base.DriverManager;

@CucumberOptions(
    features = "src/test/resources/features/registration.feature",
    glue = "stepdefinitions",
    plugin = {
        "pretty", 
        "html:target/cucumber-reports.html",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" 
    }
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @BeforeClass
    @Parameters("browser")
    public void setUpClass(@Optional("chrome") String browser) {
        System.out.println("TestRunner: Setting browser to " + browser + " for thread " + Thread.currentThread().getId());
        DriverManager.setBrowserType(browser); 
    }
}