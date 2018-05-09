package steps;

import org.openqa.selenium.By;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.HomePage;
import pages.StoreLocatorPage;
import support.BrowserSetup;
import setup.ConfigProperties;

import java.lang.reflect.Method;

/**
 * Created by SPasham on 27/04/2018.
 */

public class HomePageTest extends BrowserSetup {

    ConfigProperties config = new ConfigProperties();

    String testName;

    @BeforeMethod
    public void printMethodName(Method method) {
        testName = method.getName();
        System.out.println(System.lineSeparator()+"***Running TestCase: "+testName);
    }

    public HomePageTest() throws Exception {
        config.setParam("browser", "firefox");
        config.setParam("platform", "WINDOWS");
        openBrowser("10.0.75.1", config.getParam("platform"), config.getParam("browser"), "https://www.costa.co.uk/");
        config.setParam("node", getNode("10.0.75.1"));
        new HomePage(driver).verifyHomePageLinks();
    }

    @AfterClass
    public void quit() throws Exception {
        new HomePage(driver).quit();
    }

    @Test(priority=1)
    public void tab_responsibilty() throws Exception {
        verify_wrapper("Responsibility");
    }

    @Test(priority=2)
    public void tab_coffeeClub() throws Exception {
        verify_wrapper("Coffee Club");
    }

    @Test (priority=3)
    public void searchLocation() throws Exception {
        verify_wrapper("Locations");
        new HomePage(driver).searchBoxIsPresent();
        new HomePage(driver).enterTextInSearchBox("holborn");
        new HomePage(driver).click(HomePage.goButton);
        new StoreLocatorPage(driver).verifyStoreLocatorPage();
    }

    private void verify_wrapper(String tab) {
        new HomePage(driver).homePageLoaded();
        new HomePage(driver).hover(By.xpath("//a[text()='"+tab+"']"));
        new HomePage(driver).waitForVisibilityOf(HomePage.wrapper);
    }

    @AfterMethod
    public void testSetup(ITestResult result) throws Exception {
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println(System.lineSeparator()+result.getName()+" -- FAILED!");
            new HomePage(driver).screenCapture();
        }
    }
}
