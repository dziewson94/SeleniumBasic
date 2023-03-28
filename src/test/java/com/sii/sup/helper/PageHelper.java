package com.sii.sup.helper;

import com.sii.sup.tests.base.Attributes;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;


public class PageHelper {
    private static final Logger logger = LoggerFactory.getLogger(PageHelper.class.getSimpleName());
    private final WebDriver webDriver;

    PageHelper(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void init(String url) {
        webDriver.get(url);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public WebElement findElementById(String id) {
        return webDriver.
                findElement(By.id(id));
    }

    public String getPageSource() {
        return webDriver.getPageSource();
    }

    public List<WebElement> findChildElementsByTag(String tag, WebElement element) {
        return element.
                findElements(By.tagName(tag));
    }

    public String getNewestWindowHandle(String currentWindowHandle, Set<String> allWindowHandles) {
        return allWindowHandles.stream().filter(handle -> (!handle.equals(currentWindowHandle))).findFirst().orElse("");
    }

    public void setInputValue(WebElement element, String value) {
        logger.info(String.format("Setting input form:%s with value %s", element.getAttribute(Attributes.ID.getValue()), value));
        element.click();
        element.clear();
        element.sendKeys(value);
    }
    public void setFileInputValue(WebElement element, String value) {
        logger.info(String.format("Setting input form:%s with value %s", element.getAttribute(Attributes.ID.getValue()), value));
        element.clear();
        element.sendKeys(value);
    }

    public String getElementWithChildText(WebElement element) {
        List<WebElement> childElements = element.findElements(By.xpath("./*"));
        StringBuilder elementContent = new StringBuilder();
        if (!childElements.isEmpty())
            for (WebElement child : childElements) {
                elementContent.append(child.getAttribute("textContent")).append("\n");
            }
        logger.info(String.format("Element: %s%n%s", element.getText(), elementContent));
        return elementContent.toString();
    }


}
