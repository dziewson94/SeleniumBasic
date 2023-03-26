package com.sii.sup.helper;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class DataSources {
    public static Stream<Arguments> formTestDataSource(){
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/form.php",
                        Helper.getRandomListElement(Helper.getNamesList()),  Helper.getRandomListElement(Helper.getLastNames()),
                        Helper.getRandomListElement(Helper.getEmails()),
                        String.valueOf(ThreadLocalRandom.current().nextInt(13,77)))
        );
    }

    public static Stream<Arguments> menuTestDataSource(){
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/modal-dialog.php",
                        (String) Helper.getRandomListElement(Helper.getNamesList().stream().filter(el -> el.length() > 3 && el.length() < 16).toList()),
                        (String) Helper.getRandomListElement(Helper.getEmails()),
                        Helper.generateRandomString(10))
        );
    }
}
