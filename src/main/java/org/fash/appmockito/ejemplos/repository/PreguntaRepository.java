package org.fash.appmockito.ejemplos.repository;

import java.util.List;

public interface PreguntaRepository {
    List<String> findPreguntasPorExamenId(Long id);

}