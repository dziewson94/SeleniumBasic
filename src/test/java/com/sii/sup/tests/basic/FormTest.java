package com.sii.sup.tests.basic;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.sii.sup.tests.base.Attributes;
import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sii.sup.staticvalues.StaticValues.FormTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class FormTest extends TestBase {

    private final List<WebElement> randomOptionElement = new ArrayList<>();
    private File fileToUpload;
    private final Logger logger = new LoggerContext().getLogger(FormTest.class.getSimpleName());
    private final List<WebElement> allSelectedOption = new ArrayList<>();
    private String age;
    private String name;
    private String lastName;
    private String email;
    private int numOfFilesInDownloadDir;

    private void initData() {
        logger.info("Initializing test data");
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        logger.info("Form test properties loaded");
        fileToUpload = TestHelper.getTempFileForFormTest();
        logger.info(String.format("Test will use name:%s lastname:%s email:%s age:%s in input forms", name, lastName, email, age));

    }

    @ParameterizedTest
    @MethodSource("com.sii.sup.helper.DataSources#formTestDataSource")
    void formTest(String url, String name, String lastName, String email, String age) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        initData();
        pageHelper.init(url);
        setKeysOps();
        selectOps();
        downloadTestFile();
        verifyTestResult();
        submit();
        TestHelper.deleteTestFile();
    }


    private void setKeysOps() {
        pageHelper.setInputValue(pageHelper.findElementById(getStringProperty(NAME_PROPERTY)), name);
        pageHelper.setInputValue(pageHelper.findElementById(getStringProperty(LAST_NAME_PROPERTY)), lastName);
        pageHelper.setInputValue(pageHelper.findElementById(getStringProperty(EMAIL_PROPERTY)), email);
        pageHelper.setInputValue(pageHelper.findElementById(getStringProperty(AGE_PROPERTY)), String.valueOf(age));
        pageHelper.setFileInputValue(pageHelper.findElementById(getStringProperty(FILE_SELECT_PROPERTY)), fileToUpload.getAbsolutePath());

    }


    private void submit() {
        logger.info("Looking for submit button using css selector:button[type='submit']");
        WebElement submitButton = pageHelper.getWebDriver().findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
        String pageSource = pageHelper.getWebDriver().getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(getStringProperty(SUCCESS_TEXT_PROPERTY)));
        assertThat(pageSource).contains(getStringProperty(SUCCESS_TEXT_PROPERTY));

    }

    private void selectOps() {
        choseRandomOption(SEX_SEL_PROPERTY);
        choseRandomOption(EXP_SEL_PROPERTY);
        pageHelper.findElementById(getStringProperty(DESIRED_PROF_PROPERTY)).click();
        selectRandomListElement(getStringProperty(CONTINENTS_SELECT_PROPERTY));
        selectSpecificListElement(getStringProperty(SELENIUM_SELECT_PROPERTY));
    }

    private void verifyTestResult() {
        assertThat(pageHelper.findElementById(getStringProperty(NAME_PROPERTY)).getAttribute(Attributes.VALUE.getValue())).isEqualTo(name);
        assertThat(pageHelper.findElementById(getStringProperty(LAST_NAME_PROPERTY)).getAttribute(Attributes.VALUE.getValue())).isEqualTo(lastName);
        assertThat(pageHelper.findElementById(getStringProperty(EMAIL_PROPERTY)).getAttribute(Attributes.VALUE.getValue())).isEqualTo(email);
        assertThat(pageHelper.findElementById(getStringProperty(AGE_PROPERTY)).getAttribute(Attributes.VALUE.getValue())).isEqualTo(String.valueOf(age));
        String fakeFilePath = fileToUpload.getAbsolutePath().substring(0,
                fileToUpload.getAbsolutePath().indexOf(File.separator) + 1) + "fakepath" +
                fileToUpload.getAbsolutePath().substring(fileToUpload.getAbsolutePath().lastIndexOf(File.separator));
        assertThat(pageHelper.findElementById(getStringProperty(FILE_SELECT_PROPERTY)).getAttribute(Attributes.VALUE.getValue())).
                isEqualTo(
                        fakeFilePath);
        randomOptionElement.forEach(el -> assertThat(el.isSelected()).isTrue());
        assertThat(allSelectedOption).hasSize(3);
        assertThat(allSelectedOption.stream().anyMatch(el -> el.getAttribute(Attributes.VALUE.getValue()).equalsIgnoreCase(getStringProperty(SWITCH_PROPERTY)))).isTrue();
        assertThat(allSelectedOption.stream().anyMatch(el -> el.getAttribute(Attributes.VALUE.getValue()).equalsIgnoreCase(getStringProperty(WAIT_PROPERTY)))).isTrue();
        assertThat(TestHelper.isTestFileExist()).
                as("Failed to download test file").
                isTrue();
        assertThat(countFilesInDownloadDir()).
                as("Failed to download test file").
                isGreaterThan(numOfFilesInDownloadDir);
    }

    private void downloadTestFile() {
        WebElement downloadButton = pageHelper.getWebDriver().findElement(By.xpath("//*[contains(text(), 'Download')]"));
        TestHelper.deleteTestFile();
        numOfFilesInDownloadDir = countFilesInDownloadDir();
        logger.info(String.format("There is %d files in download directory", numOfFilesInDownloadDir));
        downloadButton.click();
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(20)).until(d -> TestHelper.isTestFileExist());
        logger.info(String.format("Now there is %d files in download directory", countFilesInDownloadDir()));
        logger.info("Files in download dir:");
    }

    private int countFilesInDownloadDir() {
        File downloadDir = new File(TestHelper.getDownloadDirPath());
        return Objects.requireNonNull(downloadDir.listFiles()).length;
    }

    private void choseRandomOption(String elementId) {
        WebElement randomElement = (WebElement) TestHelper.getRandomListElement(pageHelper.getWebDriver().findElements(By.name(getStringProperty(elementId))));
        randomElement.click();
        randomOptionElement.add(randomElement);
    }

    private void selectSpecificListElement(String id) {
        WebElement selectWebElement = pageHelper.findElementById(id);
        Select select = new Select(selectWebElement);
        select.deselectAll();
        logger.info(String.format("Selecting %s and %s form %s",
                getStringProperty(SWITCH_PROPERTY),
                getStringProperty(WAIT_PROPERTY),
                selectWebElement.getAttribute(Attributes.ID.getValue())
        ));
        select.selectByValue(getStringProperty(SWITCH_PROPERTY));
        select.selectByValue(getStringProperty(WAIT_PROPERTY));
        allSelectedOption.addAll(select.getAllSelectedOptions());

    }

    private void selectRandomListElement(String selectId) {
        WebElement selectWebElement = pageHelper.getWebDriver().findElement(By.id(selectId));
        Select select = new Select(selectWebElement);
        allSelectedOption.addAll(select.getAllSelectedOptions());
        WebElement selectedContinentWebElement = (WebElement) TestHelper.getRandomListElement(select.getOptions());
        logger.info(String.format("Selecting %s from %s", selectedContinentWebElement.getText(), selectId));
        selectedContinentWebElement.click();
    }


}
