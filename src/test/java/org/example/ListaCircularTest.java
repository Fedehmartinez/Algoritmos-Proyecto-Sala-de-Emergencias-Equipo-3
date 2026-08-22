package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ucu.edu.aed.implementaciones.ListaCircular;
import ucu.edu.aed.tda.TDALista;

public class ListaCircularTest {

    @Test
    public void listaRecienCreadaEsVacia(){
        ListaCircular<Integer> lista = new ListaCircular<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    @Test
    public void agregarUnElemento(){
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);

        assertFalse(lista.esVacio());
        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(10), lista.obtener(0));
    }

    @Test
    public void agregarVariosElementosAlFinal(){
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(3, lista.tamaño());
        assertEquals(Integer.valueOf(10), lista.obtener(0));
        assertEquals(Integer.valueOf(20), lista.obtener(1));
        assertEquals(Integer.valueOf(30), lista.obtener(2));
    }

    @Test
    public void agregarConIndiceAlPrincipio(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(20);
        lista.agregar(30);

        lista.agregar(0, 10);

        assertEquals(3, lista.tamaño());
        assertEquals(Integer.valueOf(10), lista.obtener(0));
        assertEquals(Integer.valueOf(20), lista.obtener(1));
        assertEquals(Integer.valueOf(30), lista.obtener(2));
    }

    @Test
    public void agregarConIndiceEnElMedio(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.agregar(30);

        lista.agregar(1, 20);

        assertEquals(Integer.valueOf(20), lista.obtener(1));
        assertEquals(3, lista.tamaño());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void agregarConIndiceFueraDeRango(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);

        lista.agregar(5, 20);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void obtenerIndiceFueraDeRango(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);

        lista.obtener(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void obtenerEnListaVacia(){
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.obtener(0);
    }

    @Test
    public void laColaApuntaOtraVezALaCabeza(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        lista.agregar(40);

        assertEquals(Integer.valueOf(10), lista.obtener(0));
        assertEquals(4, lista.tamaño());
    }

    @Test
    public void removerIndiceElementoExistente(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        Integer removido = lista.remover(1);

        assertEquals(Integer.valueOf(20), removido);
        assertEquals(2, lista.tamaño());
        assertFalse(lista.contiene(20));
    }

    @Test
    public void removerUnicoElementoDejaLaListaVacia(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);

        Integer removido = lista.remover(0);

        assertEquals(Integer.valueOf(10), removido);
        assertTrue(lista.esVacio());
    }

    @Test
    public void removerYLuegoAgregarMantieneLaEstructuraCircular(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.agregar(20);

        lista.remover(0);
        lista.agregar(30);

        assertEquals(2, lista.tamaño());
        assertEquals(Integer.valueOf(20), lista.obtener(0));
        assertEquals(Integer.valueOf(30), lista.obtener(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removerIndiceFueraDeRango(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);

        lista.remover(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removerIndiceListaVacia(){
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.remover(0);
    }

    @Test
    public void removerElementoExistente(){
        ListaCircular<Integer> lista = new ListaCircular<>();
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
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);

        boolean resultado = lista.remover(Integer.valueOf(99));

        assertFalse(resultado);
        assertEquals(1, lista.tamaño());
    }

    @Test
    public void removerElementoListaVacia(){
        ListaCircular<Integer> lista = new ListaCircular<>();

        boolean resultado = lista.remover(Integer.valueOf(10));

        assertFalse(resultado);
        assertTrue(lista.esVacio());
    }

    @Test
    public void contieneElementoExistenteEInexistente(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.agregar(20);

        assertTrue(lista.contiene(10));
        assertFalse(lista.contiene(99));
    }

    @Test
    public void indiceDeElementoExistenteEInexistente(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(1, lista.indiceDe(20));
        assertEquals(-1, lista.indiceDe(99));
    }

    @Test
    public void buscarPorCriterio(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.agregar(15);
        lista.agregar(20);

        Integer encontrado = lista.buscar(n -> n % 2 != 0);

        assertEquals(Integer.valueOf(15), encontrado);
    }

    @Test
    public void buscarSinCoincidenciasDevuelveNull(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);

        assertNull(lista.buscar(n -> n > 100));
    }

    @Test
    public void ordenarDevuelveNuevaListaOrdenadaSinModificarLaOriginal(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(30);
        lista.agregar(10);
        lista.agregar(20);

        TDALista<Integer> ordenada = lista.ordenar((a, b) -> a - b);

        assertEquals(Integer.valueOf(10), ordenada.obtener(0));
        assertEquals(Integer.valueOf(20), ordenada.obtener(1));
        assertEquals(Integer.valueOf(30), ordenada.obtener(2));
        assertEquals(Integer.valueOf(30), lista.obtener(0));
    }

    @Test
    public void vaciarDejaLaListaSinElementos(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.agregar(20);

        lista.vaciar();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    @Test
    public void agregarLuegoDeVaciarVuelveAFuncionar(){
        ListaCircular<Integer> lista = new ListaCircular<>();
        lista.agregar(10);
        lista.vaciar();

        lista.agregar(99);

        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(99), lista.obtener(0));
    }
}
