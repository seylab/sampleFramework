package com.sample.utilities;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.HashMap;

public class Driver {
    private static WebDriver driver;

    private Driver() {
        // Prevent instantiation
    }

    public static WebDriver get() {
        if (driver == null) {
            String browser = System.getProperty("browser", ConfigurationReader.get("browser"));
            driver = createDriver(browser);
        }
        return driver;
    }

    private static WebDriver createDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                return initializeChromeDriver(false);
            case "chrome-headless":
                return initializeChromeDriver(true);
            case "firefox":
                return new FirefoxDriver();
            case "firefox-headless":
                return new FirefoxDriver(new FirefoxOptions().addArguments("--headless"));
            case "ie":
                return initializeInternetExplorerDriver();
            case "edge":
                return initializeEdgeDriver();
            case "safari":
                return initializeSafariDriver();
            default:
                throw new WebDriverException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver initializeChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--disable-features=OptimizationGuideModelDownloading,OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("prefs", getChromePreferences());

        if (headless) {
            options.addArguments("--headless=new");
        }

        return new ChromeDriver(options);
    }

    private static HashMap<String, Object> getChromePreferences() {
        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        return prefs;
    }

    private static WebDriver initializeInternetExplorerDriver() {
        if (!isWindowsOS()) {
            throw new WebDriverException("Your OS doesn't support Internet Explorer");
        }
        return new InternetExplorerDriver();
    }

    private static WebDriver initializeEdgeDriver() {
        if (!isWindowsOS()) {
            throw new WebDriverException("Your OS doesn't support Edge");
        }
        return new EdgeDriver();
    }

    private static WebDriver initializeSafariDriver() {
        if (!isMacOS()) {
            throw new WebDriverException("Your OS doesn't support Safari");
        }
        return new SafariDriver();
    }

    private static boolean isWindowsOS() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    private static boolean isMacOS() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
