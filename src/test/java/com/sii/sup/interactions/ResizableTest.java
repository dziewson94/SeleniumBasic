package com.sii.sup.interactions;

import com.sii.sup.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.sii.sup.staticvalues.StaticValues.Resizable.RESIZABLE_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

class ResizableTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(ResizableTest.class.getSimpleName());

    @ParameterizedTest
    @MethodSource("com.sii.sup.helper.DataSources#resizableTestDataSource")
    void resizableTest(String url, List<Map<Direction, Integer>> steps) {
        pageHelper.init(url);
        propertyHelper = TestHelper.getPropertyHelper(ResizableTest.class.getSimpleName());
        WebElement resizable = pageHelper.findElementById(getStringProperty(RESIZABLE_PROPERTY));
        logger.info("Element size" + resizable.getSize().toString());
        logger.info("Element location" + resizable.getLocation().toString());
        int stepIndex = 1;
        for (Map<Direction, Integer> step : steps) {
            logger.info(String.format("Element size before step %d is %s",stepIndex, resizable.getSize().toString()));
            logger.info(String.format("Element location before step %d is %s",stepIndex, resizable.getLocation().toString()));
            if (step.size() == 1) {
                Direction direction = step.entrySet().stream().toList().get(0).getKey();
                int offset = step.entrySet().stream().toList().get(0).getValue();
                resizeWindowOneDirection(direction,offset , resizable);
                if (direction == Direction.RIGHT) {
                    logger.info(String.format("Element size after step %d is %s",stepIndex, resizable.getSize().toString()));
                    logger.info(String.format("Element location after step %d is %s",stepIndex, resizable.getLocation().toString()));
                }else if(direction == Direction.DOWN) {
                    logger.info(String.format("Element size after step %d is %s",stepIndex, resizable.getSize().toString()));
                    logger.info(String.format("Element location after step %d is %s",stepIndex, resizable.getLocation().toString()));
                }
            } else {
                Map.Entry<Direction, Integer> firstRecord = step.entrySet().stream().toList().get(0);
                Map.Entry<Direction, Integer> secondRecord = step.entrySet().stream().toList().get(1);
                resizeWindowTwoDirection(firstRecord.getKey(), firstRecord.getValue(), secondRecord.getKey(), secondRecord.getValue(), resizable);
                logger.info(String.format("Element size after step %d is %s",stepIndex, resizable.getSize().toString()));
                logger.info(String.format("Element location after step %d is %s",stepIndex, resizable.getLocation().toString()));

            }
            assertThat(resizable.getSize().getHeight()).isLessThan(1000000);
        }
    }

    private void resizeWindowOneDirection(Direction direction, int offset, WebElement element) {
        logger.info(String.format("Resizing widow to the %s by %d", direction.name(), offset));
        Actions action = new Actions(pageHelper.getWebDriver());
        int xOffset = direction == Direction.LEFT || direction == Direction.RIGHT ? direction == Direction.LEFT ? -1 * offset : offset : 0;
        int yOffset = direction == Direction.UP || direction == Direction.DOWN ? direction == Direction.UP ? offset : -1 * offset : 0;

        if (direction == Direction.UP || direction == Direction.DOWN) {
           // int i = direction == Direction.DOWN ? 1 : -1;
            action.moveToElement(element).moveByOffset(0, element.getSize().getHeight() / 2).clickAndHold().moveByOffset(xOffset, yOffset).release().perform();
        } else {
            int i = -1;
            if (direction == Direction.RIGHT) i = 1;
            action.moveToElement(element).moveByOffset(i * element.getSize().getWidth() / 2 + 18, 0).clickAndHold().moveByOffset(xOffset, yOffset).release().perform();
        }
    }

    private void resizeWindowTwoDirection(Direction verticalDirection, int yOffset, Direction horizontalDirection, int xOffset, WebElement element) {
        logger.info(String.format("Resizing widow to the %s and %s by %d and %d", verticalDirection.name(), horizontalDirection, yOffset, xOffset));

        Actions action = new Actions(pageHelper.getWebDriver());
        xOffset = horizontalDirection == Direction.LEFT ? -1 * xOffset : xOffset;
        yOffset = verticalDirection == Direction.UP ? -1 * yOffset : yOffset;
        action.moveToElement(element).moveByOffset(element.getSize().getWidth() / 2, element.getSize().getHeight() / 2).clickAndHold().moveByOffset(xOffset, yOffset).release().perform();

    }
}
