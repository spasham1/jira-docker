package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import support.*;

/**
 * Created by SPasham on 27/04/2018.
 */

public class HomePage extends BaseSetup {

    By searchAddress = By.id("txtHeaderSearchAddress");
    By registerNow = By.linkText("Register now");
    By login = By.linkText("LOGIN HERE");
    public static By acceptCookie = By.xpath("//a[@aria-label='dismiss cookie message']");
    public static By goButton = By.id("btn-go");
    public static By wrapper = By.xpath("//*[contains(@class, 'meganav mn') and @style='visibility: visible; display: block;']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage verifyHomePageLinks() {
        acceptCookie();
    	String[] tabs = {"Our Coffees", "Locations", "Coffee Club", "Responsibility", "About Us"};
    	for(String tab: tabs) {
    		waitForVisibilityOf(By.xpath("//a[text()='"+tab+"']"));
    	}
    	return new HomePage(driver);
    }

    public void homePageLoaded() {
        pageShouldContainTitle("The Nation's Favourite Coffee Shop | Costa Coffee");
    }
    
    public void searchBoxIsPresent() {
    	waitForVisibilityOf(searchAddress);
    }
    
    public void enterTextInSearchBox(String text) throws Exception {
    	input(searchAddress, text);
    }

    public void selectLink(int linkItem) {
    	int link = linkItem+1;
    	click(By.xpath("//*[@id='txtHint']/li["+link+"]"));
    }

    public void acceptCookie() {
        if(isElementPresent(acceptCookie)==true)
            click(acceptCookie);
    }

}
