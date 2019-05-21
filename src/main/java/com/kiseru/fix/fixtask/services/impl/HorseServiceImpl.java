package com.kiseru.fix.fixtask.services.impl;

import com.kiseru.fix.fixtask.services.HorseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class HorseServiceImpl implements HorseService {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Pattern LETTERS_PATTERN = Pattern.compile("[A-Z]+");
    private static final Pattern NUMBERS_PATTERN = Pattern.compile("[0-9]+");
    private static final Pattern POSITION_PATTERN = Pattern.compile("^[A-Z]+[0-9]+$");

    public int getMovesCount(int fieldHeight, int fieldWidth, String startPosition, String endPosition) {
        return getMovesCount(fieldHeight, fieldWidth, convertPosition(startPosition), convertPosition(endPosition));
    }

    private int getMovesCount(int fieldHeight, int fieldWidth, Map.Entry<Integer, Integer> startPosition, Map.Entry<Integer, Integer> endPosition) {
        var field = new int[fieldWidth][fieldHeight];
        for (var row : field) {
            Arrays.fill(row, -1);
        }

        var queue = new LinkedList<Map.Entry<Integer, Integer>>();
        queue.addLast(startPosition);
        field[startPosition.getKey()][startPosition.getValue()] = 0;

        while (!queue.isEmpty()) {
            var position = queue.removeFirst();
            if (position.equals(endPosition)) {
                break;
            }

            var col = position.getKey();
            var row = position.getValue();

            for (int i = -2; i <= 2; i++) {
                if (i == 0) {
                    continue;
                }

                for (int j = -2; j <= 2; j++) {
                    if (j == 0 || Math.abs(j) == Math.abs(i)) {
                        continue;
                    }

                    var nextCol = col + i;
                    var nextRow = row + j;
                    if (nextCol >= fieldWidth || nextCol < 0 || nextRow >= fieldHeight || nextRow < 0) {
                        continue;
                    }

                    if (field[nextCol][nextRow] == -1) {
                        field[nextCol][nextRow] = field[col][row] + 1;
                        queue.addLast(Map.entry(nextCol, nextRow));
                    }
                }
            }
        }

        return field[endPosition.getKey()][endPosition.getValue()];
    }

    private Map.Entry<Integer, Integer> convertPosition(String position) {
        if (!POSITION_PATTERN.matcher(position).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var letterMatcher = LETTERS_PATTERN.matcher(position);
        letterMatcher.find();
        var col = (convertLettersToInt(letterMatcher.group()));

        var numberMatcher = NUMBERS_PATTERN.matcher(position);
        numberMatcher.find();
        var row = Integer.valueOf(numberMatcher.group()) - 1;

        return Map.entry(col, row);
    }

    private int convertLettersToInt(String letters) {
        if (!LETTERS_PATTERN.matcher(letters).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var result = 0;
        for (int i = 0; i < letters.length(); i++) {
            var c = letters.charAt(i);
            var charIndex = ALPHABET.indexOf(c);
            result = ALPHABET.length() * result + charIndex;
        }

        return result;
    }
}
