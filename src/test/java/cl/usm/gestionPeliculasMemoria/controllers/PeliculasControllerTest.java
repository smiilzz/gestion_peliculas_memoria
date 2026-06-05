package cl.usm.gestionPeliculasMemoria.controllers;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.services.PeliculasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculasControllerTest {

    PeliculasController peliculasController;

    @Mock
    PeliculasService peliculasService;

    @BeforeEach
    void setUp() {
        this.peliculasController = new PeliculasController(peliculasService);
    }


    @Test
    void getAll_SinFiltro_Ok() {
        List<Pelicula> catalogoCompleto = new ArrayList<>();
        when(peliculasService.getAll()).thenReturn(catalogoCompleto);

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAll_ConFiltro_Ok() {
        List<Pelicula> resultadosBusqueda = new ArrayList<>();
        when(peliculasService.filter("Cyberpunk")).thenReturn(resultadosBusqueda);

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll("Cyberpunk");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAll_Error() {
        when(peliculasService.getAll()).thenThrow(new RuntimeException("Caída interna simulada"));

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void createPelicula_Ok() {
        Pelicula entidadNueva = new Pelicula();
        when(peliculasService.createPelicula(any(Pelicula.class))).thenReturn(entidadNueva);

        ResponseEntity<?> response = peliculasController.createPelicula(entidadNueva);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createPelicula_Nok() {
        Pelicula entidadMala = new Pelicula();
        // Simulamos que el servicio devolvió null al intentar crear
        when(peliculasService.createPelicula(any(Pelicula.class))).thenReturn(null);

        ResponseEntity<?> response = peliculasController.createPelicula(entidadMala);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void findById_Ok() {
        Pelicula peliEncontrada = new Pelicula();
        when(peliculasService.findById("LogiTrust-ID")).thenReturn(peliEncontrada);

        ResponseEntity<Pelicula> response = peliculasController.findById("LogiTrust-ID");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findById_Nok() {
        when(peliculasService.findById("Habitik-ID")).thenReturn(null);

        ResponseEntity<Pelicula> response = peliculasController.findById("Habitik-ID");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findById_Error() {
        when(peliculasService.findById(anyString())).thenThrow(new RuntimeException("Fallo en base de datos"));

        ResponseEntity<Pelicula> response = peliculasController.findById("999");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void getComentarios_Ok() {
        Pelicula peliConReviews = new Pelicula();
        // Ya no intentamos meterle Strings falsos, con que la entidad exista basta
        when(peliculasService.findById("DarkSouls-ID")).thenReturn(peliConReviews);

        ResponseEntity<?> response = peliculasController.getComentarios("DarkSouls-ID");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getComentarios_Nok() {
        when(peliculasService.findById("ResidentEvil-ID")).thenReturn(null);

        ResponseEntity<?> response = peliculasController.getComentarios("ResidentEvil-ID");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getComentarios_Error() {
        when(peliculasService.findById(anyString())).thenThrow(new RuntimeException("Error obteniendo reviews"));

        ResponseEntity<?> response = peliculasController.getComentarios("000");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}