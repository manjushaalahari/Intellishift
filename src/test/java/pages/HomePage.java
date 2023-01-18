package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.aventstack.extentreports.Status;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class HomePage extends BasePage {
    private By searchTxtBox = By.id("twotabsearchtextbox");
    private By searchSuggestions = By.xpath("//*[@id='nav-flyout-searchAjax' and contains(@style, 'display: block;')]//*[contains(@class,'suggestion-container') or contains(@class,'suggestion-header')]");
    private By submitButton = By.id("nav-search-submit-button");
    private By searchResultsTitle = By.xpath("//*[@data-component-type='s-search-results']//*[@data-component-type='s-search-result']//h2");
    private By searchResultsPrice = By.xpath("//*[@data-component-type='s-search-results']//*[@data-component-type='s-search-result']//*[@class='a-price']//*[@class='a-offscreen']");
    private By quantitySelectDD = By.id("quantity");
    private By addToCart = By.id("add-to-cart-button");
    private By gotoCart = By.id("sw-gtc");
    private By deleteBtn = By.xpath("//*[contains(@name,'submit.delete.')]");
    private By removedMsg = By.xpath("//*[@class='sc-list-item-removed-msg']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void enterInSearchBox(String textToEnter) {
    	//this.waitForElementToBePresent(this.searchTxtBox, 30);
    	//System.out.println("Element SearchTxtBox is present");
        this.enterText(this.searchTxtBox, textToEnter, "Search Box");
        try {
        	Thread.sleep(3000);
        }catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        String screenshotPath = takeScreenshot(driver, "Search_Box");
        test.log(Status.INFO, "Screen shot for Search Box result set:-" + test.addScreenCaptureFromPath(screenshotPath));
    }

    public void validateAllSearchSuggestions(String containTextToCompare) {
        List<String> difference = new ArrayList<>();
        this.waitForElementToBePresent(this.searchSuggestions, 60);
        this.waitForElementToBeClickable(this.searchSuggestions, 60);
        for(WebElement suggestion: this.findElements(this.searchSuggestions)){
            String suggestionText = suggestion.getText();
            if(!suggestionText.toLowerCase().contains(containTextToCompare.toLowerCase())){
                difference.add(suggestionText);
            }
        }

        if (difference.size() == 0) {
            test.pass("All suggestions contains " + containTextToCompare);
        } else {
            test.fail("Suggestions - " + difference.stream().collect(Collectors.joining(",")) + " are not contains" + containTextToCompare);
            Assert.fail();
        }
    }

    public void chooseLastOptionFromSuggestion() {
        List<WebElement> allSuggestions = this.findElements(this.searchSuggestions).stream().collect(Collectors.toList());
        WebElement lastSuggestion = allSuggestions.get(allSuggestions.size() - 1);
        String lastSuggestionText = lastSuggestion.getText();
        this.clickControl(lastSuggestion,  lastSuggestionText);
        test.pass("Successfully clicked on last option from suggestions '" + lastSuggestionText + "'");
    }

    public void validateTitleInSearchResults(String containTextToCompare) {
        List<String> allSearchResultsTitles = this.findElements(this.searchResultsTitle).stream().map(WebElement::getText).collect(Collectors.toList());
        List<String> difference = allSearchResultsTitles.stream().filter(ele -> !ele.toLowerCase().contains(containTextToCompare.toLowerCase())).collect(Collectors.toList());
        if (difference.size() == 0) {
            test.pass("All Search results contains " + containTextToCompare);
        } else {
            test.fail("Search results title - " + difference.stream().collect(Collectors.joining(",")) + " are not contains " + containTextToCompare);
            Assert.fail();
        }
    }

    public void clickOnLowestPriceItem() {
        List<String> allSearchResultsPrices = this.findElements(this.searchResultsPrice).stream().map(ele -> ele.getAttribute("innerHTML").replace("$", "").trim()).collect(Collectors.toList());
        List<Float> pricesInFloat = allSearchResultsPrices.stream().map(Float::valueOf).collect(Collectors.toList());
        float value = pricesInFloat.stream().min(Comparator.<Float>naturalOrder()).get();
        System.out.println("Lowest price of gel pen is - " + value);
        boolean flag = false;

        for (WebElement price : this.findElements(this.searchResultsPrice)) {
            String priceText = price.getAttribute("innerHTML").replace("$", "");
            if (priceText.equalsIgnoreCase(String.valueOf(value))) {
                this.jsClick(price, " lowest price -'" + priceText + "'");
                flag = true;
                break;
            }
        }

        Assert.assertTrue(flag, "Found lowest price gel pen and clicked");

    }

    public void addToCart(String quantityToSelect) {
    	this.waitForElementToBePresent(this.quantitySelectDD, 30);
        this.selectByValue(this.quantitySelectDD, quantityToSelect, "Quantity");
        this.waitForElementToBePresent(this.addToCart, 30);
        this.clickControl(this.addToCart, "Add To Cart");
    }

    public void emptyCart() {
        this.clickControl(this.gotoCart, "go to cart");
        this.waitForElementToBePresent(this.deleteBtn, 60);
        this.clickControl(this.deleteBtn, "Delete");

    }

    public void validateRemovedMsg() {
        String actualMsg = this.findElement(this.removedMsg).getText();
        String expectedMsg = "was removed from Shopping Cart.";
        Assert.assertTrue(actualMsg.contains(expectedMsg));
        test.pass("Successfully verified removal message from the Shopping Cart");
    }
}
