package com.sii.sup.tests.widget;

import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import com.sii.sup.staticvalues.StaticValues.Tooltip;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sii.sup.staticvalues.StaticValues.Tooltip.TOOLTIP_MESSAGE_CLASS_PROPERTY;
import static com.sii.sup.staticvalues.StaticValues.Tooltip.TOOLTIP_SUCCESS_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)

public class TooltipTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TooltipTest.class);

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/tooltip.php"})
    public void tooltipTest(String url) {
        pageHelper.init(url);
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        WebElement tooltipElement = pageHelper.findElementById(getStringProperty(Tooltip.TOOLTIP_PROPERTY));
        // Create an instance of the Actions class
        Actions actions = new Actions(pageHelper.getWebDriver());

// Hover over the element
        actions.moveToElement(tooltipElement).perform();
        String hoverText = pageHelper.getWebDriver().findElement
                        (By.className(getStringProperty(TOOLTIP_MESSAGE_CLASS_PROPERTY))).
                getText();
        logger.info(hoverText);
        assertThat(hoverText).isEqualTo(getStringProperty(TOOLTIP_SUCCESS_PROPERTY));
    }
}
