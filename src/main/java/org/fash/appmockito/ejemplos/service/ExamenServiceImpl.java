package org.fash.appmockito.ejemplos.service;

import org.fash.appmockito.ejemplos.models.Examen;
import org.fash.appmockito.ejemplos.repository.ExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {

    private ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Examen findExamenPorNombre(String nombre) {
        Optional<Examen> examenOpt = examenRepository.findAll().stream()
                .filter(e -> (e.getNombre().contains(nombre))).findFirst();
        Examen examen = null;
        if (examenOpt.isPresent()) examen = examenOpt.orElseThrow();

        return examen;
    }
}
