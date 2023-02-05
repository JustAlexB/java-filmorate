package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Mpa> getAllMpa() {
        log.info("Запрошены все mpa");
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public Optional<Mpa> getMpaByID(@PathVariable("id") Integer mpaID) {
        log.info("Запрошен mpa c ID = {}", mpaID);
        Optional<Mpa> mpa = mpaService.getMpaByID(mpaID);
        if(mpa.isEmpty())
            throw new NotFoundException("MPA c ID: " + mpaID + " не найден");
        return mpa;
    }
}
