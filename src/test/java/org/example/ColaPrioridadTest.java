package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import ucu.edu.aed.implementaciones.ColaPrioridad;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class ColaPrioridadTest {

    @Test
    public void colaRecienCreadaEsVacia() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    @Test
    public void frenteNoRemueveElemento() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.poneEnCola(10);
        cola.poneEnCola(20);

        assertEquals(Integer.valueOf(10), cola.frente());
        assertEquals(Integer.valueOf(10), cola.frente());
        assertEquals(2, cola.tamaño());
    }

    @Test
    public void unUnicoElementoSaleYLaColaQuedaVacia() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.poneEnCola(42);

        assertEquals(Integer.valueOf(42), cola.quitaDeCola());
        assertTrue(cola.esVacio());
    }

    @Test(expected = NoSuchElementException.class)
    public void frenteEnColaVacia() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.frente();
    }

    @Test(expected = NoSuchElementException.class)
    public void quitaDeColaEnColaVacia() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.quitaDeCola();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void agregarEnPosicionNoEstaSoportado() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.agregar(0, 10);
    }

    @Test
    public void poneEnColaOrdenaPorPrioridad() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.poneEnCola(30);
        cola.poneEnCola(10);
        cola.poneEnCola(20);

        assertEquals(Integer.valueOf(10), cola.quitaDeCola());
        assertEquals(Integer.valueOf(20), cola.quitaDeCola());
        assertEquals(Integer.valueOf(30), cola.quitaDeCola());
        assertTrue(cola.esVacio());
    }

    @Test
    public void entreIgualesRespetaOrdenDeLlegada() {
        Comparator<String> porLongitud = Comparator.comparingInt(String::length);
        ColaPrioridad<String> cola = new ColaPrioridad<String>(porLongitud);
        cola.poneEnCola("aa");
        cola.poneEnCola("bb");
        cola.poneEnCola("cc");

        assertEquals("aa", cola.quitaDeCola());
        assertEquals("bb", cola.quitaDeCola());
        assertEquals("cc", cola.quitaDeCola());
    }

    @Test
    public void elMasPrioritarioInsertadoUltimoQuedaEnElFrente() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.poneEnCola(20);
        cola.poneEnCola(30);
        cola.poneEnCola(10);

        assertEquals(Integer.valueOf(10), cola.frente());
        assertEquals(3, cola.tamaño());
    }

    @Test
    public void elMenosPrioritarioQuedaAlFinal() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(3, cola.tamaño());
        assertEquals(Integer.valueOf(10), cola.quitaDeCola());
        assertEquals(Integer.valueOf(20), cola.quitaDeCola());
        assertEquals(Integer.valueOf(30), cola.quitaDeCola());
    }

    @Test
    public void agregarDelegaEnPoneEnCola() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.poneEnCola(20);
        cola.poneEnCola(30);
        cola.agregar(10);

        assertEquals(Integer.valueOf(10), cola.frente());
        assertEquals(3, cola.tamaño());
    }

    @Test
    public void vaciarDejaLaColaUsable() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<Integer>(Comparator.naturalOrder());
        cola.poneEnCola(30);
        cola.poneEnCola(10);
        cola.poneEnCola(20);

        cola.vaciar();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());

        cola.poneEnCola(20);
        cola.poneEnCola(10);

        assertEquals(Integer.valueOf(10), cola.quitaDeCola());
        assertEquals(Integer.valueOf(20), cola.quitaDeCola());
    }

    @Test(expected = NoSuchElementException.class)
    public void constructorRechazaComparadorNull() {
        new ColaPrioridad<Integer>(null);
    }
}