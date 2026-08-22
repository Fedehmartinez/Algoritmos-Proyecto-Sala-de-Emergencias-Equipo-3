package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Test;

import ucu.edu.aed.implementaciones.ColaCircular;

public class ColaCircularTest {

    @Test
    public void colaRecienCreadaEsVacia(){
        ColaCircular<Integer> cola = new ColaCircular<>();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    @Test
    public void poneEnColaYFrenteRespetaOrden(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.poneEnCola(10);
        cola.poneEnCola(20);

        assertEquals(Integer.valueOf(10), cola.frente());
        assertEquals(2, cola.tamaño());
    }

    @Test
    public void quitaDeColaDevuelveElementosEnOrdenFifo(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(Integer.valueOf(10), cola.quitaDeCola());
        assertEquals(Integer.valueOf(20), cola.quitaDeCola());
        assertEquals(1, cola.tamaño());
        assertEquals(Integer.valueOf(30), cola.frente());
    }

    @Test(expected = NoSuchElementException.class)
    public void frenteEnColaVacia(){
        ColaCircular<Integer> cola = new ColaCircular<>();

        cola.frente();
    }

    @Test(expected = NoSuchElementException.class)
    public void quitaDeColaEnColaVacia(){
        ColaCircular<Integer> cola = new ColaCircular<>();

        cola.quitaDeCola();
    }

    @Test
    public void poneEnColaFallaCuandoLlegaALaCapacidad(){
        ColaCircular<Integer> cola = new ColaCircular<>(2);
        cola.poneEnCola(10);
        cola.poneEnCola(20);

        boolean resultado = cola.poneEnCola(30);

        assertFalse(resultado);
        assertEquals(2, cola.tamaño());
    }

    @Test
    public void reutilizaHuecosAlDarVueltaAlVector(){
        ColaCircular<Integer> cola = new ColaCircular<>(3);
        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);
        cola.quitaDeCola();
        cola.quitaDeCola();

        boolean resultado = cola.poneEnCola(40);

        assertTrue(resultado);
        assertEquals(Integer.valueOf(30), cola.frente());
        assertEquals(2, cola.tamaño());
        cola.quitaDeCola();
        assertEquals(Integer.valueOf(40), cola.frente());
    }

    @Test
    public void agregarCreceAutomaticamenteAunEstandoLlena(){
        ColaCircular<Integer> cola = new ColaCircular<>(2);
        cola.agregar(10);
        cola.agregar(20);
        cola.agregar(30);

        assertEquals(3, cola.tamaño());
        assertEquals(Integer.valueOf(10), cola.obtener(0));
        assertEquals(Integer.valueOf(20), cola.obtener(1));
        assertEquals(Integer.valueOf(30), cola.obtener(2));
    }

    @Test
    public void agregarEnIndiceDesplazaLosSiguientes(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);
        cola.agregar(30);

        cola.agregar(1, 20);

        assertEquals(3, cola.tamaño());
        assertEquals(Integer.valueOf(10), cola.obtener(0));
        assertEquals(Integer.valueOf(20), cola.obtener(1));
        assertEquals(Integer.valueOf(30), cola.obtener(2));
    }

    @Test
    public void agregarEnIndiceFuncionaTrasDarVueltaAlVector(){
        ColaCircular<Integer> cola = new ColaCircular<>(3);
        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);
        cola.quitaDeCola();
        cola.poneEnCola(40);

        cola.agregar(1, 99);

        assertEquals(Integer.valueOf(20), cola.obtener(0));
        assertEquals(Integer.valueOf(99), cola.obtener(1));
        assertEquals(Integer.valueOf(30), cola.obtener(2));
        assertEquals(Integer.valueOf(40), cola.obtener(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void obtenerIndiceFueraDeRango(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);

        cola.obtener(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void obtenerEnColaVacia(){
        ColaCircular<Integer> cola = new ColaCircular<>();

        cola.obtener(0);
    }

    @Test
    public void removerPorIndiceDesplazaLosSiguientes(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);
        cola.agregar(20);
        cola.agregar(30);

        Integer removido = cola.remover(1);

        assertEquals(Integer.valueOf(20), removido);
        assertEquals(2, cola.tamaño());
        assertEquals(Integer.valueOf(10), cola.obtener(0));
        assertEquals(Integer.valueOf(30), cola.obtener(1));
    }

    @Test
    public void removerPorIndiceFuncionaTrasDarVueltaAlVector(){
        ColaCircular<Integer> cola = new ColaCircular<>(3);
        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);
        cola.quitaDeCola();
        cola.poneEnCola(40);

        Integer removido = cola.remover(1);

        assertEquals(Integer.valueOf(30), removido);
        assertEquals(2, cola.tamaño());
        assertEquals(Integer.valueOf(20), cola.obtener(0));
        assertEquals(Integer.valueOf(40), cola.obtener(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removerIndiceFueraDeRango(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);

        cola.remover(5);
    }

    @Test
    public void removerElementoExistente(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);
        cola.agregar(20);
        cola.agregar(30);

        boolean resultado = cola.remover(Integer.valueOf(20));

        assertTrue(resultado);
        assertEquals(2, cola.tamaño());
        assertFalse(cola.contiene(20));
    }

    @Test
    public void removerElementoInexistente(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);

        boolean resultado = cola.remover(Integer.valueOf(99));

        assertFalse(resultado);
        assertEquals(1, cola.tamaño());
    }

    @Test
    public void contieneYIndiceDeElementoExistente(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);
        cola.agregar(20);

        assertTrue(cola.contiene(20));
        assertEquals(1, cola.indiceDe(20));
    }

    @Test
    public void contieneYIndiceDeElementoInexistente(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);

        assertFalse(cola.contiene(99));
        assertEquals(-1, cola.indiceDe(99));
    }

    @Test
    public void buscarPorCriterio(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);
        cola.agregar(15);
        cola.agregar(20);

        Integer encontrado = cola.buscar(n -> n % 2 != 0);

        assertEquals(Integer.valueOf(15), encontrado);
    }

    @Test
    public void buscarSinCoincidenciasDevuelveNull(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);
        cola.agregar(20);

        Integer encontrado = cola.buscar(n -> n > 100);

        assertEquals(null, encontrado);
    }

    @Test
    public void ordenarDevuelveNuevaColaOrdenadaSinModificarLaOriginal(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(30);
        cola.agregar(10);
        cola.agregar(20);

        ColaCircular<Integer> ordenada = (ColaCircular<Integer>) cola.ordenar((a, b) -> a - b);

        assertEquals(Integer.valueOf(10), ordenada.obtener(0));
        assertEquals(Integer.valueOf(20), ordenada.obtener(1));
        assertEquals(Integer.valueOf(30), ordenada.obtener(2));
        assertEquals(Integer.valueOf(30), cola.obtener(0));
    }

    @Test
    public void vaciarDejaLaColaEnEstadoInicial(){
        ColaCircular<Integer> cola = new ColaCircular<>();
        cola.agregar(10);
        cola.agregar(20);

        cola.vaciar();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
        assertTrue(cola.poneEnCola(99));
        assertEquals(Integer.valueOf(99), cola.frente());
    }
}
