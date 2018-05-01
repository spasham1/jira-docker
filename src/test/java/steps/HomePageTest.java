package steps;

import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import pages.HomePage;
import pages.StoreLocatorPage;
import support.BrowserSetup;
import support.ConfigProperties;

/**
 * Created by SPasham on 27/04/2018.
 */

public class HomePageTest extends BrowserSetup {

    ConfigProperties config = new ConfigProperties();

    public HomePageTest() throws Exception {
        config.setParam("browser", "firefox");
        config.setParam("platform", "LINUX");
        openBrowser("10.0.75.1", config.getParam("platform"), config.getParam("browser"), "https://www.costa.co.uk/");
        new HomePage(driver).verifyHomePageLinks();
    }

    @After
    public void quit() throws Exception {
        new HomePage(driver).quit();
    }

    @Given("^I'm on costa web page$")
    public void HomePageLoaded() throws Exception {
        new HomePage(driver).homePageLoaded();
    }

    @Given("^I hover on tab (.+)$")
    public void hoverOnTab(String option) throws Exception {
        new HomePage(driver).hover(By.xpath("//a[text()='"+option+"']"));
    }

    @Then("^I get popup with further options$")
    public void wrapper() throws Exception {
        new HomePage(driver).waitForVisibilityOf(HomePage.wrapper);
    }

    @When("^I enter text in search box (.+)$")
    public void enterText(String text) throws Exception {
        new HomePage(driver).searchBoxIsPresent();
        new HomePage(driver).enterTextInSearchBox(text);
    }

    @And("^I click go button$")
    public void clickGo() throws Exception {
        new HomePage(driver).click(HomePage.goButton);
    }

    @Then("^search results are displayed$")
    public void displayResults() throws Exception {
        new StoreLocatorPage(driver).verifyStoreLocatorPage();
    }
}
