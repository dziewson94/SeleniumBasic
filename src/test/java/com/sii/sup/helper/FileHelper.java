package com.sii.sup.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

class FileHelper {
    protected static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

    private FileHelper() {
    }

    static List<String> readFileToList(String filePath) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(FileHelper.class.getClassLoader().getResource(filePath).toURI()));
        } catch (IOException | URISyntaxException e) {
            logger.warn(String.format("Failed to read file %s.%n%s", filePath, e.getMessage()));
        }
        return lines;
    }

    static Properties readProperties(String filePath) {
        Properties prop = new Properties();
        try (InputStream input = FileHelper.class.getClassLoader().getResourceAsStream(filePath)) {
            prop.load(input);
        } catch (IOException ex) {
            logger.warn(String.format("Failed to read file %s.%n%s", filePath, ex.getMessage()));
        }
        return prop;
    }

    static File createTempFileInFormResources() {
        String content = generateRandomString(20000);
        File file = new File("");
        try {
            URL formResourcesUrl = FileHelper.class.getClassLoader().getResource("form");
            Path filePath = Paths.get(formResourcesUrl.toURI()).resolve("temp.txt");
            file = filePath.toFile();
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (URISyntaxException | IOException e) {
            logger.warn(String.format("Failed to crete temp form file.%n%s", e.getMessage()));
        }
        return file;
    }

    static String getDownloadDirPath() {
        return Paths.get("target").toAbsolutePath().toString();
    }

    static void deleteTestFile() {
        File file = new File(getDownloadDirPath() + File.separator + "test-file-to-download.xlsx");
        if (file.exists()) {
            logger.info("Is test file removed:" + file.delete());
        }
    }

    static boolean testFileExist() {
        return new File(getDownloadDirPath() + File.separator + "test-file-to-download.xlsx").exists();

    }

    static List<String> getDownloadDirFilesNames() {
        return Arrays.stream(Objects.requireNonNull(new File(getDownloadDirPath()).listFiles())).map(File::getName).toList();
    }

    static void removeTempFormFile() {
        try {
            File file = Paths.get(FileHelper.class.getClassLoader().getResource("form/temp.txt").toURI()).toFile();
            if (file.exists()) logger.info("Temp file deleted:" + file.delete());
        } catch (URISyntaxException e) {
            logger.warn(String.format("Failed to remove temp form file .%n%s", e.getMessage()));
        }
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int asciiValue = ThreadLocalRandom.current().nextInt(33, 127);
            sb.append((char) asciiValue);
        }
        return sb.toString();
    }
}