package org.fash.appmockito.ejemplos.service;

import org.fash.appmockito.ejemplos.constants.Datos;
import org.fash.appmockito.ejemplos.models.Examen;
import org.fash.appmockito.ejemplos.repository.ExamenRepository;
import org.fash.appmockito.ejemplos.repository.ExamenRepositoryImpl;
import org.fash.appmockito.ejemplos.repository.PreguntaRepository;
import org.fash.appmockito.ejemplos.repository.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {

    @Spy
    ExamenRepositoryImpl repository; //No puede ser la interfaz, tiene que ser la implementacion
    @Spy
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Test
    void testSpy() {
        List<String> preguntas = Arrays.asList("Geometria");
      //  when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);  esto aún imprime el mensaje PreguntaRepositoryImpl.findPreguntasPorExamenId
        //con el siguiente codigo ya no se invoca al metodo real
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Geometria"));

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());

    }
}

















