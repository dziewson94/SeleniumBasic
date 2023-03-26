package com.sii.sup.helper;

import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Helper {
    public static List<String> getNamesList() {
        return FileHelper.readFileToList("form/data/names.txt");
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
}
