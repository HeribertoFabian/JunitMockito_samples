package org.fash.appmockito.ejemplos.repository;

import org.fash.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {

    List<Examen> findAll();
}
