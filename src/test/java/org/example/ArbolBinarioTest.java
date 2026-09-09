package org.example;

import junit.framework.TestCase;

import ucu.edu.aed.implementaciones.ArbolBinario;

public class ArbolBinarioTest extends TestCase {

    private ArbolBinario<Integer> arbol;

    protected void setUp(){
        arbol = new ArbolBinario<>();
        int[] claves = {1, 2, 3, 4, 5, 6, 7};
        for (int clave : claves) {
            arbol.insertar(clave);
        }
    }

    public void testEsVacioEnArbolNuevo(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertTrue(vacio.esVacio());
    }

    public void testEsVacioFalseConDatos(){
        assertFalse(arbol.esVacio());
    }

    public void testObtenerRaizEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertNull(vacio.obtenerRaiz());
    }

    public void testInsertarEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertTrue(vacio.insertar(10));
        assertEquals(Integer.valueOf(10), vacio.obtenerRaiz().getDato());
        assertEquals(1, vacio.cantidadNodos());
    }

    public void testInsertarRepartaEntreRamas(){
        assertEquals(7, arbol.cantidadNodos());
        assertEquals("1,2,5,7,3,4,6", arbol.preOrderString());
    }

    public void testCantidadNodos(){
        assertEquals(7, arbol.cantidadNodos());
    }

    public void testCantidadNodosEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertEquals(0, vacio.cantidadNodos());
    }

    public void testCantidadHojas(){
        assertEquals(4, arbol.cantidadHojas());
    }

    public void testCantidadNodosInternos(){
        assertEquals(3, arbol.cantidadNodosInternos());
    }

    public void testAltura(){
        assertEquals(3, arbol.altura());
    }

    public void testAlturaEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertEquals(0, vacio.altura());
    }

    public void testPreOrderString(){
        assertEquals("1,2,5,7,3,4,6", arbol.preOrderString());
    }

    public void testInOrderString(){
        assertEquals("5,2,7,1,4,3,6", arbol.inOrderString());
    }

    public void testPostOrderString(){
        assertEquals("5,7,2,4,6,3,1", arbol.postOrderString());
    }

    public void testPreOrderStringEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertEquals("", vacio.preOrderString());
    }

    public void testPorNivelesString(){
        assertEquals("1,2,3,5,7,4,6", arbol.porNivelesString());
    }

    public void testPorNivelesStringEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertEquals("", vacio.porNivelesString());
    }

    public void testPorNivelesConUnSoloNodo(){
        ArbolBinario<Integer> unSoloNodo = new ArbolBinario<>();
        unSoloNodo.insertar(10);
        assertEquals("10", unSoloNodo.porNivelesString());
    }

    public void testPorNivelesEsLaConcatenacionDeLosNiveles(){
        StringBuilder porNiveles = new StringBuilder();
        for (int nivel = 0; nivel < arbol.altura(); nivel++){
            for (int i = 0; i < arbol.enNivel(nivel).tamaño(); i++){
                if (porNiveles.length() > 0){
                    porNiveles.append(",");
                }
                porNiveles.append(arbol.enNivel(nivel).obtener(i));
            }
        }
        assertEquals(arbol.porNivelesString(), porNiveles.toString());
    }

    public void testCompletosEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertEquals(0, vacio.completos().tamaño());
    }

    public void testEnNivelEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertEquals(0, vacio.enNivel(0).tamaño());
    }

    public void testBuscarElementoExistente(){
        assertEquals(Integer.valueOf(6), arbol.buscar(6));
        assertEquals(Integer.valueOf(1), arbol.buscar(1));
    }

    public void testBuscarElementoInexistente(){
        assertNull(arbol.buscar(99));
    }

    public void testBuscarEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertNull(vacio.buscar(5));
    }

    public void testEliminarHoja(){
        assertTrue(arbol.eliminar(4));
        assertEquals(6, arbol.cantidadNodos());
        assertNull(arbol.buscar(4));
    }

    public void testEliminarRaizConDosHijos(){
        assertTrue(arbol.eliminar(1));
        assertEquals(6, arbol.cantidadNodos());
        assertEquals("7,2,5,3,4,6", arbol.preOrderString());
    }

    public void testEliminarClaveInexistente(){
        assertFalse(arbol.eliminar(99));
        assertEquals(7, arbol.cantidadNodos());
    }

    public void testEliminarEnArbolVacio(){
        ArbolBinario<Integer> vacio = new ArbolBinario<>();
        assertFalse(vacio.eliminar(5));
    }

    public void testObtenerNivelNoImplementado(){
        assertEquals(-1, arbol.obtenerNivel(5));
    }

    public void testClaveMenorNoImplementado(){
        assertNull(arbol.claveMenor());
    }

    public void testGetContadorNoImplementado(){
        assertEquals(0, arbol.getContador());
    }
}
