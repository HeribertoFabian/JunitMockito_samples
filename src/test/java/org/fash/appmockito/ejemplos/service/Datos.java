package org.fash.appmockito.ejemplos.service;

import org.fash.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public static final List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Matematicas"),
            new Examen(6L, "Lenguaje"), new Examen(7L, "Historia"));

    public final static List<String> PREGUNTAS = Arrays.asList("aritmetica", "integrales",
            "derivadas", "trigonometria", "Geometria");
}
