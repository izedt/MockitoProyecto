import com.minsait.models.Examen;
import com.minsait.repositories.PreguntasRepository;
import com.minsait.repositories.ExamenRepository;
import com.minsait.services.ExamenServiceImplements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ExamenServiceImplTest {
    @Mock
    ExamenRepository examenRepository;
    @Mock
    PreguntasRepository preguntasRepository;

    @Captor
    ArgumentCaptor<Long> captor;

    @InjectMocks
    ExamenServiceImplements service;

    @Test
    void testArgumentCaptor(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when((preguntasRepository.findPreguntasByExamenId(anyLong()))).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(preguntasRepository).findPreguntasByExamenId(captor.capture());
        assertEquals(1L,captor.getValue());

    }

    @Test
    void  testFindExamenPorNombre(){
        //Given
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //When
        String nombre = "Matematicas";

        Optional<Examen> examen=service.findExamenPorNombre(nombre);
        //Then
        assertTrue(examen.isPresent());
        assertEquals(nombre, examen.get().getNombre());
    }

    @Test
    void  testfindExamenPorNombreConPreguntas(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when((preguntasRepository.findPreguntasByExamenId(anyLong()))).thenReturn(Datos.PREGUNTAS);

        Examen examen= service.findExamenPorNombreConPreguntas("Fisica");

        assertFalse(examen.getPreguntas().isEmpty());
        assertTrue(examen.getPreguntas().contains("Aritmetica"));

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasByExamenId(2L);
    }
@Test
    void testExceptions(){
        //Given
    when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
    when((preguntasRepository.findPreguntasByExamenId(anyLong()))).thenThrow(IllegalArgumentException.class);
    String nombre = "Matematicas";

    //When //Then
    assertThrows(IllegalArgumentException.class,()->service.findExamenPorNombreConPreguntas(nombre));
}


@Test
    void testSaveException(){
    //given
    Examen examen = Datos.EXAMEN;
    examen.setPreguntas(Datos.PREGUNTAS);
    doThrow(IllegalArgumentException.class).when(preguntasRepository).savePreguntas(anyList());


    //when
    assertThrows(IllegalArgumentException.class, ()->service.save(examen).setPreguntas(anyList()));


    verify(examenRepository,times(0)).save(examen);

}

@Test
    void testAddPregunta() {
        // given
        Examen examen = new Examen(1L, "Matematicas");

        // when
        examen.addPregunta("Pregunta 1");
        examen.addPregunta("Pregunta 2");

        // then
        assertEquals(2, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Pregunta 1"));
        assertTrue(examen.getPreguntas().contains("Pregunta 2"));
    }

@Test
    void  testDoAnswer(){
        //given
    when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
    //when(preguntasRepository.findPreguntasByExamenId(1L)).thenReturn(Datos.PREGUNTAS);
    //when(preguntasRepository.findPreguntasByExamenId(2L)).thenReturn(Collections.emptyList());

    doAnswer( invocationOnMock -> {
        Long id = invocationOnMock.getArgument(0);
        return id==1L?Datos.PREGUNTAS:Collections.EMPTY_LIST;
    }).when(preguntasRepository).findPreguntasByExamenId(anyLong());

    //when
    Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

    assertAll(
            ()->assertEquals(1L,examen.getId(),"El examen no es matematicas"),
            ()->assertFalse(examen.getPreguntas().isEmpty(), "El examen no es matematicas")
            //()->assertTrue(examen.getPreguntas().isEmpty(),"El examen es matematicas")
    );



}

    @Test
    void testSaveExamenConPreguntas() {

        // given
        Examen examenConPreguntas = new Examen(null, "Ciencias");
        examenConPreguntas.setPreguntas(Arrays.asList("Pregunta1", "Pregunta2"));
        AtomicLong nextId = new AtomicLong(5L);

        doAnswer(invocationOnMock -> {
            //Examen nuevoExamen = new Examen((long) (Datos.EXAMENES.size() + 1), examenConPreguntas.getNombre());// empieza desde 4 porque Historia nunca es añadido a la lista
            Examen nuevoExamen = new Examen(nextId.getAndIncrement(), examenConPreguntas.getNombre());//index de forma manual
            nuevoExamen.setPreguntas(examenConPreguntas.getPreguntas());
            return nuevoExamen;
        }).when(examenRepository).save(any());

        // when
        Examen savedExamenConPreguntas = service.save(examenConPreguntas);
        System.out.println(examenConPreguntas.getId());
        System.out.println(savedExamenConPreguntas.getId());

        // then
        assertNotNull(savedExamenConPreguntas.getId(), "ID no fue generado");
        assertTrue(Datos.EXAMENES.getLast().getId()<savedExamenConPreguntas.getId());
        assertEquals(examenConPreguntas.getPreguntas(), savedExamenConPreguntas.getPreguntas());
        assertNotEquals(Collections.EMPTY_LIST, savedExamenConPreguntas.getPreguntas());
        verify(examenRepository).save(examenConPreguntas);
        verify(preguntasRepository).savePreguntas(anyList());

    }

    @Test
    void testSaveExamenSinPreguntas() {
        // given
        Examen examensinPreguntas = new Examen(null, "Artes");
        AtomicLong nextId = new AtomicLong(5L);

        doAnswer(invocationOnMock -> {
            //Examen nuevoExamen = new Examen((long) (Datos.EXAMENES.size() + 1), examensinPreguntas.getNombre());// empieza desde 4 porque Historia nunca es añadido a la lista
            Examen nuevoExamen = new Examen(nextId.getAndIncrement(), examensinPreguntas.getNombre());//index de forma manual
            nuevoExamen.setPreguntas(examensinPreguntas.getPreguntas());
            return nuevoExamen;
        }).when(examenRepository).save(any());

        // when
        Examen savedExamenSinPreguntas = service.save(examensinPreguntas);

        // then
        assertNotNull(savedExamenSinPreguntas.getId(), "ID no fue generado");
        assertEquals("Artes", savedExamenSinPreguntas.getNombre());
        assertTrue(Datos.EXAMENES.getLast().getId()<savedExamenSinPreguntas.getId());
        assertEquals(Collections.EMPTY_LIST, savedExamenSinPreguntas.getPreguntas(), "Se esperaba una lista vacia");
        verify(examenRepository).save(examensinPreguntas);
        verifyNoInteractions(preguntasRepository);
    }




}
