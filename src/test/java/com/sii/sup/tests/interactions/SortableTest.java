package com.sii.sup.tests.interactions;

import com.sii.sup.tests.base.Attributes;
import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.sii.sup.staticvalues.StaticValues.Sortable.SORTABLE_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)

class SortableTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(SortableTest.class.getSimpleName());

    @ParameterizedTest
    @MethodSource("com.sii.sup.helper.DataSources#sortableTestDataSource")
    void sortableTest(String url, List<Integer> values) {
        pageHelper.init(url);
        propertyHelper = TestHelper.getPropertyHelper(SortableTest.class.getSimpleName());
        StringBuilder valuesString = new StringBuilder();
        values.forEach(val -> valuesString.append(val).append(","));
        logger.info("Provided values: " + valuesString.substring(0, valuesString.length() - 1));
        logger.info("Shuffle it!");
        Collections.shuffle(values, new Random(System.currentTimeMillis()));
        StringBuilder shuffledValuesString = new StringBuilder();
        values.forEach(val -> shuffledValuesString.append(val).append(","));
        logger.info("Shuffled values: " + shuffledValuesString.substring(0, shuffledValuesString.length() - 1));
        WebElement sortableElement = pageHelper.findElementById(getStringProperty(SORTABLE_PROPERTY));
        List<WebElement> listElements = sortableElement.findElements(By.tagName(Attributes.LI.getValue()));
        listElements.stream().map(WebElement::getText).forEach(logger::info);
        shuffleWebElements(values, sortableElement);
        listElements = sortableElement.findElements(By.tagName(Attributes.LI.getValue()));
        logger.info("List of elements after shuffle");
        listElements.stream().map(WebElement::getText).forEach(logger::info);

        for (int j = 0; j < listElements.size(); j++) {
            assertThat(listElements.get(j).getText()).endsWith(String.valueOf(values.get(j)));
        }

    }

    private void shuffleWebElements(List<Integer> values, WebElement sortableElement) {
        List<WebElement> listElements;
        int i = 0;
        for (int val : values) {
            listElements = sortableElement.findElements(By.tagName(Attributes.LI.getValue()));
            WebElement webElement = findMatchingElementInList(val, listElements);
            Actions action = new Actions(pageHelper.getWebDriver());
            action.moveToElement(webElement).dragAndDrop(webElement, listElements.get(i)).perform();
            i++;
        }
    }

    private WebElement findMatchingElementInList(int val, List<WebElement> elements) {
        for (WebElement el : elements) {
            if (el.getText().trim().endsWith(String.valueOf(val))) {
                return el;
            }
        }
        return pageHelper.getWebDriver().findElement(By.tagName(Attributes.LI.getValue()));
    }
}
