package com.sii.sup.widget;

import com.sii.sup.base.Attributes;
import com.sii.sup.base.TestBase;
import com.sii.sup.helper.PropertyHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static com.sii.sup.staticvalues.StaticValues.DataPicker.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DatePicker extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(DatePicker.class.getSimpleName());

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/datepicker.php"})
    public void datePickerTest(String url) {
        LocalDate today = LocalDate.now();
        pageHelper.init(url);
        propertyHelper = new PropertyHelper(this.getClass().getSimpleName());
        logger.info("DatePicker tests properties loaded");
        WebElement datePickerElement = pageHelper.findElementById(getStringProperty(DATE_PICKER_ID_PROPERTY));
        setDate(today, datePickerElement);
        assertDate(today, datePickerElement);
        LocalDate next = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        setDate(next, datePickerElement);
        assertDate(next, datePickerElement);
        next = LocalDate.of(LocalDate.now().getYear() + 1, 1, 31);
        setDate(next, datePickerElement);
        assertDate(next, datePickerElement);
        datePickerElement.clear();
        setDate(next, datePickerElement);
        assertDate(next, datePickerElement);
        int randomDay = new Random().nextInt(LocalDate.now().minusMonths(1).lengthOfMonth()) + 1;
        next = LocalDate.now().minusMonths(1).withDayOfMonth(randomDay);
        setDate(next, datePickerElement);
        assertDate(next, datePickerElement);
        int year = LocalDate.now().minusYears(1).getYear();
        int randomMonth = new Random().nextInt(12) + 1;
        int randomDate = new Random().nextInt(LocalDate.of(year, randomMonth, 1).lengthOfMonth()) + 1;
        next = LocalDate.of(year, randomMonth, randomDate);
        setDate(next, datePickerElement);
        assertDate(next, datePickerElement);
    }

    public void setDate(LocalDate dateTime, WebElement datepicker) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        logger.info(String.format("Insert date %s in datepicker. Current value:%s", formatter.format(dateTime), datepicker.getText()));
        datepicker.click();
        int providedDay = dateTime.getDayOfMonth();
        int providedMonth = dateTime.getMonthValue();
        int providedYear = dateTime.getYear();
        WebElement table = datepicker.findElement(By.xpath(getStringProperty(TABLE_PROPERTY)));
        List<WebElement> currentMonthRows = table.findElements(By.tagName(Attributes.TABLE_COLUMN.getValue()));
        goToSelectedYear(datepicker, providedYear);
        currentMonthRows = goToSelectedMonth(datepicker, providedMonth);
        for (WebElement tableRow : currentMonthRows) {
            int day = Integer.parseInt(tableRow.getText());
            int month = Integer.parseInt(tableRow.getAttribute("data-month")) + 1;
            if (month == providedMonth && day == providedDay) {
                tableRow.click();
                break;
            }
        }
    }

    private List<WebElement> goToSelectedMonth(WebElement datepicker, int month) {
        WebElement table;
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(datepicker));
        datepicker.click();
        table = datepicker.findElement(By.xpath(getStringProperty(TABLE_PROPERTY)));
        List<WebElement> currentMonthRows = table.findElements(By.tagName(Attributes.TABLE_COLUMN.getValue()));
        datepicker.click();
        while (Integer.parseInt(currentMonthRows.get(getMidValueOfListSize(currentMonthRows)).getAttribute("data-month")) < month - 1) {
            currentMonthRows = nextElementClick(datepicker);

        }
        while (Integer.parseInt(currentMonthRows.get(getMidValueOfListSize(currentMonthRows)).
                getAttribute("data-month")) > month - 1) {
            currentMonthRows = previousElementClick(datepicker);

        }
        return currentMonthRows;
    }

    private List<WebElement> previousElementClick(WebElement datepicker) {
        List<WebElement> currentMonthRows;
        WebElement table;
        WebElement prevMonthButton;
        prevMonthButton = pageHelper.getWebDriver().findElement(By.xpath(getStringProperty(PREV_PROPERTY)));
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(prevMonthButton));
        prevMonthButton.click();
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(datepicker));
        datepicker.click();
        table = datepicker.findElement(By.xpath(getStringProperty(TABLE_PROPERTY)));
        currentMonthRows = table.findElements(By.tagName(Attributes.TABLE_COLUMN.getValue()));
        return currentMonthRows;
    }

    private List<WebElement> nextElementClick(WebElement datepicker) {
        List<WebElement> currentMonthRows;
        WebElement table;
        WebElement nextMonthButton;
        nextMonthButton = pageHelper.getWebDriver().findElement(By.xpath(getStringProperty(NEXT_PROPERTY)));
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(nextMonthButton));
        nextMonthButton.click();
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(datepicker));
        datepicker.click();
        table = datepicker.findElement(By.xpath(getStringProperty(TABLE_PROPERTY)));
        currentMonthRows = table.findElements(By.tagName(Attributes.TABLE_COLUMN.getValue()));
        return currentMonthRows;
    }

    private int getMidValueOfListSize(List<?> list) {
        return list.size() / 2;
    }

    private List<WebElement> goToSelectedYear(WebElement datepicker, int year) {
        WebElement table;
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(datepicker));

        datepicker.click();
        table = datepicker.findElement(By.xpath(getStringProperty(TABLE_PROPERTY)));

        List<WebElement> currentMonthRows = table.findElements(By.tagName(Attributes.TABLE_COLUMN.getValue()));

        datepicker.click();
        while (Integer.parseInt(currentMonthRows.get(getMidValueOfListSize(currentMonthRows)).getAttribute("data-year")) < year) {
            currentMonthRows = nextElementClick(datepicker);

        }


        while (Integer.parseInt(currentMonthRows.get(getMidValueOfListSize(currentMonthRows)).
                getAttribute("data-year")) > year) {
            currentMonthRows = previousElementClick(datepicker);

        }
        return currentMonthRows;
    }

    private void assertDate(LocalDate date, WebElement el) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        assertThat(el.getAttribute(Attributes.VALUE.getValue())).isEqualTo(formatter.format(date));
    }

}
