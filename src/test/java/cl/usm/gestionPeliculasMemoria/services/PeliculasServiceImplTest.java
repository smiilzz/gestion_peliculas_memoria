package cl.usm.gestionPeliculasMemoria.services;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.repositories.PeliculasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculasServiceImplTest {

    PeliculasServiceImpl peliculasService;

    @Mock
    PeliculasRepository peliculasRepository;

    @BeforeEach
    void setUp() {
        this.peliculasService = new PeliculasServiceImpl(peliculasRepository);
    }

    @Test
    void createPelicula_Ok() {
        Pelicula pelicula = new Pelicula();
        pelicula.setId("1");

        // Simulamos que al insertar el repositorio nos devuelve la película
        when(peliculasRepository.insert(any(Pelicula.class))).thenReturn(pelicula);

        Pelicula result = peliculasService.createPelicula(pelicula);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertNotNull(pelicula.getTokenDescarga());
    }

    @Test
    void createPelicula_Exception() {
        Pelicula pelicula = new Pelicula();

        // Forzamos un error en el repositorio para que entre al bloque catch
        when(peliculasRepository.insert(any(Pelicula.class))).thenThrow(new RuntimeException("Error simulado"));

        Pelicula result = peliculasService.createPelicula(pelicula);

        // El catch retorna null
        assertNull(result);
    }


    @Test
    void getAll_Ok() {
        Pelicula p1 = new Pelicula();
        Pelicula p2 = new Pelicula();

        // Simulamos que devuelve una lista de 2 películas
        when(peliculasRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Pelicula> result = peliculasService.getAll();

        assertEquals(2, result.size());
    }


    @Test
    void findById_Ok() {
        Pelicula pelicula = new Pelicula();
        pelicula.setId("100");

        // Simulamos la búsqueda
        when(peliculasRepository.findById("100")).thenReturn(pelicula);

        Pelicula result = peliculasService.findById("100");

        assertNotNull(result);
        assertEquals("100", result.getId());
    }


    @Test
    void filter_Ok() {
        Pelicula p1 = new Pelicula();
        p1.setId("1");
        p1.setTitulo("Harry Potter");

        Pelicula p2 = new Pelicula();
        p2.setId("2");
        p2.setTitulo("Ponyo");

        when(peliculasRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // 1. Probamos filtrando por título
        List<Pelicula> resultTitulo = peliculasService.filter("harry");
        assertEquals(1, resultTitulo.size());
        assertEquals("Harry Potter", resultTitulo.get(0).getTitulo());

        // 2. Probamos filtrando por ID
        List<Pelicula> resultId = peliculasService.filter("2");
        assertEquals(1, resultId.size());
        assertEquals("Ponyo", resultId.get(0).getTitulo());

        // 3. Probamos un filtro que no coincida con nada
        List<Pelicula> resultVacio = peliculasService.filter("Avatar");
        assertEquals(0, resultVacio.size());
    }
}