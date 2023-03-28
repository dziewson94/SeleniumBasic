package com.sii.sup.helper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class TestHelper {

    private static Logger logger = LoggerFactory.getLogger(TestHelper.class);





    public static PageHelper getPageHelper(WebDriver webDriver) {
        return new PageHelper(webDriver);
    }


    public static List<String> getNamesList() {
        return FileHelper.readFileToList("form/data/names.txt");
    }
    private static PropertyHelper initializeProperties(String className) {
        Properties allProperties = new Properties();
        switch (className) {
            case ("AccordionTest") -> allProperties = readAccordionProperties();
            case ("FormTest") -> allProperties = readFormProperties();
            case ("AlertTest") -> allProperties = readAlertProperties();
            case ("TableTest") -> allProperties = readTableProperties();
            case ("WindowTest") -> allProperties = readWindowProperties();
            case ("IframeTest") -> allProperties = readIframeProperties();
            case ("MenuTest") -> allProperties = readMenuProperties();
            case ("DatePickerTest") -> allProperties = readDatepickerProperties();
            case ("AutocompleteTest") -> allProperties = readAutocompleteProperties();
            case ("SliderTest") -> allProperties = readSliderProperties();
            case ("ProgressBarTest") -> allProperties = readProgressbarProperties();
            case ("SelectableTest") -> allProperties = readSelectableProperties();
            case ("TooltipTest") -> allProperties = readTooltipProperties();
            case ("DraggableTest") -> allProperties = readDraggableProperties();
            case ("DroppableTest") -> allProperties = readDroppableProperties();
            case ("ResizableTest") ->allProperties= readResizableProperties();
        }
        logger.debug("Initializing properties configuration for " + className);

        return new PropertyHelper(allProperties);

    }

    public static List<String> getLastNames() {
        return FileHelper.readFileToList("form/data/lastnames.txt");
    }

    public static List<String> getEmails() {
        return FileHelper.readFileToList("form/data/emails.txt");
    }

    public static Object getRandomListElement(List<?> list) {
        return list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
    }

    public static File getTempFileForFormTest() {
        return FileHelper.createTempFileInFormResources();
    }

    public static String getDownloadDirPath() {
        return FileHelper.getDownloadDirPath();
    }

    public static void deleteTestFile() {
        FileHelper.deleteTestFile();
    }

    public static boolean isTestFileExist() {
        return FileHelper.testFileExist();
    }
    public static PropertyHelper getPropertyHelper(String className) {
        return initializeProperties(className);
    }
    public static List<String> getDownloadDirFilesNames() {
        return FileHelper.getDownloadDirFilesNames();
    }

    public static WebElement findWebElementById(List<WebElement> elements, String id) {
        return elements.stream().filter(el -> el.getAttribute("id").equals(id)).findFirst().orElse(elements.get(0));
    }



    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int asciiValue = ThreadLocalRandom.current().nextInt(97, 122);
            sb.append((char) asciiValue);
        }
        return sb.toString();
    }

    private static Properties readTableProperties() {
        return FileHelper.readProperties("table/table.properties");
    }

    private static Properties readWindowProperties() {
        return FileHelper.readProperties("window/window.properties");
    }

    private static Properties readDatepickerProperties() {
        return FileHelper.readProperties("datepicker/datepicker.properties");
    }

    private static Properties readAutocompleteProperties() {
        return FileHelper.readProperties("autocomplete/autocomplete.properties");
    }

    private static Properties readResizableProperties() {
        return FileHelper.readProperties("resizable/resizable.properties");
    }

    private static Properties readDraggableProperties() {
        return FileHelper.readProperties("draggable/draggable.properties");
    }

    private static Properties readDroppableProperties() {
        return FileHelper.readProperties("droppable/droppable.properties");
    }

    private static Properties readSliderProperties() {
        return FileHelper.readProperties("slider/slider.properties");
    }

    private static Properties readTooltipProperties() {
        return FileHelper.readProperties("tooltip/tooltip.properties");
    }

    private static Properties readSelectableProperties() {
        return FileHelper.readProperties("selectable/selectable.properties");
    }

    private static Properties readProgressbarProperties() {
        return FileHelper.readProperties("progressbar/progressbar.properties");
    }

    private static Properties readFormProperties() {
        return FileHelper.readProperties("form/form.properties");
    }

    private static Properties readIframeProperties() {
        return FileHelper.readProperties("iframe/iframe.properties");
    }

    private static Properties readMenuProperties() {
        return FileHelper.readProperties("menu/menu.properties");
    }

    private static Properties readAccordionProperties() {
        return FileHelper.readProperties("accordion/accordion.properties");
    }

    private static Properties readAlertProperties() {
        return FileHelper.readProperties("alert/alert.properties");
    }
}
