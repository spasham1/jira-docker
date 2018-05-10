package steps;

import cucumber.api.java.After;
import cucumber.api.java.en.*;
import org.openqa.selenium.By;
import pages.HomePage;
import pages.StoreLocatorPage;
import support.BrowserSetup;
import setup.ConfigProperties;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * Created by SPasham on 27/04/2018.
 */

public class BaseSteps extends BrowserSetup {

    ConfigProperties config = new ConfigProperties();

    public BaseSteps() throws Exception {
        config.setParam("browser", "firefox");
        config.setParam("platform", "WINDOWS");
        openBrowser("10.0.75.1", config.getParam("platform"), config.getParam("browser"), "https://www.costa.co.uk/");
        config.setParam("node", getNode("localhost"));
        new HomePage(driver).verifyHomePageLinks();
    }

    @Step
    @After
    public void quit() throws Exception {
        new HomePage(driver).quit();
    }

    @Step
    @Given("^I'm on costa web page$")
    public void HomePageLoaded() throws Exception {
        new HomePage(driver).homePageLoaded();
    }

    @Step
    @Given("^I hover on tab (.+)$")
    public void hoverOnTab(String option) throws Exception {
        new HomePage(driver).hover(By.xpath("//a[text()='"+option+"']"));
    }

    @Step
    @Then("^I get popup with further options$")
    public void wrapper() throws Exception {
        new HomePage(driver).waitForVisibilityOf(HomePage.wrapper);
    }

    @Step
    @When("^I enter text in search box (.+)$")
    public void enterText(String text) throws Exception {
        new HomePage(driver).searchBoxIsPresent();
        new HomePage(driver).enterTextInSearchBox(text);
    }

    @Step
    @And("^I click go button$")
    public void clickGo() throws Exception {
        new HomePage(driver).click(HomePage.goButton);
    }

    @Step
    @Then("^search results are displayed$")
    public void displayResults() throws Exception {
        new StoreLocatorPage(driver).verifyStoreLocatorPage();
    }
}
