package com.learning;

import com.google.common.io.Files;
import org.openqa.selenium.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

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
        WebDriver driver = WebDriverManager.chromedriver().capabilities(new ChromeOptions()
                .addArguments("--remote-allow-origins=*")
                .addArguments("--disable-notifications")
                .addArguments("--disable-extensions")
                .addArguments("--disable-infobars")).create();

        // Configuration to smooth out issues with website testing
        driver.manage().window().maximize();

        // Navigate to google home and grab title of page
        driver.get("https://www.google.com");
        String actualTitle = driver.getTitle();

        // If we actually navigated to google.com, then the title should be "Google"
        if (actualTitle.contains("Google")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", actualTitle);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", actualTitle);
        }

        /**
         *  DAY 3: search Yahoo
         */
        // Find Google search box by id
        WebElement myElement = driver.findElement(By.id("APjFqb"));
        // Find Google search box by xpath
        WebElement anElement = driver.findElement(By.xpath("//textarea[@id='APjFqb']"));

        // Use both elements to complete Yahoo search
        myElement.sendKeys("Yahoo");
        anElement.sendKeys(Keys.ENTER);

        // Verify we are searching for Yahoo
        String yahooSearch = driver.getTitle();
        System.out.format("Do we leave the previous page? %s\n", yahooSearch);

        // If we actually hit enter, the title should be "Google Search"
        if (yahooSearch.contains("Google Search")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", yahooSearch);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", yahooSearch);
        }

        // Reset the browser by re-getting Google
        driver.get("https://www.google.com");
        String returnTitle = driver.getTitle();

        // If we actually navigated to google.com, then the title should be "Google"
        if (returnTitle.contains("Google")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", returnTitle);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", returnTitle);
        }

        // Find Google search box by xpath
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
        if (yahooSearch2.contains("Google Search")) {
            System.out.format("We passed AGAIN, yay!, just to be sure: %s\n", yahooSearch2);
        } else {
            System.out.format("We failed AGAIN, darn, here's what we got: %s\n", yahooSearch2);
        }

        /**
         *  Day 4: navigate webpages
         */
        driver.get("https://sqeautomation.wordpress.com/sample-html-page/");
        String testTitle = driver.getTitle();

        // If we actually navigated to our test page, then the title should be "Sample HTML Page – SQEAutomation"
        if (testTitle.contains("Sample HTML Page – SQEAutomation")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", testTitle);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", testTitle);
        }

        // Find and select the dropdown element
        List<WebElement> listOfElements = driver.findElements(By.xpath("//ul[@id='g11-text-1-menu']"));

        for (WebElement webElement : listOfElements) {
            if (webElement.getText().trim().equals("option3")) {
                webElement.click();
            }
        }

        // Find and select the email field
        WebElement emailField = driver.findElement(By.id("g11-email"));
        emailField.sendKeys("example@email.com");

        // Find and select the website field
        WebElement websiteField = driver.findElement(By.id("g11-website"));
        websiteField.sendKeys("https://www.example.com");

        /**
         *  Day 5: handle ads and popups
         */
        // Wait for the cookie consent banner to be present and accept cookie law
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement waitForCookie = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eu-cookie-law")));
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='bottom-sticky__ad-container']/div[1]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='eu-cookie-law']/form/input"))).click();

        // Find and select the submit button
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait2.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='contact-form-11']/form/p[1]/button"))).click();

        String submitTitle = driver.getTitle();
        System.out.format("What is our title? %s\n", submitTitle);

        // If we actually navigated to our test page, then the title should be "Sample HTML Page – SQEAutomation"
        if (submitTitle.contains("Sample HTML Page – SQEAutomation")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", submitTitle);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", submitTitle);
        }

        // Do we find the "Go Back" <p> link?
        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement goBack = wait3.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='contact-form-11']/div/p[1]/a")));
        String goBackText = goBack.getText();

        // If we can identify the text of the "Go Back" <p> link, then we know it generated on the page
        if (goBackText.contains("Go back")) {
            System.out.format("We passed, yay!, just to be sure: %s\n", goBackText);
        } else {
            System.out.format("We failed, darn, here's what we got: %s\n", goBackText);
        }

        /**
         *  Day 6: screenshots of webpages
         */
        // navigate to Reddit homepage
        driver.get("https://www.reddit.com");
        TakesScreenshot scrShot = ((TakesScreenshot) driver);
        File scrShotSrc = scrShot.getScreenshotAs(OutputType.FILE);
        File screenShot = new File("screenshot.png");
        try {
            Files.copy(scrShotSrc, screenShot);
        } catch (Exception e) {
            System.out.format("We failed to save our screenshot, here's the error:\n%s\n", e);
        }


        /**
         *  Day 7:
         */
        // TODO: continue lessons from here

        // Cleanup step
        driver.close();
        driver.quit();
    }
}