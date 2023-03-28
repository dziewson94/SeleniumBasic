package com.sii.sup.tests.other;

import com.sii.sup.helper.TestHelper;
import com.sii.sup.tests.base.Attributes;
import com.sii.sup.tests.base.TestBase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.sii.sup.staticvalues.StaticValues.DemoQa.*;
import static org.assertj.core.api.Assertions.assertThat;

class DemoQaTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(com.sii.sup.tests.other.DemoQaTest.class.getSimpleName());
    private final StringBuilder expectedResult = new StringBuilder();

    @ParameterizedTest
    @ValueSource(strings = {"https://demoqa.com/automation-practice-form"})
    void demoQaTest(String url) {
        propertyHelper = TestHelper.getPropertyHelper(DemoQaTest.class.getSimpleName());
        pageHelper.init(url);

        selectDesiredSubjectValue(getStringProperty(SUBJECTS_MATHS_PROPERTY));
        selectDesiredSubjectValue(getStringProperty(SUBJECTS_ARTS_PROPERTY));

        StringBuilder actualSubjectsString = new StringBuilder();
        List<WebElement> resultContainer = pageHelper.getWebDriver().findElements(By.className(getStringProperty(SUBJECTS_RESULT_CONTAINER_PROPERTY)));
        for (WebElement webElement : resultContainer) {
            actualSubjectsString.append(webElement.getText()).append(" ");
        }
        logger.info("Actual selected subjects: " + actualSubjectsString + ". Expected subjects to be selected: " + expectedResult);
        assertThat(actualSubjectsString).isEqualToIgnoringWhitespace(expectedResult);
    }

    private void selectDesiredSubjectValue(String subject) {
        expectedResult.append(subject).append(" ");
        WebElement subjectInput = pageHelper.findElementById(getStringProperty(SUBJECTS_INPUT_ID_PROPERTY));
        subjectInput.sendKeys(subject.substring(0, 1).toLowerCase());
        WebElement subjectMenu = pageHelper.getWebDriver().findElement(By.className(getStringProperty(SUBJECTS_MENU_PROPERTY)));
        for (WebElement webElement : subjectMenu.findElements(By.tagName(Attributes.DIV.getValue()))) {
            if (webElement.getText().equals(subject)) {
                logger.info("Clicking on subject:" + webElement.getText());
                webElement.click();
                break;
            }
        }
    }

}
