package com.sii.sup.tests.widget;

import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sii.sup.staticvalues.StaticValues.Slider.SLIDER_BUTTON_PROPERTY;
import static com.sii.sup.staticvalues.StaticValues.Slider.SLIDER_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)
class SliderTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(SliderTest.class.getSimpleName());

    @ParameterizedTest
    @MethodSource("com.sii.sup.helper.DataSources#sliderTestDataSource")
    void slider(String url, int[] positions) {
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        pageHelper.init(url);
        Actions action = new Actions(pageHelper.getWebDriver());
        WebElement slider = pageHelper.findElementById(getStringProperty(SLIDER_PROPERTY));
        WebElement sliderButton = pageHelper.findElementById(getStringProperty(SLIDER_BUTTON_PROPERTY));
        int oneStepInPixels = slider.getSize().getWidth() / 100;
        logger.info("One step in slider progress in pixels is equal to:" + oneStepInPixels);
        for (int val : positions) {
            moveSlider(val, action, oneStepInPixels);
            assertThat(Integer.parseInt(sliderButton.getText())).isEqualTo(val);
        }
    }

    private void moveSlider(int desiredVal, Actions action, int oneStepInPx) {
        WebElement slider = pageHelper.findElementById(getStringProperty(SLIDER_BUTTON_PROPERTY));
        logger.info(String.format("Moving slider from %s to %d%nSlider beginning position x:%d y%d", slider.getText(), desiredVal, slider.getLocation().getX(), slider.getLocation().getY()));
        int sliderValueDistance = desiredVal - Integer.parseInt(slider.getText());
        sliderValueDistance *= oneStepInPx;
        String cssBorderWidth = slider.getCssValue("border-width");
        long borderWidth = Math.round(Double.parseDouble(cssBorderWidth.substring(0, cssBorderWidth.indexOf('p'))));
        logger.info(slider.getSize().getWidth() + " " + borderWidth);
        sliderValueDistance -= (slider.getSize().getWidth() / 3);
        sliderValueDistance += borderWidth * 2;
        String direction = sliderValueDistance > 0 ? "right" : "left";
        logger.info(String.format("Slider will be moved %d pixels %s%n It is result of (slider_length/100)*(desiredVal - actualVal) - slider_button_width/3", sliderValueDistance, direction));
        action.dragAndDropBy(slider, sliderValueDistance, 0).perform();
        logger.info("End button location " + slider.getLocation().getX());

    }

}

