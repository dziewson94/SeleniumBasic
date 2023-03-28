package com.sii.sup.tests.interactions;

import com.sii.sup.tests.base.Attributes;
import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.sii.sup.staticvalues.StaticValues.SelectableInteractions.SELECTABLE_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)

class SelectableTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(SelectableTest.class.getSimpleName());

    @ParameterizedTest
    @MethodSource("com.sii.sup.helper.DataSources#selectableTestDataSource")
    void selectableInteractionsTest(String url, int[] values) {
        pageHelper.init(url);
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        WebElement selectableList = pageHelper.findElementById(getStringProperty(SELECTABLE_PROPERTY));
        List<WebElement> listElements = selectableList.findElements(By.tagName(Attributes.LI.getValue()));
        listElements.stream().map(WebElement::getText).toList().forEach(logger::info);
        Actions action = new Actions(pageHelper.getWebDriver());
        action.keyDown(Keys.LEFT_CONTROL).perform();
        Arrays.stream(values).<Consumer<? super WebElement>>mapToObj(value -> el -> {
            if (el.getText().contains(String.valueOf(value))) {
                el.click();
            }
        }).forEach(listElements::forEach);
        action.release();
        String pageSoure = pageHelper.getPageSource();
        StringBuilder resultStringBuilder = new StringBuilder("You've selected:");
        for (int val : values) {
            logger.info("Check if " + val + "is selected");
            resultStringBuilder.append(String.format(" #%d", val));
            assertThat(pageSoure).contains(String.format(" #%d", val));
        }
        logger.info("Text " + resultStringBuilder + "appeared!");


    }
}
