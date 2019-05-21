package com.kiseru.fix.fixtask.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.regex.Pattern;

@Service
public class HorseService {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Pattern LETTERS_PATTERN = Pattern.compile("[A-Z]+");
    private static final Pattern NUMBERS_PATTERN = Pattern.compile("[0-9]+");
    private static final Pattern POSITION_PATTERN = Pattern.compile("^[A-Z]+[0-9]+$");

    public int getMovesCount(int fieldHeight, int fieldWidth, String startPosition, String endPosition) {
        return 0;
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
