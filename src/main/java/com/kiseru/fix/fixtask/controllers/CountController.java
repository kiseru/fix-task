package com.kiseru.fix.fixtask.controllers;

import com.kiseru.fix.fixtask.dto.MovesDto;
import com.kiseru.fix.fixtask.services.HorseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CountController.URI)
public class CountController {

    static final String URI = "/hourse/rest/count";

    private final HorseService horseService;

    @Autowired
    public CountController(HorseService horseService) {
        this.horseService = horseService;
    }

    @GetMapping
    public MovesDto getMovesCount(@RequestParam int height, @RequestParam int width, @RequestParam String start,
                                  @RequestParam String end) {
        var moves = horseService.getMovesCount(height, width, start, end);
        return new MovesDto(moves);
    }
}
