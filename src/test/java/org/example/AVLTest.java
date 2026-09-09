package org.example;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import ucu.edu.aed.implementaciones.AVLImpl;
import ucu.edu.aed.implementaciones.ArbolBinarioBusqueda;

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

    // ---------- estructura vacía ----------

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

    // ---------- un único nodo ----------

    public void testInsertarUnUnicoNodo(){
        assertTrue(avl.insertar(10));
        assertEquals(1, avl.cantidadNodos());
        assertEquals(1, avl.altura());
        assertEquals(Integer.valueOf(10), avl.obtenerRaiz().getDato());
    }

    // ---------- balanceado vs degenerado ----------

    public void testInsercionAscendenteNoDegenera(){
        for (int i = 1; i <= 15; i++){
            avl.insertar(i);
        }
        assertEquals(15, avl.cantidadNodos());
        // log2(15) ~ 3.9: un AVL balanceado da altura 4, un BST comun degeneraria a 15
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

    /**
     * El caso que justifica todo el desafío: con los mismos datos insertados en el
     * mismo orden, un ArbolBinarioBusqueda comun degenera a una cadena, mientras que
     * el AVL se mantiene balanceado.
     */
    public void testDiferenciaEntreArbolBalanceadoYDegenerado(){
        ArbolBinarioBusqueda<Integer> sinBalancear = new ArbolBinarioBusqueda<>();
        AVLImpl<Integer> balanceado = new AVLImpl<>();
        for (int i = 1; i <= 15; i++){
            sinBalancear.insertar(i);
            balanceado.insertar(i);
        }
        assertEquals(15, sinBalancear.cantidadNodos());
        assertEquals(15, balanceado.cantidadNodos());
        // insertar en orden ascendente degenera el BST comun a una cadena de altura 15
        assertEquals(15, sinBalancear.altura());
        // el AVL con los mismos datos, en el mismo orden, se mantiene balanceado
        assertEquals(4, balanceado.altura());
        assertTrue(balanceado.altura() < sinBalancear.altura());
    }

    // ---------- rotaciones ----------

    public void testRotacionSimpleDerechaLL(){
        avl.insertar(30);
        avl.insertar(20);
        avl.insertar(10); // fuerza LL
        assertEquals(2, avl.altura());
        assertEquals(Integer.valueOf(20), avl.obtenerRaiz().getDato());
        assertEquals("10,20,30", avl.inOrderString());
    }

    public void testRotacionSimpleIzquierdaRR(){
        avl.insertar(10);
        avl.insertar(20);
        avl.insertar(30); // fuerza RR
        assertEquals(2, avl.altura());
        assertEquals(Integer.valueOf(20), avl.obtenerRaiz().getDato());
        assertEquals("10,20,30", avl.inOrderString());
    }

    public void testRotacionDobleIzquierdaDerechaLR(){
        avl.insertar(30);
        avl.insertar(10);
        avl.insertar(20); // fuerza LR
        assertEquals(2, avl.altura());
        assertEquals(Integer.valueOf(20), avl.obtenerRaiz().getDato());
        assertEquals("10,20,30", avl.inOrderString());
    }

    public void testRotacionDobleDerechaIzquierdaRL(){
        avl.insertar(10);
        avl.insertar(30);
        avl.insertar(20); // fuerza RL
        assertEquals(2, avl.altura());
        assertEquals(Integer.valueOf(20), avl.obtenerRaiz().getDato());
        assertEquals("10,20,30", avl.inOrderString());
    }

    // ---------- duplicados ----------

    public void testInsertarDuplicadoNoAgrega(){
        avl.insertar(10);
        assertFalse(avl.insertar(10));
        assertEquals(1, avl.cantidadNodos());
    }

    public void testInsertarNullNoAgrega(){
        assertFalse(avl.insertar(null));
        assertTrue(avl.esVacio());
    }

    // ---------- búsquedas ----------

    public void testBuscarElementoExistenteEInexistente(){
        int[] claves = {50, 30, 70, 20, 40, 60, 80};
        for (int c : claves) avl.insertar(c);
        assertEquals(Integer.valueOf(40), avl.buscar(40));
        assertNull(avl.buscar(99));
    }

    // ---------- eliminación: todos los casos de borrado en un árbol de búsqueda ----------

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
        assertTrue(avl.eliminar(30)); // 30 solo tiene el hijo izquierdo (20)
        assertEquals(3, avl.cantidadNodos());
        assertNull(avl.buscar(30));
        assertNotNull(avl.buscar(20));
        assertOrdenado(avl);
    }

    public void testEliminarNodoConDosHijos(){
        int[] claves = {50, 30, 70, 20, 40, 60, 80};
        for (int c : claves) avl.insertar(c);
        assertTrue(avl.eliminar(30)); // dos hijos: 20 y 40
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

    // ---------- recorridos ----------

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

    // ---------- casos borde: inserción/eliminación aleatoria a mayor escala ----------

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
        double alturaMaximaEsperada = Math.log(presentes.size() + 1) / Math.log(2) * 2 + 2;
        assertTrue("altura demasiado alta para un AVL: " + avl.altura(), avl.altura() <= alturaMaximaEsperada);

        for (Integer valor : presentes){
            assertNotNull("deberia encontrarse " + valor, avl.buscar(valor));
        }
    }
}
