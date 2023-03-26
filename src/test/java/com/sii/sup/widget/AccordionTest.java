//package com.sii.sup.widget;
//
//import com.sii.sup.base.Attributes;
//import com.sii.sup.base.TestBase;
//import com.sii.sup.helper.PropertyHelper;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.Duration;
//import java.util.List;
//
//import static com.sii.sup.staticvalues.StaticValues.AccordionTest.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class AccordionTest extends TestBase {
//    private static Logger logger = LoggerFactory.getLogger(AccordionTest.class.getSimpleName());
//    private PropertyHelper config = new PropertyHelper(this.getClass().getSimpleName());
//
//
//    @ParameterizedTest
//    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/accordion.php"})
//    void createUserModal(String url) {
//        pageHelper.init(url);
//        List<WebElement> sectionWebElements = pageHelper.findChildElementsByTag(Attributes.H3.getValue(), pageHelper.findElementById(config.get(ACCORDION_DIV_PROPERTY)));
//        sectionWebElements.forEach(section -> parseAndStoreSectionText(section));
//        logger.info("Verifying collected data size is equal to no of sections and no of expected texts in properties");
//        assertThat(DataHolder.storedElementsKeys).
//                hasSameSizeAs(sectionWebElements).
//                hasSize(config.getInt(ACCORDION_SECTIONS_PROPERTY));
//        logger.info("Verifying that collected data is correct");
//        assertThat(DataHolder.actualValuesMap.get(DataHolder.storedElementsKeys.get(0))).isEqualToIgnoringWhitespace(config.get(ACCORDION_TEXT1_PROPERTY));
//        assertThat(DataHolder.actualValuesMap.get(DataHolder.storedElementsKeys.get(1))).isEqualToIgnoringWhitespace(config.get(ACCORDION_TEXT2_PROPERTY));
//        assertThat(DataHolder.actualValuesMap.get(DataHolder.storedElementsKeys.get(2))).isEqualToIgnoringWhitespace(config.get(ACCORDION_TEXT3_PROPERTY));
//        assertThat(DataHolder.actualValuesMap.get(DataHolder.storedElementsKeys.get(3))).isEqualToIgnoringWhitespace(config.get(ACCORDION_TEXT4_PROPERTY));
//    }
//
//    private void parseAndStoreSectionText(WebElement section) {
//        section.click();
//        WebElement divElement = section.findElement(By.xpath("following-sibling::div[1]"));
//        new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(divElement));
//        DataHolder.storedElementsKeys.add(pageHelper.getWebElementStoreKey(divElement));
//        pageHelper.getElementWithChildText(divElement);
//    }
//}
