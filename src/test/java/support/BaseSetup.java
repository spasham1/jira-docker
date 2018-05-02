package support;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

public class BaseSetup {

    protected WebDriver driver;
    protected WebElement element;
    protected int timeout =15;

    public BaseSetup(WebDriver driver) {
        this.driver = driver;
    }

	@Step
	public void quit() throws Exception {
		if(driver!=null)
			driver.quit();
	}

	@Step
    public void waitForVisibilityOf(By locator) {
    	try {
    		WebDriverWait wait = new WebDriverWait(driver, timeout);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    	} catch (Exception e) {
    		Assert.fail("Element '"+locator+"' is not visible in "+timeout+" seconds");
    	}
    }

	@Step
    public WebElement findElement(By locator) {
    	try{
    	element = new WebDriverWait(driver, timeout)
    			.until(ExpectedConditions.presenceOfElementLocated(locator)); 
    	} catch (Exception e) {
    		Assert.fail("Failed to find element '"+locator+"' in "+timeout+" seconds");
    	}
    	return element;
    }

	@Step
	public boolean isElementPresent(By locator) {
		element=findElement(locator);
		if(element.isDisplayed()==true)
			return true;
		else
			return false;
	}

	@Step
	public void hover(By locator) {
		element = findElement(locator);
		new Actions(driver).moveToElement(element).perform();
	}

	@Step
    public void click(By locator) {
    	element = findElement(locator);
    	element.click();
    }

	@Step
    public void input(By locator, String text) throws Exception {
    	click(locator);
    	element.clear();
    	element.sendKeys(text);
    }

	@Step
    public void pageShouldContainTitle(String title) {
    	if(!driver.getTitle().contains(title))
    		Assert.fail("Page does not contain title " +title);
    }

	@Step
    public void pageShouldContainText(String text) {
		waitForVisibilityOf(By.xpath("//*[contains(., '" + text + "')]"));
	}

	@Attachment
	public byte[] screenCapture() {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

}