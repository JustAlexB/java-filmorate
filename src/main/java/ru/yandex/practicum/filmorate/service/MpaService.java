package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;
    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Optional<Mpa> getMpaByID(Integer mpaID){
        return mpaStorage.getMpaByID(mpaID);
    }

    public Collection<Mpa> getAllMpa() {
        return new ArrayList<>(mpaStorage.getAllMpa());
    }
}
