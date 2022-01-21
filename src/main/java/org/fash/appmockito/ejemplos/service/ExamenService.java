package org.fash.appmockito.ejemplos.service;

import org.fash.appmockito.ejemplos.models.Examen;

public interface ExamenService {
    Examen findExamenPorNombre(String nombre);
}
