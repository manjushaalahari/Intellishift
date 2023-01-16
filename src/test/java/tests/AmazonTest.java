package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.HomePage;

public class AmazonTest extends BaseTest {
    HomePage homePage;

    @BeforeClass
    public void launchApp()
    {
        WebDriver driver=startBrowser("https://www.amazon.com/");
        homePage = PageFactory.initElements(driver, HomePage.class);

    }
    @Test(description="This test case searches for gel pens on Amazon.com and chooses the lowest priced pen")
    
    public void shopping_cart() {
            onTestStart("Shopping Cart");

            homePage.enterInSearchBox("gel pens");
            homePage.validateAllSearchSuggestions("gel pens");

            homePage.chooseLastOptionFromSuggestion();
            homePage.validateTitleInSearchResults("Pen");
            homePage.clickOnLowestPriceItem();

            homePage.addToCart("2");
            homePage.emptyCart();
            homePage.validateRemovedMsg();
    }
}
