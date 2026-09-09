package org.example;

import junit.framework.TestCase;
import ucu.edu.aed.clases.NivelCatalogo;
import ucu.edu.aed.clases.NodoCatalogo;

public class NodoCatalogoTest extends TestCase {

    public void testGuardaLoQueSeLePaso(){
        NodoCatalogo nodo = new NodoCatalogo("I", "Enfermedades del sistema circulatorio", NivelCatalogo.CAPITULO);
        assertEquals("I", nodo.getCodigo());
        assertEquals("Enfermedades del sistema circulatorio", nodo.getNombre());
        assertEquals(NivelCatalogo.CAPITULO, nodo.getNivel());
    }

    public void testCodigoNuloLanzaExcepcion(){
        try {
            new NodoCatalogo(null, "nombre", NivelCatalogo.GRUPO);
            fail("Debia lanzar excepcion");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testCodigoVacioLanzaExcepcion(){
        try {
            new NodoCatalogo("   ", "nombre", NivelCatalogo.GRUPO);
            fail("Debia lanzar excepcion");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testNombreNuloLanzaExcepcion(){
        try {
            new NodoCatalogo("I21", null, NivelCatalogo.CODIGO);
            fail("Debia lanzar excepcion");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testNivelNuloLanzaExcepcion(){
        try {
            new NodoCatalogo("I21", "Infarto", null);
            fail("Debia lanzar excepcion");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testOrdenaPorCodigo(){
        NodoCatalogo a = new NodoCatalogo("A", "a", NivelCatalogo.CAPITULO);
        NodoCatalogo b = new NodoCatalogo("B", "b", NivelCatalogo.CAPITULO);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    public void testDosNodosConElMismoCodigoSonIguales(){
        NodoCatalogo a = new NodoCatalogo("I21", "Infarto", NivelCatalogo.CODIGO);
        NodoCatalogo b = new NodoCatalogo("I21", "Otro nombre", NivelCatalogo.GRUPO);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(0, a.compareTo(b));
    }

    public void testNodosConDistintoCodigoNoSonIguales(){
        NodoCatalogo a = new NodoCatalogo("I21", "Infarto", NivelCatalogo.CODIGO);
        NodoCatalogo b = new NodoCatalogo("I22", "Infarto", NivelCatalogo.CODIGO);
        assertFalse(a.equals(b));
    }

    public void testPorCodigoEncuentraElNodoConEseCodigo(){
        NodoCatalogo nodo = new NodoCatalogo("I21", "Infarto", NivelCatalogo.CODIGO);
        assertEquals(0, NodoCatalogo.porCodigo("I21").compareTo(nodo));
    }

    public void testPorCodigoConCodigoNuloLanzaExcepcion(){
        try {
            NodoCatalogo.porCodigo(null);
            fail("Debia lanzar excepcion");
        } catch (IllegalArgumentException e) {

        }
    }
}
