package org.example;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import ucu.edu.aed.implementaciones.AVLImpl;
import ucu.edu.aed.implementaciones.ArbolBinarioBusqueda;
import ucu.edu.aed.tda.TDAElemento;

public class AVLTest extends TestCase {

    private AVLImpl<Integer> avl;

    protected void setUp(){
        avl = new AVLImpl<>();
    }

    private void assertOrdenado(AVLImpl<Integer> unAvl){
        List<Integer> inorden = new ArrayList<>();
        unAvl.inOrder(inorden::add);
        for (int i = 1; i < inorden.size(); i++){
            assertTrue("debe seguir ordenado: " + inorden, inorden.get(i - 1) < inorden.get(i));
        }
    }

    // Recorre el arbol de verdad para calcular la altura, sin confiar en el
    // valor que el AVL tiene cacheado en cada nodo.
    private int alturaReal(TDAElemento<Integer> nodo){
        if (nodo == null){
            return 0;
        }
        return 1 + Math.max(alturaReal(nodo.getHijoIzquierdo()), alturaReal(nodo.getHijoDerecho()));
    }

    // Verifica, en cada nodo, que la altura cacheada coincida con la real y
    // que el balance (usando la altura real) este entre -1 y 1.
    private void assertBalanceado(TDAElemento<Integer> nodo){
        if (nodo == null){
            return;
        }
        int real = alturaReal(nodo);
        assertEquals("altura cacheada desactualizada en el nodo " + nodo.getDato(),
                real, nodo.altura());
        int balance = alturaReal(nodo.getHijoIzquierdo()) - alturaReal(nodo.getHijoDerecho());
        assertTrue("nodo " + nodo.getDato() + " desbalanceado, balance real=" + balance,
                balance >= -1 && balance <= 1);
        assertBalanceado(nodo.getHijoIzquierdo());
        assertBalanceado(nodo.getHijoDerecho());
    }

    public void testEsVacioEnArbolNuevo(){
        assertTrue(avl.esVacio());
        assertEquals(0, avl.cantidadNodos());
        assertEquals(0, avl.altura());
    }

    public void testBuscarEnArbolVacio(){
        assertNull(avl.buscar(5));
    }

    public void testEliminarEnArbolVacio(){
        assertFalse(avl.eliminar(5));
    }

    public void testInsertarUnUnicoNodo(){
        assertTrue(avl.insertar(10));
        assertEquals(1, avl.cantidadNodos());
        assertEquals(1, avl.altura());
        assertEquals(Integer.valueOf(10), avl.obtenerRaiz().getDato());
    }

    public void testInsercionAscendenteNoDegenera(){
        for (int i = 1; i <= 15; i++){
            avl.insertar(i);
        }
        assertEquals(15, avl.cantidadNodos());

        assertEquals(4, avl.altura());
        assertOrdenado(avl);
    }

    public void testInsercionDescendenteNoDegenera(){
        for (int i = 15; i >= 1; i--){
            avl.insertar(i);
        }
        assertEquals(15, avl.cantidadNodos());
        assertEquals(4, avl.altura());
        assertOrdenado(avl);
    }

    public void testDiferenciaEntreArbolBalanceadoYDegenerado(){
        ArbolBinarioBusqueda<Integer> sinBalancear = new ArbolBinarioBusqueda<>();
        AVLImpl<Integer> balanceado = new AVLImpl<>();
        for (int i = 1; i <= 15; i++){
            sinBalancear.insertar(i);
            balanceado.insertar(i);
        }
        assertEquals(15, sinBalancear.cantidadNodos());
        assertEquals(15, balanceado.cantidadNodos());

        assertEquals(15, sinBalancear.altura());

        assertEquals(4, balanceado.altura());
        assertTrue(balanceado.altura() < sinBalancear.altura());
    }

    public void testRotacionSimpleDerechaLL(){
        avl.insertar(30);
        avl.insertar(20);
        avl.insertar(10);
        assertEquals(2, avl.altura());
        assertEquals(Integer.valueOf(20), avl.obtenerRaiz().getDato());
        assertEquals("10,20,30", avl.inOrderString());
    }

    public void testRotacionSimpleIzquierdaRR(){
        avl.insertar(10);
        avl.insertar(20);
        avl.insertar(30);
        assertEquals(2, avl.altura());
        assertEquals(Integer.valueOf(20), avl.obtenerRaiz().getDato());
        assertEquals("10,20,30", avl.inOrderString());
    }

    public void testRotacionDobleIzquierdaDerechaLR(){
        avl.insertar(30);
        avl.insertar(10);
        avl.insertar(20);
        assertEquals(2, avl.altura());
        assertEquals(Integer.valueOf(20), avl.obtenerRaiz().getDato());
        assertEquals("10,20,30", avl.inOrderString());
    }

    public void testRotacionDobleDerechaIzquierdaRL(){
        avl.insertar(10);
        avl.insertar(30);
        avl.insertar(20);
        assertEquals(2, avl.altura());
        assertEquals(Integer.valueOf(20), avl.obtenerRaiz().getDato());
        assertEquals("10,20,30", avl.inOrderString());
    }

    public void testInsertarDuplicadoNoAgrega(){
        avl.insertar(10);
        assertFalse(avl.insertar(10));
        assertEquals(1, avl.cantidadNodos());
    }

    public void testInsertarNullNoAgrega(){
        assertFalse(avl.insertar(null));
        assertTrue(avl.esVacio());
    }

    public void testBuscarElementoExistenteEInexistente(){
        int[] claves = {50, 30, 70, 20, 40, 60, 80};
        for (int c : claves) avl.insertar(c);
        assertEquals(Integer.valueOf(40), avl.buscar(40));
        assertNull(avl.buscar(99));
    }

    public void testEliminarHoja(){
        int[] claves = {50, 30, 70, 20, 40};
        for (int c : claves) avl.insertar(c);
        assertTrue(avl.eliminar(20));
        assertEquals(4, avl.cantidadNodos());
        assertNull(avl.buscar(20));
        assertOrdenado(avl);
    }

    public void testEliminarNodoConUnHijo(){
        int[] claves = {50, 30, 70, 20};
        for (int c : claves) avl.insertar(c);
        assertTrue(avl.eliminar(30));
        assertEquals(3, avl.cantidadNodos());
        assertNull(avl.buscar(30));
        assertNotNull(avl.buscar(20));
        assertOrdenado(avl);
    }

    public void testEliminarNodoConDosHijos(){
        int[] claves = {50, 30, 70, 20, 40, 60, 80};
        for (int c : claves) avl.insertar(c);
        assertTrue(avl.eliminar(30));
        assertEquals(6, avl.cantidadNodos());
        assertNull(avl.buscar(30));
        assertEquals("20,40,50,60,70,80", avl.inOrderString());
        assertOrdenado(avl);
    }

    public void testEliminarRaiz(){
        int[] claves = {50, 30, 70};
        for (int c : claves) avl.insertar(c);
        assertTrue(avl.eliminar(50));
        assertEquals(2, avl.cantidadNodos());
        assertNull(avl.buscar(50));
        assertOrdenado(avl);
    }

    public void testEliminarClaveInexistente(){
        avl.insertar(10);
        assertFalse(avl.eliminar(99));
        assertEquals(1, avl.cantidadNodos());
    }

    public void testEliminarProvocaRebalanceoYMantieneOrden(){
        int[] claves = {50, 25, 75, 10, 30, 60, 90, 5};
        for (int c : claves) avl.insertar(c);

        avl.eliminar(90);
        avl.eliminar(75);
        avl.eliminar(60);

        assertEquals(5, avl.cantidadNodos());
        assertOrdenado(avl);
        assertTrue("altura demasiado alta para 5 nodos balanceados: " + avl.altura(), avl.altura() <= 3);
    }

    public void testEliminarTodoElArbolQuedaVacio(){
        int[] claves = {50, 30, 70, 20, 40, 60, 80};
        for (int c : claves) {
            avl.insertar(c);
        }
        for (int c : claves) {
            assertTrue(avl.eliminar(c));
        }
        assertTrue(avl.esVacio());
        assertEquals(0, avl.cantidadNodos());
        assertEquals(0, avl.altura());
    }

    public void testRecorridos(){
        int[] claves = {50, 30, 70, 20, 40, 60, 80};
        for (int c : claves) avl.insertar(c);
        assertEquals("20,30,40,50,60,70,80", avl.inOrderString());
        assertEquals(Integer.valueOf(50), avl.obtenerRaiz().getDato());
    }

    public void testPorNivelesEnArbolVacio(){
        assertEquals("", avl.porNivelesString());
    }

    public void testPorNivelesArbolBalanceado(){
        int[] claves = {50, 30, 70, 20, 40, 60, 80};
        for (int c : claves) avl.insertar(c);
        assertEquals("50,30,70,20,40,60,80", avl.porNivelesString());
    }

    public void testInsercionYEliminacionAleatoriaMantieneInvariantes(){
        java.util.Random random = new java.util.Random(42);
        List<Integer> presentes = new ArrayList<>();

        for (int i = 0; i < 300; i++){
            int valor = random.nextInt(200);
            if (avl.buscar(valor) == null){
                avl.insertar(valor);
                presentes.add(valor);
            } else if (random.nextBoolean()){
                avl.eliminar(valor);
                presentes.remove(Integer.valueOf(valor));
            }
        }

        assertEquals(presentes.size(), avl.cantidadNodos());
        assertOrdenado(avl);
        assertBalanceado(avl.obtenerRaiz());
        double alturaMaximaEsperada = Math.log(presentes.size() + 1) / Math.log(2) * 2 + 2;
        assertTrue("altura demasiado alta para un AVL: " + avl.altura(), avl.altura() <= alturaMaximaEsperada);

        for (Integer valor : presentes){
            assertNotNull("deberia encontrarse " + valor, avl.buscar(valor));
        }
    }

    // El predecesor de un nodo con dos hijos (el mas a la derecha de su
    // subarbol izquierdo) puede estar a varios niveles de profundidad, no
    // solo como hijo directo. Si al desengancharlo no se actualiza la
    // altura en todo ese camino (no solo en el padre inmediato del
    // predecesor), el arbol queda desbalanceado sin que nada lo detecte.
    // Insertar todas las claves y despues eliminarlas todas, en varios
    // ordenes, genera ese escenario con una probabilidad muy alta.
    public void testEliminarTodoEnVariosOrdenesQuedaSiempreBalanceado(){
        java.util.Random random = new java.util.Random(7);

        for (int intento = 0; intento < 30; intento++){
            List<Integer> claves = new ArrayList<>();
            for (int i = 0; i < 80; i++){
                claves.add(i);
            }
            java.util.Collections.shuffle(claves, random);

            AVLImpl<Integer> unAvl = new AVLImpl<>();
            for (int c : claves){
                unAvl.insertar(c);
            }

            List<Integer> ordenEliminar = new ArrayList<>(claves);
            java.util.Collections.shuffle(ordenEliminar, random);

            for (int c : ordenEliminar){
                assertTrue("deberia poder eliminar " + c, unAvl.eliminar(c));
                assertBalanceado(unAvl.obtenerRaiz());
            }

            assertEquals(0, unAvl.cantidadNodos());
            assertTrue(unAvl.esVacio());
        }
    }
}
