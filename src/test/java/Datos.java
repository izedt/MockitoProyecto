import com.minsait.models.Examen;
import com.minsait.repositories.PreguntasRepository;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Datos {
    public static final List<Examen> EXAMENES= Arrays.asList(
            new Examen(1L, "Matematicas"),
            new Examen(2L, "Fisica"),
            new Examen(3L, "Quimica")
    );

    public static final Examen EXAMEN = new Examen (4L, "Historia");


    public static final List<String> PREGUNTAS = Arrays.asList(
            "Aritmetica",
            "Matematicas",
            "Integrales",
            "Derivadas"
    );




}
