package com.kiseru.fix.fixtask.services.impl;

import com.kiseru.fix.fixtask.services.HorseService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HorseServiceImplTest {

    @Autowired
    private HorseService horseService;

    @Test
    public void testGetMovesCount() {
        Assert.assertEquals(1, horseService.getMovesCount(10, 14, "B1", "A3"));
        Assert.assertEquals(6, horseService.getMovesCount(10, 14, "A1", "H8"));
        Assert.assertEquals(4, horseService.getMovesCount(10, 14, "A1", "B2"));
        Assert.assertEquals(0, horseService.getMovesCount(10, 14, "A1", "A1"));
        Assert.assertEquals(-1, horseService.getMovesCount(3, 3, "A1", "B2"));
    }

    @Test
    public void testConvertPosition() throws NoSuchMethodException {
        var positions = new String[]{"A3", "B1", "11"};
        var result = new Map.Entry[]{Map.entry(0, 2), Map.entry(1, 0), null};

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
    public void testConvertLettersToInt() throws NoSuchMethodException {
        var input = new String[]{"A", "BBD", "QWERTY", "1234", "ФЫАВ", "adfD"};
        var expected = new int[]{0, 705, 200237802, -1, -1, -1};

        var convertLettersToInt = horseService.getClass().getDeclaredMethod("convertLettersToInt", String.class);
        convertLettersToInt.setAccessible(true);
        var result = Arrays.stream(input)
                .mapToInt(letters -> {
                    try {
                        return (int) convertLettersToInt.invoke(horseService, letters);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        return -1;
                    }
                })
                .toArray();

        Assert.assertArrayEquals(expected, result);
    }
}