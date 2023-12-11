package com.minsait.repositories;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

//@ExtendWith(MockitoExtension.class)

public interface PreguntasRepository {
    List<String> findPreguntasByExamenId(Long id);

    void savePreguntas(List<String> preguntas);
}
