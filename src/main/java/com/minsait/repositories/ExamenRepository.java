package com.minsait.repositories;

import com.minsait.models.Examen;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

//@ExtendWith(MockitoExtension.class)

public interface ExamenRepository {
    List<Examen> findAll();
    Examen save(Examen examen);
}
