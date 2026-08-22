package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ucu.edu.aed.implementaciones.Conjunto;

public class ConjuntoTest {

    @Test
    public void unionConElementosComunes() {
        Conjunto<Integer> conjunto1 = new Conjunto<>();
        Conjunto<Integer> conjunto2 = new Conjunto<>();

        conjunto1.agregar(10);
        conjunto1.agregar(20);
        conjunto1.agregar(30);

        conjunto2.agregar(20);
        conjunto2.agregar(30);
        conjunto2.agregar(40);

        Conjunto<Integer> resultado = (Conjunto<Integer>) conjunto1.union(conjunto2);

        assertEquals(4, resultado.tamaño());
        assertTrue(resultado.contiene(10));
        assertTrue(resultado.contiene(20));
        assertTrue(resultado.contiene(30));
        assertTrue(resultado.contiene(40));
    }

    @Test
    public void interseccionConElementosComunes() {
        Conjunto<Integer> conjunto1 = new Conjunto<>();
        Conjunto<Integer> conjunto2 = new Conjunto<>();

        conjunto1.agregar(10);
        conjunto1.agregar(20);
        conjunto1.agregar(30);

        conjunto2.agregar(20);
        conjunto2.agregar(30);
        conjunto2.agregar(40);

        Conjunto<Integer> resultado = (Conjunto<Integer>) conjunto1.interseccion(conjunto2);

        assertEquals(2, resultado.tamaño());
        assertTrue(resultado.contiene(20));
        assertTrue(resultado.contiene(30));
        assertFalse(resultado.contiene(10));
        assertFalse(resultado.contiene(40));
    }

    @Test
    public void unionSinElementosComunes() {
        Conjunto<Integer> conjunto1 = new Conjunto<>();
        Conjunto<Integer> conjunto2 = new Conjunto<>();

        conjunto1.agregar(10);
        conjunto1.agregar(20);

        conjunto2.agregar(30);
        conjunto2.agregar(40);

        Conjunto<Integer> resultado = (Conjunto<Integer>) conjunto1.union(conjunto2);

        assertEquals(4, resultado.tamaño());
        assertTrue(resultado.contiene(10));
        assertTrue(resultado.contiene(20));
        assertTrue(resultado.contiene(30));
        assertTrue(resultado.contiene(40));
    }

    @Test
    public void interseccionSinElementosComunes() {
        Conjunto<Integer> conjunto1 = new Conjunto<>();
        Conjunto<Integer> conjunto2 = new Conjunto<>();

        conjunto1.agregar(10);
        conjunto1.agregar(20);

        conjunto2.agregar(30);
        conjunto2.agregar(40);

        Conjunto<Integer> resultado = (Conjunto<Integer>) conjunto1.interseccion(conjunto2);

        assertEquals(0, resultado.tamaño());
        assertTrue(resultado.esVacio());
    }
}