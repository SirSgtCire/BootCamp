package com.learning;

import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world! I am born!");

        // Create chromedriver instance using WebDriverManager
        // TODO: parameterize browser arguments
        WebDriver driver = WebDriverManager.chromedriver().capabilities(
                new ChromeOptions().addArguments("--remote-allow-origins=*")).create();

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

        // Cleanup step
        driver.close();
    }
}