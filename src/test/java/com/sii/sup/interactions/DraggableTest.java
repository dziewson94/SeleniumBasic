package com.sii.sup.interactions;

import com.sii.sup.base.TestBase;
import com.sii.sup.helper.PropertyHelper;
import com.sii.sup.helper.TestHelper;
import com.sii.sup.staticvalues.StaticValues;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sii.sup.staticvalues.StaticValues.Draggable.DRAGGABLE_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

public class DraggableTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(DraggableTest.class.getSimpleName());

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/draggable.php"})
    public void draggableTest(String url) {
        pageHelper.init(url);
        propertyHelper = TestHelper.getPropertyHelper(DraggableTest.class.getSimpleName());
        WebElement draggableEl = pageHelper.findElementById(getStringProperty(DRAGGABLE_PROPERTY));
        Dimension windowSize = pageHelper.getWebDriver().manage().window().getSize();
        Dimension draggableElSize = draggableEl.getSize();
        logger.info(String.format("Window size = width:%d height:%d. Draggable size = width%d height:%d. Draggable position = x:%d y:%d.",
                windowSize.getWidth(), windowSize.getHeight(), draggableElSize.getWidth(), draggableElSize.getHeight(),
                draggableEl.getLocation().getX(), draggableEl.getLocation().getY()));
        Point leftTopCorner = new Point(0, 0);
        Point rightTopCorner = new Point(windowSize.getWidth() - draggableElSize.getWidth(), 0);
        Point leftBottomCorner = new Point(0, windowSize.getHeight() - (draggableElSize.getHeight() * 2));
        Point rightBottomCorner = new Point(windowSize.getWidth() - draggableElSize.getWidth(), windowSize.getHeight() - (draggableElSize.getHeight() * 2));
        logger.info(String.format("Moving draggable to left top corner. Position x:%d, y:%d", leftTopCorner.getX(), leftTopCorner.getY()));
        moveDraggableToPosition(draggableEl, leftTopCorner);
        logger.info(String.format("Draggable position = x:%d y:%d.",
                draggableEl.getLocation().getX(), draggableEl.getLocation().getY()));
        assertThat(draggableEl.getLocation()).isEqualTo(leftTopCorner);
        logger.info(String.format("Moving draggable to right top corner. Position x:%d, y:%d", rightTopCorner.getX(), rightTopCorner.getY()));
        moveDraggableToPosition(draggableEl, rightTopCorner);
        logger.info(String.format("Draggable position = x:%d y:%d.",
                draggableEl.getLocation().getX(), draggableEl.getLocation().getY()));
        assertThat(draggableEl.getLocation()).isEqualTo(rightTopCorner);
        logger.info(String.format("Moving draggable to left bottom corner. Position x:%d, y:%d", leftBottomCorner.getX(), leftBottomCorner.getY()));
        moveDraggableToPosition(draggableEl, leftBottomCorner);
        logger.info(String.format("Draggable position = x:%d y:%d.",
                draggableEl.getLocation().getX(), draggableEl.getLocation().getY()));
        assertThat(draggableEl.getLocation()).isEqualTo(leftBottomCorner);
        logger.info(String.format("Moving draggable to right bottom corner. Position x:%d, y:%d", rightBottomCorner.getX(), rightBottomCorner.getY()));
        moveDraggableToPosition(draggableEl, rightBottomCorner);
        logger.info(String.format("Draggable position = x:%d y:%d.",
                draggableEl.getLocation().getX(), draggableEl.getLocation().getY()));
        assertThat(draggableEl.getLocation()).isEqualTo(rightBottomCorner);

    }

    private void moveDraggableToPosition(WebElement draggableEl, Point position) {
        Point draggableLocation = draggableEl.getLocation();
        int xOffset = position.getX() - draggableLocation.getX();
        int yOffset = position.getY() - draggableLocation.getY();
        logger.info(String.format("Moving draggable by x_Offset:%d y_Offset:%d", xOffset, yOffset));
        Actions action = new Actions(pageHelper.getWebDriver());
        action.dragAndDropBy(draggableEl, xOffset, yOffset).perform();
    }
}
