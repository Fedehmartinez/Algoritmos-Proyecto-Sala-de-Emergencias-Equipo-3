package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import ucu.edu.aed.implementaciones.Cola;
import java.util.NoSuchElementException;

public class ColaTest {
  @Test
  public void poneEnColaYFrenteRespetaOrden() {
    Cola<Integer> cola = new Cola<>();
    cola.poneEnCola(10);
    cola.poneEnCola(20);

    assertEquals(Integer.valueOf(10), cola.frente());
  }

  @Test
  public void quitaDeColaDevuelveElementosEnOrden() {
    Cola<Integer> cola = new Cola<>();
    cola.poneEnCola(10);
    cola.poneEnCola(20);
    cola.poneEnCola(30);

    assertEquals(Integer.valueOf(10), cola.quitaDeCola());
    assertEquals(Integer.valueOf(20), cola.quitaDeCola());
    assertEquals(1, cola.tamaño());
  }

  @Test
  public void colaRecienCreadaEsVacia() {
    Cola<Integer> cola = new Cola<>();
    
    assertTrue(cola.esVacio());
  }

  @Test
  public void frenteNoRemueveElemento() {
    Cola<Integer> cola = new Cola<>();
    cola.poneEnCola(10);

    assertFalse(cola.esVacio());
  }

  @Test(expected = NoSuchElementException.class)
  public void quitaDeColaEnColaVacia() {
    Cola<Integer> cola = new Cola<>();
    cola.quitaDeCola();
  }

  @Test(expected = NoSuchElementException.class)
  public void frenteEnColaVacia() {
    Cola<Integer> cola = new Cola<>();
    cola.frente();
  }
}



