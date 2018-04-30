package stepdefs;

import cucumber.api.java.After;
import cucumber.api.java.en.*;
import pages.*;
import support.BrowserSetup;

/**
 * Created by SPasham on 27/04/2018.
 */

public class SearchLocator extends BrowserSetup {

    public SearchLocator() throws Exception {
        //openBrowser("chrome", "https://www.costa.co.uk/");
        //new HomePage(driver).verifyHomePageLinks();
    }

    @After
    public void quit() throws Exception {
        new HomePage(driver).quit();
    }

    @Given("^user navigates to locations tab$")
    public void navigateToLocations() throws Exception {
        new HomePage(driver).searchBoxIsPresent();
    }

    /*@When("^I enter text in search box (.+)$")
    public void enterText(String text) throws Exception {
        new HomePage(driver).enterTextInSearchBox(text);
    }

    @Then("^search results are displayed$")
    public void displayResults() throws Exception {
        new StoreLocatorPage(driver).verifyStoreLocatorPage();
    }*/

    @When("^I select search suggestion link (.+)$")
    public void selectSuggestionLink(int search_link) throws Exception {
        new HomePage(driver).selectLink(search_link);
    }

    @Then("^I can see search results on page (.+)$")
    public void displaySearchResults(String search_result) throws Throwable {
        new SearchResultsPage(driver).verifySearchResults(search_result);
    }

    @Given("^page contains search results for (.+)$")
    public void page_contains_search_results(String search_result) throws Throwable {
        //enterText(search_result);
        selectSuggestionLink(1);
        displaySearchResults(search_result);
    }

    @When("^I set filter (.+) with value (.+)$")
    public void setFilterBrand(String brand, String brandName) throws Exception {
        new SearchResultsPage(driver).setFilter(brand, brandName);
        new SearchResultsPage(driver).click(SearchResultsPage.filterButton);
    }

    @Then("^I can see product (.+) with part_number (.+)$")
    public void assertProductAndPartNumber(String product, String part_number) {
        new SearchResultsPage(driver).verifyProductDescriptionLink(product);
        new SearchResultsPage(driver).verifyProductPartNumber(part_number);
    }

    @And("^product count is (.+)$")
    public void assertProductCount(int count) {
        new SearchResultsPage(driver).verifyProductCount(count);
    }

    @When("^I add product (.+) with quantity (.+) to basket$")
    public void addProductToBasket(String product, int quantity) throws Exception{
        new SearchResultsPage(driver).addProductToBasket(product, quantity);
    }

    @And("^I click checkout basket and confirm")
    public void clickCheckOut() {
        //new HomePage(driver).click(HomePage.basket);
        //new HomePage(driver).click(HomePage.checkout);
    }

    @Then("^I get login popup$")
    public void assertLoginPopup() throws Exception{
        //new HomePage(driver).waitForVisibilityOf(HomePage.loginDialog);
    }

    @When("^I add product to basket$")
    public void addProductToBasket() throws Exception{
        //new HomePage(driver).click(HomePage.addToBasket);
        //new HomePage(driver).waitForVisibilityOf(HomePage.viewBasketButton);
    }

}
