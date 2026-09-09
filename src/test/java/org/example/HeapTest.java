package org.example;

import java.util.Comparator;

import junit.framework.TestCase;
import ucu.edu.aed.implementaciones.Heap;
import ucu.edu.aed.tda.TDAHeap;

public class HeapTest extends TestCase {

    private Heap<Integer> heap;

    protected void setUp(){
        heap = new Heap<Integer>(Comparator.naturalOrder());
    }

    private <E> String extraerTodo(TDAHeap<E> unHeap){
        StringBuilder resultado = new StringBuilder();
        while (!unHeap.esVacio()){
            if (resultado.length() > 0){
                resultado.append(",");
            }
            resultado.append(unHeap.eliminar());
        }
        return resultado.toString();
    }

    public void testHeapNuevoEsVacio(){
        assertTrue(heap.esVacio());
        assertEquals(0, heap.cantidad());
        assertNull(heap.minimo());
        assertEquals("", heap.toString());
    }

    public void testOperacionesSobreHeapVacio(){
        assertNull(heap.eliminar());
        assertNull(heap.eliminar());
        assertTrue(heap.esVacio());
        assertEquals(0, heap.cantidad());
    }

    public void testUnUnicoElemento(){
        assertTrue(heap.insertar(7));
        assertFalse(heap.esVacio());
        assertEquals(1, heap.cantidad());
        assertEquals(Integer.valueOf(7), heap.minimo());
    }

    public void testEliminarElUnicoElementoDejaElHeapVacio(){
        heap.insertar(7);
        assertEquals(Integer.valueOf(7), heap.eliminar());
        assertTrue(heap.esVacio());
        assertEquals(0, heap.cantidad());
        assertNull(heap.minimo());
        assertNull(heap.eliminar());
    }

    public void testElOrdenDeLlegadaNoDeterminaElDeSalida(){
        heap.insertar(5);
        heap.insertar(3);
        heap.insertar(1);
        assertEquals(Integer.valueOf(1), heap.minimo());
        assertEquals("1,3,5", extraerTodo(heap));
    }

    public void testElHeapNoQuedaOrdenadoInternamente(){
        heap.insertar(5);
        heap.insertar(3);
        heap.insertar(1);
        assertEquals("1,5,3", heap.toString());
        assertEquals(Integer.valueOf(1), heap.minimo());
    }

    public void testDistintosOrdenesDeInsercionDanLaMismaSalida(){
        Heap<Integer> ascendente = new Heap<Integer>(Comparator.naturalOrder());
        Heap<Integer> descendente = new Heap<Integer>(Comparator.naturalOrder());
        Heap<Integer> desordenado = new Heap<Integer>(Comparator.naturalOrder());

        for (int i = 1; i <= 7; i++){
            ascendente.insertar(i);
        }
        for (int i = 7; i >= 1; i--){
            descendente.insertar(i);
        }
        int[] mezclado = {4, 1, 7, 3, 6, 2, 5};
        for (int valor : mezclado){
            desordenado.insertar(valor);
        }

        assertEquals("1,2,3,4,5,6,7", extraerTodo(ascendente));
        assertEquals("1,2,3,4,5,6,7", extraerTodo(descendente));
        assertEquals("1,2,3,4,5,6,7", extraerTodo(desordenado));
    }

    public void testInsertarNullNoHaceNada(){
        assertFalse(heap.insertar(null));
        assertTrue(heap.esVacio());
        assertEquals(0, heap.cantidad());
    }

    public void testInsertarUnNuevoMinimoLoLlevaALaRaiz(){
        heap.insertar(10);
        heap.insertar(20);
        heap.insertar(30);
        assertEquals(Integer.valueOf(10), heap.minimo());
        heap.insertar(1);
        assertEquals(Integer.valueOf(1), heap.minimo());
        assertEquals(4, heap.cantidad());
    }

    public void testInsertarUnMaximoNoAlteraLaRaiz(){
        heap.insertar(10);
        heap.insertar(20);
        heap.insertar(99);
        assertEquals(Integer.valueOf(10), heap.minimo());
    }

    public void testAlHundirSeEligeElMenorDeLosDosHijos(){
        heap.insertar(1);
        heap.insertar(2);
        heap.insertar(6);
        heap.insertar(9);

        assertEquals(Integer.valueOf(1), heap.eliminar());
        assertEquals(Integer.valueOf(2), heap.minimo());
        assertEquals("2,9,6", heap.toString());
        assertEquals("2,6,9", extraerTodo(heap));
    }

    public void testHundirRecorreVariosNiveles(){
        for (int i = 1; i <= 7; i++){
            heap.insertar(i);
        }
        assertEquals("1,2,3,4,5,6,7", heap.toString());

        assertEquals(Integer.valueOf(1), heap.eliminar());
        assertEquals("2,4,3,7,5,6", heap.toString());
        assertEquals("2,3,4,5,6,7", extraerTodo(heap));
    }

    public void testEliminarDejaElSiguienteMinimoEnLaRaiz(){
        int[] valores = {8, 3, 5, 1, 9, 2};
        for (int valor : valores){
            heap.insertar(valor);
        }
        assertEquals(Integer.valueOf(1), heap.eliminar());
        assertEquals(Integer.valueOf(2), heap.minimo());
        assertEquals(Integer.valueOf(2), heap.eliminar());
        assertEquals(Integer.valueOf(3), heap.minimo());
        assertEquals(4, heap.cantidad());
    }

    public void testRemoverElementoDelMedio(){
        int[] valores = {50, 30, 70, 20, 40, 60, 80, 10, 90};
        for (int v : valores) heap.insertar(v);

        assertTrue(heap.remover(40));
        assertEquals(8, heap.cantidad());
        assertFalse(extraerTodo(heap).contains("40"));
    }

    public void testRemoverLaRaiz(){
        int[] valores = {10, 20, 30, 40, 50};
        for (int v : valores) heap.insertar(v);

        assertTrue(heap.remover(10));
        assertEquals(4, heap.cantidad());
        assertEquals(Integer.valueOf(20), heap.minimo());
    }

    public void testRemoverElUltimoElementoInsertado(){
        heap.insertar(5);
        heap.insertar(3);
        heap.insertar(8);
        assertTrue(heap.remover(8));
        assertEquals(2, heap.cantidad());
    }

    public void testRemoverElUnicoElemento(){
        heap.insertar(42);
        assertTrue(heap.remover(42));
        assertTrue(heap.esVacio());
    }

    public void testRemoverInexistenteNoAlteraElHeap(){
        heap.insertar(5);
        heap.insertar(3);
        assertFalse(heap.remover(99));
        assertEquals(2, heap.cantidad());
    }

    public void testRemoverEnHeapVacio(){
        assertFalse(heap.remover(5));
    }

    public void testRemoverMantieneElInvarianteDeHeap(){
        int[] valores = {8, 3, 5, 1, 9, 2, 7, 6, 4};
        for (int v : valores) heap.insertar(v);

        assertTrue(heap.remover(3));
        assertTrue(heap.remover(7));

        assertEquals("1,2,4,5,6,8,9", extraerTodo(heap));
    }

    public void testElementosRepetidos(){
        heap.insertar(3);
        heap.insertar(1);
        heap.insertar(3);
        heap.insertar(1);
        heap.insertar(2);
        assertEquals(5, heap.cantidad());
        assertEquals("1,1,2,3,3", extraerTodo(heap));
    }

    public void testInsertarYEliminarIntercalados(){
        heap.insertar(5);
        heap.insertar(3);
        assertEquals(Integer.valueOf(3), heap.eliminar());

        heap.insertar(8);
        heap.insertar(1);
        assertEquals(Integer.valueOf(1), heap.eliminar());
        assertEquals(Integer.valueOf(5), heap.eliminar());

        heap.insertar(2);
        assertEquals(Integer.valueOf(2), heap.eliminar());
        assertEquals(Integer.valueOf(8), heap.eliminar());
        assertTrue(heap.esVacio());
    }

    public void testCreceMasAlladeLaCapacidadInicial(){

        for (int i = 100; i >= 1; i--){
            heap.insertar(i);
        }
        assertEquals(100, heap.cantidad());
        assertEquals(Integer.valueOf(1), heap.minimo());

        StringBuilder esperado = new StringBuilder();
        for (int i = 1; i <= 100; i++){
            if (i > 1){
                esperado.append(",");
            }
            esperado.append(i);
        }
        assertEquals(esperado.toString(), extraerTodo(heap));
    }

    public void testCapacidadInicialMinima(){
        Heap<Integer> chico = new Heap<Integer>(1, Comparator.naturalOrder());
        for (int i = 5; i >= 1; i--){
            chico.insertar(i);
        }
        assertEquals(5, chico.cantidad());
        assertEquals("1,2,3,4,5", extraerTodo(chico));
    }

    public void testCapacidadInicialInvalidaSeAjusta(){
        Heap<Integer> raro = new Heap<Integer>(0, Comparator.naturalOrder());
        assertTrue(raro.insertar(4));
        assertTrue(raro.insertar(2));
        assertEquals("2,4", extraerTodo(raro));
    }

    public void testLaSalidaSiempreEsNoDecreciente(){
        for (int i = 0; i < 50; i++){
            heap.insertar((i * 37) % 101);
        }
        assertEquals(50, heap.cantidad());

        int anterior = Integer.MIN_VALUE;
        int extraidos = 0;
        while (!heap.esVacio()){
            int actual = heap.eliminar();
            assertTrue("salió " + actual + " después de " + anterior, actual >= anterior);
            anterior = actual;
            extraidos++;
        }
        assertEquals(50, extraidos);
    }

    private static class Paciente implements Comparable<Paciente> {

        private final String nombre;
        private final int urgencia;
        private final int llegada;

        Paciente(String nombre, int urgencia, int llegada){
            this.nombre = nombre;
            this.urgencia = urgencia;
            this.llegada = llegada;
        }

        public int compareTo(Paciente otro){
            if (urgencia != otro.urgencia){
                return urgencia - otro.urgencia;
            }
            return llegada - otro.llegada;
        }

        public String toString(){
            return nombre;
        }
    }

    public void testColaDePrioridadDePacientes(){
        Heap<Paciente> guardia = new Heap<Paciente>(Comparator.naturalOrder());
        guardia.insertar(new Paciente("Ana", 5, 1));
        guardia.insertar(new Paciente("Bruno", 3, 2));
        guardia.insertar(new Paciente("Carla", 1, 3));

        assertEquals("Carla", guardia.minimo().toString());
        assertEquals("Carla,Bruno,Ana", extraerTodo(guardia));
    }

    public void testAIgualUrgenciaSaleElQueLlegoPrimero(){
        Heap<Paciente> guardia = new Heap<Paciente>(Comparator.naturalOrder());
        guardia.insertar(new Paciente("Tercero", 3, 3));
        guardia.insertar(new Paciente("Primero", 3, 1));
        guardia.insertar(new Paciente("Segundo", 3, 2));

        assertEquals("Primero,Segundo,Tercero", extraerTodo(guardia));
    }

    public void testUnCriticoSeAdelantaAUnaColaDeLeves(){
        Heap<Paciente> guardia = new Heap<Paciente>(Comparator.naturalOrder());
        for (int i = 1; i <= 20; i++){
            guardia.insertar(new Paciente("Leve" + i, 5, i));
        }
        guardia.insertar(new Paciente("Grave", 1, 21));

        assertEquals("Grave", guardia.eliminar().toString());
        assertEquals("Leve1", guardia.eliminar().toString());
        assertEquals(19, guardia.cantidad());
    }
}
