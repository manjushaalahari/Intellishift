package pages;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage extends BaseTest {
    WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickControl(By elementPropertyBy, String elementName) {
        driver.findElement(elementPropertyBy).click();
        test.info("Successfully clicked on  '" + elementName + "'");
    }

    public void clickControl(WebElement element, String elementName) {
        element.click();
        test.info("Successfully clicked on '" + elementName + "'");
    }

    public void jsClick(WebElement element, String elementName) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
        test.info("Successfully clicked on '" + elementName + "'");
    }

    public void enterText(By elementPropertyBy, String textToEnter, String elementName) {
        driver.findElement(elementPropertyBy).sendKeys(textToEnter);
        test.info("'" + textToEnter + "' entered in the field '" + elementName + "'");
    }

    public List<WebElement> findElements(By elementPropertyBy) {
        waitForElementToBePresent(elementPropertyBy, 60);
        List<WebElement> allElements = driver.findElements(elementPropertyBy);
        return allElements;
    }

    public WebElement findElement(By elementProperty) {
        waitForElementToBePresent(elementProperty, 60);
        WebElement element = driver.findElement(elementProperty);
        return element;
    }

    public void selectByValue(By elementPropertyBy, String valueToSelect, String elementName) {
        Select select = new Select(driver.findElement(elementPropertyBy));
        select.selectByValue(valueToSelect);
        test.info("'" + valueToSelect + "' units is selected from dropdown for element - '" + elementName + "'");
    }

    public void waitForElementToBePresent(By elementPropertyBy, int waitTimeInSec) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(waitTimeInSec));
        w.until(ExpectedConditions.presenceOfElementLocated(elementPropertyBy));
    }

    public void waitForElementToBeClickable(By elementPropertyBy, int waitTimeInSec){
        WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(waitTimeInSec));
        // elementToBeClickable expected criteria
        wt.until(ExpectedConditions.elementToBeClickable (elementPropertyBy));
    }

}
