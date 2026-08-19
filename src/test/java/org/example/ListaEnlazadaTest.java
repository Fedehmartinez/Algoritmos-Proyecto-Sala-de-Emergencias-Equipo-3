package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import ucu.edu.aed.implementaciones.ListaEnlazada;

public class ListaEnlazadaTest {

    @Test
    public void removerIndiceElementoExistente(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        Integer removido = lista.remover(1);

        assertEquals(Integer.valueOf(20), removido);
        assertEquals(2, lista.tamaño());
        assertFalse(lista.contiene(20));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removerIndiceFueraDeRango(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);

        lista.remover(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removerIndiceListaVacia(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

        lista.remover(0);
    }

    @Test
    public void removerElementoExistente(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        boolean resultado = lista.remover(Integer.valueOf(20));

        assertTrue(resultado);
        assertEquals(2, lista.tamaño());
        assertFalse(lista.contiene(20));
    }

    @Test
    public void removerElementoInexistente(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);

        boolean resultado = lista.remover(Integer.valueOf(99));

        assertFalse(resultado);
        assertEquals(2, lista.tamaño());
    }

    @Test
    public void removerElementoListaVacia(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

        boolean resultado = lista.remover(Integer.valueOf(10));

        assertFalse(resultado);
        assertTrue(lista.esVacio());
    }

    @Test
    public void removerCabezaDejaSiguienteEnNull(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);

        Integer removido = lista.remover(0);

        assertEquals(Integer.valueOf(10), removido);
        assertEquals(1, lista.tamaño());
    }
}