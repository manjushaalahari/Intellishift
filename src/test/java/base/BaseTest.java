package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class BaseTest {
    public static String dest;
    public static String time;
    public static ExtentSparkReporter reporter;
    public static ExtentTest test;
    public static ExtentReports extent;
    WebDriver driver;

    @BeforeSuite
    public void config() throws IOException {
        String rootDirectory = System.getProperty("user.dir") + "\\report\\";
        FileUtils.cleanDirectory(new File(rootDirectory));
        String path = rootDirectory + "index.html";
        reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("Web Automation Results");
        reporter.config().setDocumentTitle("Test Results");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "Manjusha K");
    }


    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                String screenshotPath = takeScreenshot(driver, result.getName());
                test.log(Status.FAIL, result.getThrowable());
                test.log(Status.FAIL, "Below is the screen shot:-" + test.addScreenCaptureFromPath(screenshotPath));
                test.log(Status.FAIL, "Test Case Failed is:- " + result.getName());

            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.log(Status.PASS, "Test Case passed is:- " + result.getName());
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.log(Status.SKIP, "Test Case Skipped is:- " + result.getName());
            } else if (result.getStatus() == ITestResult.STARTED) {
                test.log(Status.INFO, "Test Case started");
            }

        } catch (Exception es) {
            System.out.println(" Report generation Exception is:- " + es.getMessage());
        }
    }

    @AfterSuite
    public void endTest() {
        extent.flush();
        driver.quit();
    }

    public WebDriver startBrowser(String url) {

        String browserName = System.getProperty("browser");
        if (browserName.equalsIgnoreCase("Firefox")) {

            SeleniumManager.getInstance().getDriverPath("geckodriver");
            driver = new FirefoxDriver();

        } else if (browserName.equalsIgnoreCase("Chrome")) {

            //SeleniumManager.getInstance().getDriverPath("chromedriver");
            System.setProperty("webdriver.chrome.driver","C:\\Manjusha\\Selenium\\chromedriver_win32_109\\chromedriver.exe");
            driver = new ChromeDriver();

        } else if (browserName.equalsIgnoreCase("Chrome_headless")) {

            //SeleniumManager.getInstance().getDriverPath("chromedriver");
            System.setProperty("webdriver.chrome.driver","C:\\Manjusha\\Selenium\\chromedriver_win32_109\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("excludeSwitches", "enable-automation");
            chromePrefs.put("useAutomationExtension", false);
            options.setHeadless(true);
            options.setExperimentalOption("prefs", chromePrefs);
            options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");

            driver = new ChromeDriver(options);
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(80));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        driver.manage().window().maximize();

        driver.get(url);
        return driver;

    }

    public void onTestStart(String messageName) {
        test = extent.createTest(messageName);
        test.log(Status.INFO, "Test Started - " + messageName);
    }

    public void addScreenshotToExtentReport(String fileName) {
        String screenshotPath = takeScreenshot(driver, fileName);
        test.addScreenCaptureFromPath(screenshotPath);
    }

    public static String takeScreenshot(WebDriver driver, String filename) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
            Date date = new Date();
            time = dateFormat.format(date);
            TakesScreenshot tc = (TakesScreenshot) driver;
            File src = tc.getScreenshotAs(OutputType.FILE);
            dest = System.getProperty("user.dir") + "\\report\\" + filename + "_" + time + ".png";

            File destination = new File(dest);
            FileUtils.copyFile(src, destination);
            System.out.println("Screen shot taken");
        } catch (Exception ex) {
            System.out.println("Screenshot error is" + ex.getMessage());
        }
        return filename + "_" + time + ".png";
    }
}
