package com.zjy.webapp;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Reptile {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/Users/zjy/Downloads/chromedriver_mac_arm64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        driver.get("http://itpub    .net");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception ex) {

        }
        int p = 1;
        while (p <= 10) {
            List<WebElement> titleList = driver.findElements(By.cssSelector("div.right-box h4 a"));
            for (WebElement title : titleList) {
                log.info("title:{},href:{}", title.getText(), title.getAttribute("href"));
            }

            List<WebElement> pages = driver.findElements(By.cssSelector("div.page a"));
            WebElement nextPage = pages.get(pages.size() - 1);
            nextPage.click();
            p++;
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception ex) {

            }
        }
    }
}
