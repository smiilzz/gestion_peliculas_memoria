package cl.usm.gestionPeliculasMemoria.repositories;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeliculasRepositoryImplTest {

    PeliculasRepository peliculasRepository;

    @BeforeEach
    void setUp() {
        // Implementamos un Setup-Method.
        peliculasRepository = new PeliculasRepositoryImpl();
    }


    @Test
    void insert_Ok() {
        Pelicula pelicula = new Pelicula();
        pelicula.setId("1");

        Pelicula result = peliculasRepository.insert(pelicula);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(1, peliculasRepository.findAll().size());
    }

    @Test
    void insert_IdNulo() {
        Pelicula pelicula = new Pelicula();
        // Al no asignar ID, simulamos el caso de ID nulo

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            peliculasRepository.insert(pelicula);
        });

        assertEquals("El ID de la pelicula no puede ser nulo", ex.getMessage());
    }

    @Test
    void insert_IdDuplicado() {
        // Insertamos la primera película
        Pelicula pelicula1 = new Pelicula();
        pelicula1.setId("1");
        peliculasRepository.insert(pelicula1);

        // Intentamos insertar una segunda con el mismo ID
        Pelicula pelicula2 = new Pelicula();
        pelicula2.setId("1");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            peliculasRepository.insert(pelicula2);
        });

        assertEquals("La pelicula con ID 1 ya existe", ex.getMessage());
    }


    @Test
    void findAll_Ok() {
        Pelicula p1 = new Pelicula();
        p1.setId("1");

        Pelicula p2 = new Pelicula();
        p2.setId("2");

        peliculasRepository.insert(p1);
        peliculasRepository.insert(p2);

        List<Pelicula> resultado = peliculasRepository.findAll();

        assertEquals(2, resultado.size());
    }


    @Test
    void findById_Ok() {
        Pelicula pelicula = new Pelicula();
        pelicula.setId("1");
        peliculasRepository.insert(pelicula);

        Pelicula result = peliculasRepository.findById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void findById_Nulo() {
        // Probamos enviando un null directamente
        Pelicula result = peliculasRepository.findById(null);

        assertNull(result);
    }

    @Test
    void findById_NoEncontrado() {
        Pelicula pelicula = new Pelicula();
        pelicula.setId("1");
        peliculasRepository.insert(pelicula);

        // Buscamos un ID que no existe
        Pelicula result = peliculasRepository.findById("999");

        assertNull(result);
    }
}