package com.sii.sup.widget;

import com.sii.sup.base.Attributes;
import com.sii.sup.base.TestBase;
import com.sii.sup.helper.Helper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.sii.sup.staticvalues.StaticValues.AccordionTest.*;
import static org.assertj.core.api.Assertions.assertThat;

class AccordionTest extends TestBase {

    private Properties properties;

    private void initProperties() {
        properties = Helper.readAccordionProperties();
        logger.info("Accordion test properties loaded");
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/accordion.php"})
    void createUserModal(String url) {
        initProperties();
        webDriver.get(url);
        List<String> collectedData = new ArrayList<>();
        List<WebElement> sectionWebElements = webDriver.
                findElement(By.id(properties.getProperty(ACCORDION_DIV_PROPERTY))).
                findElements(By.tagName(Attributes.H3.getValue()));
        sectionWebElements.forEach(section -> parseAndStoreSectionText(collectedData, section));
        logger.info("Verifying collected data size is equal to no of sections and no of expected texts in properties");

        assertThat(collectedData).
                hasSameSizeAs(sectionWebElements).
                hasSize(Integer.parseInt(properties.getProperty(ACCORDION_SECTIONS_PROPERTY)));
        logger.info("Verifying that collected data is correct");

        assertThat(collectedData.get(0)).isEqualToIgnoringWhitespace(properties.getProperty(ACCORDION_TEXT1_PROPERTY));
        assertThat(collectedData.get(1)).isEqualToIgnoringWhitespace(properties.getProperty(ACCORDION_TEXT2_PROPERTY));
        assertThat(collectedData.get(2)).isEqualToIgnoringWhitespace(properties.getProperty(ACCORDION_TEXT3_PROPERTY));
        assertThat(collectedData.get(3)).isEqualToIgnoringWhitespace(properties.getProperty(ACCORDION_TEXT4_PROPERTY));
    }

    private void parseAndStoreSectionText(List<String> collectedData, WebElement section) {
        section.click();
        WebElement divElement = section.findElement(By.xpath("following-sibling::div[1]"));
        new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(divElement));
        List<WebElement> childElements = divElement.findElements(By.xpath("./*"));
        StringBuilder sectionContent = new StringBuilder();
        if (!childElements.isEmpty())
            for (WebElement child : childElements) {
                sectionContent.append(child.getAttribute("textContent")).append("\n");
            }
        logger.info(String.format("Section: %s%n%s", section.getText(), sectionContent));
        collectedData.add(sectionContent.toString());
    }
}
