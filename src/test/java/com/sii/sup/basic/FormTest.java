package com.sii.sup.basic;

import com.sii.sup.base.Attributes;
import com.sii.sup.base.TestBase;
import com.sii.sup.helper.Helper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.sii.sup.staticvalues.StaticValues.FormTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class FormTest extends TestBase {

    private String name;
    private String lastName;
    private String email;

    private File fileToUpload;

    private Properties properties;
    private int age;

    private void initData() {
        logger.info("Initializing test data");
        name = (String) Helper.getRandomListElement(Helper.getNamesList());
        lastName = (String) Helper.getRandomListElement(Helper.getLastNames());
        email = (String) Helper.getRandomListElement(Helper.getEmails());
        age = ThreadLocalRandom.current().nextInt(18, 80);
        properties = Helper.readFormProperties();
        logger.info("Form test properties loaded");

        fileToUpload = Helper.getTempFileForFormTest();
        logger.info(String.format("Test will use name:%s lastname:%s email:%s age:%d in input forms", name, lastName, email, age));

    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/form.php"})
    void formTest(String url) {
        initData();
        webDriver.get(url);
        performOperations();
    }

    private void performOperations() {
        List<WebElement> inputWebElements = webDriver.findElements(By.tagName(Attributes.INPUT.getValue()));
        Helper.findWebElementById(inputWebElements, properties.getProperty(NAME_PROPERTY)).click();
        setInputValue(properties.getProperty(NAME_PROPERTY), name, inputWebElements);
        setInputValue(properties.getProperty(LAST_NAME_PROPERTY), lastName, inputWebElements);
        setInputValue(properties.getProperty(EMAIL_PROPERTY), email, inputWebElements);
        selectRandomRadiosElement(SEX_SEL_PROPERTY);
        setInputValue(properties.getProperty(AGE_PROPERTY), String.valueOf(age), inputWebElements);
        selectRandomRadiosElement(EXP_SEL_PROPERTY);
        Objects.requireNonNull(webDriver.findElements(By.name(properties.getProperty(PROF_SEL_PROPERTY))).
                stream().filter(el -> el.getAttribute(Attributes.ID.getValue()).equals(properties.getProperty(DESIRED_PROF_PROPERTY))).
                findFirst().orElse(null)).click();
        assertThat(webDriver.findElement(By.id(properties.getProperty(DESIRED_PROF_PROPERTY))).isSelected()).isTrue();
        selectRandomListElement(properties.getProperty(CONTINENTS_SELECT_PROPERTY));
        WebElement selectWebElement = webDriver.findElement(By.id(properties.getProperty(SELENIUM_SELECT_PROPERTY)));
        selectSpecificListElement(selectWebElement);
        setInputValue(properties.getProperty(FILE_SELECT_PROPERTY), fileToUpload.getAbsolutePath(), inputWebElements);
        downloadTestFile();
        logger.info("Looking for submit button using css selector:button[type='submit']");
        WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        String pageSource = webDriver.getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(properties.getProperty(SUCCESS_TEXT_PROPERTY)));
        assertThat(pageSource).contains(properties.getProperty(SUCCESS_TEXT_PROPERTY));
    }

    private void downloadTestFile() {
        WebElement downloadButton = webDriver.findElement(By.xpath("//*[contains(text(), 'Download')]"));
        Helper.deleteTestFile();
        int numOfFilesInDownloadDir = countFilesInDownloadDir();
        logger.info(String.format("There is %d files in download directory", numOfFilesInDownloadDir));
        downloadButton.click();
        new WebDriverWait(webDriver, Duration.ofSeconds(20)).until(d -> Helper.isTestFileExist());
        assertThat(Helper.isTestFileExist()).
                as("Failed to download test file").
                isTrue();
        assertThat(countFilesInDownloadDir()).
                as("Failed to download test file").
                isGreaterThan(numOfFilesInDownloadDir);
        logger.info(String.format("Now there is %d files in download directory", countFilesInDownloadDir()));
        logger.info("Files in download dir:");
        Helper.getDownloadDirFilesNames().forEach(logger::info);
        Helper.deleteTestFile();
    }

    private int countFilesInDownloadDir() {
        File downloadDir = new File(Helper.getDownloadDirPath());
        return Objects.requireNonNull(downloadDir.listFiles()).length;
    }

    private void selectRandomRadiosElement(String expSelProperty) {
        WebElement randomExperienceElement = (WebElement) Helper.getRandomListElement(webDriver.findElements(By.name(properties.getProperty(expSelProperty))));
        randomExperienceElement.click();
        assertThat(randomExperienceElement.isSelected()).isTrue();
    }

    private void selectSpecificListElement(WebElement selectWebElement) {
        Select select = new Select(selectWebElement);

        select.deselectAll();
        logger.info(String.format("Selecting %s and %s form %s",
                properties.getProperty(SWITCH_PROPERTY),
                properties.getProperty(WAIT_PROPERTY),
                selectWebElement.getAttribute(Attributes.ID.getValue())
        ));
        select.selectByValue(properties.getProperty(SWITCH_PROPERTY));
        select.selectByValue(properties.getProperty(WAIT_PROPERTY));
        List<String> correctValues = new ArrayList<>();
        correctValues.add(properties.getProperty(SWITCH_PROPERTY));
        correctValues.add(properties.getProperty(WAIT_PROPERTY));
        assertThat(select.getAllSelectedOptions().stream().
                map(el -> el.getAttribute(Attributes.VALUE.getValue())).collect(Collectors.toList())).
                isEqualTo(correctValues);
    }

    private void selectRandomListElement(String selectId) {
        WebElement selectWebElement = webDriver.findElement(By.id(selectId));
        Select select = new Select(selectWebElement);
        WebElement randomListElement = (WebElement) Helper.getRandomListElement(select.getOptions());
        logger.info(String.format("Selecting %s from %s", randomListElement.getText(), selectId));
        randomListElement.click();
        assertThat(randomListElement.isSelected()).as("Item %s isn't selected from list %s", randomListElement.getText(), selectId).isTrue();
    }


    private void setInputValue(String id, String value, List<WebElement> elements) {
        WebElement element = Helper.findWebElementById(elements, id);
        logger.info(String.format("Setting input form:%s with value %s", id, value));
        if (!id.equals(properties.getProperty(FILE_SELECT_PROPERTY)))
            element.click();
        element.clear();
        element.sendKeys(value);
        String actualValue = webDriver.findElement(By.id(id)).getAttribute(Attributes.VALUE.getValue());
        String expectedValue = !id.equals(properties.getProperty(FILE_SELECT_PROPERTY)) ? value : value.substring(0, value.indexOf('\\') + 1) + "fakepath" + value.substring(value.lastIndexOf('\\'));
        assertThat(actualValue).
                isEqualTo(expectedValue);
    }
}
