package com.sii.sup.tests.widget;

import com.sii.sup.tests.base.Attributes;
import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.sii.sup.staticvalues.StaticValues.AccordionTest.*;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)

class AccordionTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(AccordionTest.class.getSimpleName());
    private final List<String> sectionContent = new ArrayList<>();

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/accordion.php"})
    void createUserModal(String url) {
        pageHelper.init(url);
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        List<WebElement> sectionWebElements = pageHelper.findChildElementsByTag(Attributes.H3.getValue(), pageHelper.findElementById(getStringProperty(ACCORDION_DIV_PROPERTY)));
        sectionWebElements.forEach(this::parseAndStoreSectionText);
        logger.info("Verifying collected data size is equal to no of sections and no of expected texts in properties");
        assertThat(sectionContent).
                hasSameSizeAs(sectionWebElements).
                hasSize(getIntProperty(ACCORDION_SECTIONS_PROPERTY));
        logger.info("Verifying that collected data is correct");
        assertThat(sectionContent.get(0)).isEqualToIgnoringWhitespace(getStringProperty(ACCORDION_TEXT1_PROPERTY));
        assertThat(sectionContent.get(1)).isEqualToIgnoringWhitespace(getStringProperty(ACCORDION_TEXT2_PROPERTY));
        assertThat(sectionContent.get(2)).isEqualToIgnoringWhitespace(getStringProperty(ACCORDION_TEXT3_PROPERTY));
        assertThat(sectionContent.get(3)).isEqualToIgnoringWhitespace(getStringProperty(ACCORDION_TEXT4_PROPERTY));
    }

    private void parseAndStoreSectionText(WebElement section) {
        section.click();
        WebElement divElement = section.findElement(By.xpath("following-sibling::div[1]"));
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(divElement));
        sectionContent.add(pageHelper.getElementWithChildText(divElement));
    }
}
