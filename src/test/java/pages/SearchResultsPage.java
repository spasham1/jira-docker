package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.*;

/**
 * Created by SPasham on 27/04/2018.
 */

public class SearchResultsPage extends BaseSetup {

    By productCount = By.xpath("//*[@id='bread-box']//span[@ng-bind='facetFilters.productCount']");
    public static By filterButton = By.xpath("(//button[text()='Apply filters'])[2]");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public void verifySearchResults(String search_result) {
    	pageShouldContainTitle(search_result);
    	pageShouldContainText(search_result);
    }

    public void verifyProductDescriptionLink(String text) {
        waitForVisibilityOf(By.linkText(text));
    }

    public void verifyProductPartNumber(String part) {
        waitForVisibilityOf(By.xpath("//span[@class='text-contents' and text()='"+part+"']"));
    }

    public void setFilter(String type, String value) throws Exception {
        click(By.xpath("//a[@class='filter' and contains(., '"+type+"')]"));
        click(By.xpath("//input[@type='checkbox']/following-sibling::span[contains(., '"+value+"')]"));
    }

    public void verifyProductCount(int count) {
        String products = Integer.toString(count);
        String product = findElement(productCount).getText();
        Assert.assertEquals(products, product);
    }

    public void addProductToBasket(String product, int quantity) throws Exception {
        if(quantity>1)
            input(By.xpath("//*[text()='"+product+"']/../../following-sibling::td[@class='priceCol']//input"), Integer.toString(quantity));
        click(By.xpath("//*[text()='"+product+"']/../../following-sibling::td[@class='priceCol']//button"));
    }

}
