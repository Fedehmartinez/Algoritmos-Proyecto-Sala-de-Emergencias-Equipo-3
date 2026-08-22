package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.NoSuchElementException;
import ucu.edu.aed.implementaciones.Pila;

public class PilaTest {

    @Test
    public void meteElementoQuedaEnElTope(){
        Pila<Integer> pila = new Pila<>();

        pila.mete(10);

        assertEquals(Integer.valueOf(10), pila.tope());
        assertEquals(1, pila.tamaño());
    }

    @Test
    public void meteVariosElUltimoQuedaEnElTope(){
        Pila<Integer> pila = new Pila<>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(Integer.valueOf(30), pila.tope());
        assertEquals(3, pila.tamaño());
    }

    @Test
    public void meteLuegoDeVaciarVuelveAFuncionar(){
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);
        pila.vaciar();

        pila.mete(99);

        assertEquals(Integer.valueOf(99), pila.tope());
        assertEquals(1, pila.tamaño());
    }

    @Test
    public void meteElementosRepetidos(){
        Pila<String> pila = new Pila<>();

        pila.mete("a");
        pila.mete("a");

        assertEquals("a", pila.saca());
        assertEquals("a", pila.saca());
        assertTrue(pila.esVacio());
    }

    @Test
    public void sacaDevuelveElTopeYLoRemueve(){
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);
        pila.mete(20);

        Integer sacado = pila.saca();

        assertEquals(Integer.valueOf(20), sacado);
        assertEquals(Integer.valueOf(10), pila.tope());
        assertEquals(1, pila.tamaño());
    }

    @Test
    public void sacaRespetaElOrdenLifo(){
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(Integer.valueOf(30), pila.saca());
        assertEquals(Integer.valueOf(20), pila.saca());
        assertEquals(Integer.valueOf(10), pila.saca());
        assertTrue(pila.esVacio());
    }

    @Test(expected = NoSuchElementException.class)
    public void sacaPilaVacia(){
        Pila<Integer> pila = new Pila<>();

        pila.saca();
    }

    @Test(expected = NoSuchElementException.class)
    public void sacaMasElementosDeLosQueHay(){
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);
        pila.saca();

        pila.saca();
    }

    @Test
    public void topeNoRemueveElElemento(){
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);

        assertEquals(Integer.valueOf(10), pila.tope());

        assertEquals(Integer.valueOf(10), pila.tope());
        assertEquals(1, pila.tamaño());
    }

    @Test(expected = NoSuchElementException.class)
    public void topePilaVacia(){
        Pila<Integer> pila = new Pila<>();

        pila.tope();
    }

    @Test(expected = NoSuchElementException.class)
    public void topeLuegoDeVaciar(){
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);

        pila.vaciar();

        pila.tope();
    }

    @Test
    public void esVacioPilaRecienCreada(){
        Pila<Integer> pila = new Pila<>();

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    @Test
    public void esVacioLuegoDeMeter(){
        Pila<Integer> pila = new Pila<>();

        pila.mete(10);

        assertFalse(pila.esVacio());
    }

    @Test
    public void esVacioLuegoDeSacarTodo(){
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);
        pila.mete(20);

        pila.saca();
        pila.saca();

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    @Test
    public void tamanioAcompanaAMeteYSaca(){
        Pila<Integer> pila = new Pila<>();
        assertEquals(0, pila.tamaño());

        pila.mete(10);
        assertEquals(1, pila.tamaño());
        pila.mete(20);
        assertEquals(2, pila.tamaño());

        pila.saca();
        assertEquals(1, pila.tamaño());
        pila.saca();
        assertEquals(0, pila.tamaño());
    }

    @Test
    public void esVacioYTamanioNoSeContradicen(){
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        pila.vaciar();

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }
}
