package org.fash.appmockito.ejemplos.service;

import org.fash.appmockito.ejemplos.models.Examen;
import org.fash.appmockito.ejemplos.repository.ExamenRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {

    @Test
    void findExamenPorNombre() {
        ExamenRepository repository = mock(ExamenRepository.class);
        ExamenService service = new ExamenServiceImpl(repository);
        List<Examen> datos = Arrays.asList(new Examen(5L,"Matematicas"),
                new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"));
        when(repository.findAll()).thenReturn(datos);


        Examen examen = service.findExamenPorNombre("Matematicas");

        assertNotNull(examen);
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }
}