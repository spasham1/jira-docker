package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.BaseSetup;

/**
 * Created by SPasham on 27/04/2018.
 */

public class StoreLocatorPage extends BaseSetup {

    By storeLocator = By.xpath("//*[@class='store-form filter-container']");
    By mapContainer = By.xpath("//*[@class='map-container']");
    By miniMapDetails = By.xpath("//*[@class='main map-details']");
    By headerText = By.xpath("//*[text()='Showing coffee shops near you']");
    By locDetailsPane = By.id("location-details");
    By filterInput =By.id("filter-input");
    By goButton =By.id("filter-btn");

    public StoreLocatorPage(WebDriver driver) {
        super(driver);
    }

    public StoreLocatorPage verifyStoreLocatorPage() {
        waitForVisibilityOf(storeLocator);
        waitForVisibilityOf(mapContainer);
        waitForVisibilityOf(miniMapDetails);
        waitForVisibilityOf(headerText);
        waitForVisibilityOf(locDetailsPane);
    	return new StoreLocatorPage(driver);
    }
    
    public void searchBoxIsPresent() {
    	waitForVisibilityOf(filterInput);
    }
    
    public void enterTextInSearchBox(String text) throws Exception {
    	input(filterInput, text);
    }

    public void clickGoButton() {
    	click(goButton);
    }

}
