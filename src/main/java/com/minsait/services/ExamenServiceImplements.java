package com.minsait.services;

import com.minsait.models.Examen;
import com.minsait.repositories.ExamenRepository;
import com.minsait.repositories.PreguntasRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExamenServiceImplements implements  ExamenService{

    ExamenRepository examenRepository;
    PreguntasRepository preguntasRepository;
    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {

        return  examenRepository.findAll().stream().filter(examen -> examen.getNombre().equals(nombre)).findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional <Examen> examen =findExamenPorNombre(nombre);
        List<String> preguntas;
        Examen e=null;
        if(examen.isPresent()){
            e=examen.get();
            preguntas=preguntasRepository.findPreguntasByExamenId(e.getId());
            e.setPreguntas(preguntas);
        }
        return e;
    }

    @Override
    public Examen save(Examen examen) {

        if (examen.getPreguntas().isEmpty()) return examenRepository.save(examen);

        preguntasRepository.savePreguntas(examen.getPreguntas());

        return examenRepository.save(examen);

    }
}
