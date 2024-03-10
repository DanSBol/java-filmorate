package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Rating getMpa(@PathVariable(value = "id") Integer id) {
        return mpaService.getMpa(id);
    }

    @GetMapping
    public Collection<Rating> getAllMpa() {
        return mpaService.getAllMpa();
    }
}