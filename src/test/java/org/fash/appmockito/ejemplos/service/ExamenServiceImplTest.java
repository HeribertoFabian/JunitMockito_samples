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

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepositoryImpl repository;
    @Mock
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;



    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this); primer forma de habilitar anotaciones
       // repository = mock(ExamenRepositoryImpl.class);
       // preguntaRepository = mock(PreguntaRepositoryImpl.class);
       // service = new ExamenServiceImpl(repository, preguntaRepository);

    }

    @Test
    void findExamenPorNombre() {

        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos);

        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertFalse(examen.isPresent());
        assertTrue(examen.isEmpty());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
    }


    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
       // when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertNull(examen);
        // verify(repository).findAll();
        // verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testGuardarExamen() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){

            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //When
        Examen examen = service.guardar(newExamen);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }
    @Test
    void testManejoException() {
        //Given
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, ()->{
            service.findExamenPorNombreConPreguntas("Matematicas");
        });
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        //verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg.equals(5L) ));
    }

    @Test
    void testArgumentMatchersWithNestedAclass() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        //El uso de una clase anonima nos permite personalizar el mensaje de error
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
    }

    public static class MiArgsMatchers  implements ArgumentMatcher<Long>{

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString(){
            return "Mensaje: " + this.argument + " debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class); //Se comenta para usar anotaciones
        verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        //when(preguntaRepository.guardarVarias(anyList())).thenThrow(IllegalArgumentException.class);
        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    @Test
    void testDoAnswer() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //doAnswer se utiliza cuando se quiere realizar un tratamiento de los datos antes de su retorno
        doAnswer(invocation -> {
           Long idExamen = invocation.getArgument(0);
           return idExamen == 5L ? Datos.PREGUNTAS : null;
        }).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertTrue(examen.getPreguntas().contains("Geometria"));
        assertEquals(5, examen.getPreguntas().size());

        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }


    @Test
    void testGuardarExamenDoAnswer() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        doAnswer(new Answer<Examen>(){

            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(repository).guardar(any(Examen.class));

        /*  La implementacion anterior de doAnswer remplaza el siguiente script
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){

            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });*/

        //When
        Examen examen = service.guardar(newExamen);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void doCallRealMethod() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
       // when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Mockito.doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    @Test
    void testSpy() {
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("Geometria");
      //  when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);  esto a??n imprime el mensaje PreguntaRepositoryImpl.findPreguntasPorExamenId
        //con el siguiente codigo ya no se invoca al metodo real
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Geometria"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());

    }
}


















