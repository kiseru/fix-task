package com.kiseru.fix.fixtask.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HorseServiceTest {

    @Autowired
    private HorseService horseService;

    @Test
    public void testConvertPosition() throws NoSuchMethodException {
        var positions = new String[] { "A3", "B1", "11" };
        var result = new Map.Entry[] { Map.entry(0, 2), Map.entry(1, 0), null };

        var convertPosition = horseService.getClass().getDeclaredMethod("convertPosition", String.class);
        convertPosition.setAccessible(true);
        var convertedPositions = Arrays.stream(positions)
                .map(position -> {
                    try {
                        return (Map.Entry<Integer, Integer>) convertPosition.invoke(horseService, position);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        return null;
                    }
                }).toArray(Map.Entry[]::new);

        for (int i = 0; i < result.length; i++) {
            Assert.assertArrayEquals(result, convertedPositions);
        }
    }

    @Test
    public void testConvertLettersToInt() {
        var input = new String[] { "A", "BBD", "QWERTY" };
        var result = new int[] { 0, 705, 200, 227, 194 };


    }
}