package com.learning;

import org.openqa.selenium.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        /**
         * DAY 1: project creation
         */
        System.out.println("Hello world! I am born!");

        /**
         *  DAY 2: launching browsers
         */
        // Create chromedriver instance using WebDriverManager
        // TODO: parameterize browser arguments
        WebDriver driver = WebDriverManager.chromedriver().capabilities(
                new ChromeOptions().addArguments("--remote-allow-origins=*")).create();

        // Configuration to smooth out issues with website testing
        driver.manage().window().maximize();

        // Navigate to google home and grab title of page
        // TODO: parameterize url
        driver.get("https://www.google.com");
        String actualTitle = driver.getTitle();

        // If we actually navigated to google.com, then the title should be "Google"
        // TODO: parameterize checks
        if (actualTitle.contains("Google")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", actualTitle);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", actualTitle);
        }

        /**
         *  DAY 3: search Yahoo
         */
        // Find Google search box by id
        // TODO: Parameterize element id
        WebElement myElement = driver.findElement(By.id("APjFqb"));
        // Find Google search box by xpath
        WebElement anElement = driver.findElement(By.xpath("//textarea[@id='APjFqb']"));

        // Use both elements to complete Yahoo search
        // TODO: Parameterize sendable keys
        myElement.sendKeys("Yahoo");
        anElement.sendKeys(Keys.ENTER);

        // Verify we are searching for Yahoo
        String yahooSearch = driver.getTitle();
        System.out.format("Do we leave the previous page? %s\n", yahooSearch);

        // If we actually hit enter, the title should be "Google Search"
        // TODO: parameterize checks
        if (yahooSearch.contains("Google Search")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", yahooSearch);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", yahooSearch);
        }

        // Reset the browser by re-getting Google
        // TODO: parameterize url
        driver.get("https://www.google.com");
        String returnTitle = driver.getTitle();

        // If we actually navigated to google.com, then the title should be "Google"
        // TODO: parameterize checks
        if (returnTitle.contains("Google")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", returnTitle);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", returnTitle);
        }

        // Find Google search box by xpath
        // TODO: Parameterize xpath variable
        WebElement ohElement = driver.findElement(By.xpath("//textarea[@id='APjFqb']"));

        // Replace sendKeys functions using Javascript executor
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("document.getElementById('APjFqb').value='Yahoo';");

        // Hit enter
        ohElement.sendKeys(Keys.ENTER);

        // Verify we are searching for Yahoo AGAIN
        String yahooSearch2 = driver.getTitle();
        System.out.format("Do we leave the previous page? %s\n", yahooSearch2);

        // If we actually hit enter, the title should be "Google Search"
        // TODO: parameterize checks
        if (yahooSearch2.contains("Google Search")) {
            System.out.format("We passed AGAIN, yay!, just to be sure: %s\n", yahooSearch2);
        } else {
            System.out.format("We failed AGAIN, darn, here's what we got: %s\n", yahooSearch2);
        }

        /**
         *  Day 4: navigate webpages
         */
        // TODO: parameterize url
        driver.get(" https://sqeautomation.wordpress.com/sample-html-page/");
        String testTitle = driver.getTitle();

        // If we actually navigated to our test page, then the title should be ""
        // TODO: parameterize checks
        if (testTitle.contains("Sample HTML Page – SQEAutomation")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", testTitle);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", testTitle);
        }

        // Cleanup step
        driver.close();
    }
}