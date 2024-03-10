package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public Rating getMpa(int id) {
        log.info("Getting mpa (id = {})", id);
        return mpaStorage.getMpa(id).orElseThrow(() -> new NotFoundException("Mpa not found."));
    }

    public Collection<Rating> getAllMpa() {
        log.info("Getting all mpa");
        return mpaStorage.getAllMpa();
    }
}