package com.sii.sup.helper;

import org.junit.jupiter.params.provider.Arguments;

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
                        Helper.getRandomListElement(Helper.getNamesList().stream().filter(el -> el.length() > 3 && el.length() < 16).toList()),
                        Helper.getRandomListElement(Helper.getEmails()),
                        Helper.generateRandomString(10))
        );
    }
    public static Stream<Arguments> sliderTestDataSource(){
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/slider.php",
                        new int[]{50,80,80,20,0}),
                Arguments.of("http://www.seleniumui.moderntester.pl/slider.php",
                        new int[]{51,80,33,20,0}));
    }

}
