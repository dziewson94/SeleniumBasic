package com.sii.sup.tests.basic;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.sii.sup.tests.base.Attributes;
import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.sii.sup.staticvalues.StaticValues.TableTest.*;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)

class TableTest extends TestBase {
    private final Logger logger = new LoggerContext().getLogger(TableTest.class.getSimpleName());


    private void initProperties() {
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        logger.info("Table test properties loaded");
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/table.php"})
    void tableTest(String url) {
        initProperties();
        pageHelper.init(url);
        WebElement tableWebElement = pageHelper.getWebDriver().findElement(By.className(getStringProperty(TABLE_PROPERTY)));
        WebElement tbodyWebElement = tableWebElement.findElement(By.tagName(Attributes.TBODY.getValue()));
        List<WebElement> rows = tbodyWebElement.findElements(By.tagName(Attributes.TABLE_ROW.getValue()));
        List<Mountain> mountains = new ArrayList<>();

        for (int i = 1; i < rows.size() - 1; i++) {
            List<WebElement> cells = rows.get(i).findElements(By.tagName(Attributes.TABLE_COLUMN.getValue()));
            String rank = rows.get(i).findElement(By.tagName(Attributes.TABLE_HEADER.getValue())).getText();
            Mountain mountain = new Mountain();
            mountain.setRank(rank);
            mountain.setPeak(cells.get(0).getText());
            mountain.setMountainRange(cells.get(1).getText());
            mountain.setState(cells.get(2).getText());
            mountain.setHeight(cells.get(3).getText());
            mountains.add(mountain);
        }
        List<Mountain> filteredList = mountains.stream().filter(mountain ->
                mountain.state.equals(getStringProperty(STATE_PROPERTY)) &&
                        (Integer.parseInt(mountain.height) >
                                getIntProperty(MIN_HEIGHT_PROPERTY))
        ).toList();
        assertThat(filteredList).hasSize(Integer.parseInt(getStringProperty(EXPECTED_RESULT_RECORDS_PROPERTY)));
    }

    private static class Mountain {
        private String rank;
        private String peak;
        private String mountainRange;
        private String state;
        private String height;


        void setRank(String rank) {
            this.rank = rank;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Mountain.class.getSimpleName() + "[", "]")
                    .add("rank='" + rank + "'")
                    .add("peak='" + peak + "'")
                    .add("mountainRange='" + mountainRange + "'")
                    .toString();
        }

        void setPeak(String peak) {
            this.peak = peak;
        }


        void setMountainRange(String mountainRange) {
            this.mountainRange = mountainRange;
        }


        void setState(String state) {
            this.state = state;
        }


        void setHeight(String height) {
            this.height = height;
        }
    }
}

