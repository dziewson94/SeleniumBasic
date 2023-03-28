package com.sii.sup.tests.interactions;

import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sii.sup.staticvalues.StaticValues.Droppable.DROPPABLE_DRAGGABLE_PROPERTY;
import static com.sii.sup.staticvalues.StaticValues.Droppable.DROPPABLE_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)

class DroppableTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(DroppableTest.class.getSimpleName());

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/droppable.php"})
    void droppableTest(String url) {
        pageHelper.init(url);
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        WebElement draggable = pageHelper.findElementById(getStringProperty(DROPPABLE_DRAGGABLE_PROPERTY));
        WebElement droppable = pageHelper.findElementById(getStringProperty(DROPPABLE_PROPERTY));
        logger.info(String.format("Draggable size = width:%d height:%d. Droppable size = width:%d height:%d.", draggable.getSize().getWidth(),draggable.getSize().getHeight(),
                droppable.getSize().getWidth(),droppable.getSize().getHeight()));
        Actions actions = new Actions(pageHelper.getWebDriver());
        actions.dragAndDrop(draggable, droppable).perform();
        logger.info(String.format("Draggable location = x:%d y:%d. Droppable location = x:%d y:%d",
                draggable.getLocation().getX(), draggable.getLocation().getY(),
                droppable.getLocation().getX(), droppable.getLocation().getY()));

        assertThat(droppable.getLocation().getX()).isLessThan(draggable.getLocation().getX());
        assertThat(droppable.getLocation().getX()+droppable.getSize().getWidth()).isGreaterThan(draggable.getLocation().getX()+draggable.getSize().getWidth());
        assertThat(droppable.getLocation().getY()).isLessThan(draggable.getLocation().getY());
        assertThat(droppable.getLocation().getY()+droppable.getSize().getHeight()).isGreaterThan(draggable.getLocation().getY()+draggable.getSize().getHeight());


    }
}
