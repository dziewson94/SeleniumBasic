package com.sii.sup.helper;

import com.sii.sup.tests.interactions.Direction;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataSources {
    public static Stream<Arguments> formTestDataSource() {
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/form.php",
                        TestHelper.getRandomListElement(TestHelper.getNamesList()), TestHelper.getRandomListElement(TestHelper.getLastNames()),
                        TestHelper.getRandomListElement(TestHelper.getEmails()),
                        String.valueOf(ThreadLocalRandom.current().nextInt(13, 77)))
        );
    }

    public static Stream<Arguments> menuTestDataSource() {
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/modal-dialog.php",
                        TestHelper.getRandomListElement(TestHelper.getNamesList().stream().filter(el -> el.length() > 3 && el.length() < 16).toList()),
                        TestHelper.getRandomListElement(TestHelper.getEmails()),
                        TestHelper.generateRandomString(10))
        );
    }

    public static Stream<Arguments> selectableTestDataSource() {
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/selectable.php",
                        new int[]
                                {1, 3, 4}
                )
        );
    }

    public static Stream<Arguments> highSiteTestDataSource() {
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/high-site.php",
                        true
                ),
                Arguments.of("http://www.seleniumui.moderntester.pl/high-site.php",
                        false
                )
        );
    }


    public static Stream<Arguments> sortableTestDataSource() {
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/sortable.php",
                        Lists.newArrayList(IntStream.range(1,8).iterator())
                )
        );
    }

    public static Stream<Arguments> resizableTestDataSource() {
        List<Map<Direction, Integer>> steps = new ArrayList<>();

        Map<Direction, Integer> firstStep = new HashMap<>();
        Map<Direction, Integer> secondStep = new HashMap<>();
        Map<Direction, Integer> thirdStep = new HashMap<>();
        firstStep.put(Direction.RIGHT, 10);
        secondStep.put(Direction.DOWN, 10);
        thirdStep.put(Direction.DOWN, 10);
        thirdStep.put(Direction.RIGHT, 10);

        steps.add(firstStep);
        steps.add(secondStep);
        steps.add(thirdStep);
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/resizable.php", steps));


    }

    public static Stream<Arguments> sliderTestDataSource() {
        return Stream.of(
                Arguments.of("http://www.seleniumui.moderntester.pl/slider.php",
                        new int[]{50, 80, 80, 20, 0}),
                Arguments.of("http://www.seleniumui.moderntester.pl/slider.php",
                        new int[]{51, 80, 33, 20, 0}));
    }

}
