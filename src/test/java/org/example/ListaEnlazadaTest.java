package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import ucu.edu.aed.implementaciones.ListaEnlazada;
import ucu.edu.aed.tda.TDALista;

public class ListaEnlazadaTest {

    @Test
    public void listaRecienCreadaEsVacia(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    @Test
    public void agregarUnElemento(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

        lista.agregar(10);

        assertFalse(lista.esVacio());
        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(10), lista.obtener(0));
    }

    @Test
    public void agregarVariosElementosAlFinal(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

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
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
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
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(30);

        lista.agregar(1, 20);

        assertEquals(Integer.valueOf(20), lista.obtener(1));
        assertEquals(3, lista.tamaño());
    }

    @Test
    public void agregarConIndiceAlFinalEquivaleAAgregar(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);

        lista.agregar(1, 20);

        assertEquals(Integer.valueOf(20), lista.obtener(1));
        assertEquals(2, lista.tamaño());
    }

    @Test
    public void agregarConIndiceEnListaVacia(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

        lista.agregar(0, 10);

        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(10), lista.obtener(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void agregarConIndiceFueraDeRango(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);

        lista.agregar(5, 20);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void obtenerIndiceFueraDeRango(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);

        lista.obtener(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void obtenerEnListaVacia(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

        lista.obtener(0);
    }

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

    @Test
    public void removerIndiceUnicoElemento(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);

        Integer removido = lista.remover(0);

        assertEquals(Integer.valueOf(10), removido);
        assertTrue(lista.esVacio());
    }

    @Test
    public void removerIndiceUltimoElementoActualizaCola(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);

        lista.remover(1);
        lista.agregar(30);

        assertEquals(Integer.valueOf(30), lista.obtener(1));
        assertEquals(2, lista.tamaño());
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
    public void removerElementoQueEsLaCola(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);

        boolean resultado = lista.remover(Integer.valueOf(20));
        lista.agregar(30);

        assertTrue(resultado);
        assertEquals(Integer.valueOf(30), lista.obtener(1));
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

    @Test
    public void contieneElementoExistenteEInexistente(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);

        assertTrue(lista.contiene(10));
        assertFalse(lista.contiene(99));
    }

    @Test
    public void contieneEnListaVaciaEsFalse(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

        assertFalse(lista.contiene(10));
    }

    @Test
    public void indiceDeElementoExistenteEInexistente(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(1, lista.indiceDe(20));
        assertEquals(-1, lista.indiceDe(99));
    }

    @Test
    public void buscarPorCriterio(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(15);
        lista.agregar(20);

        Integer encontrado = lista.buscar(n -> n % 2 != 0);

        assertEquals(Integer.valueOf(15), encontrado);
    }

    @Test
    public void buscarSinCoincidenciasDevuelveNull(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);

        Integer encontrado = lista.buscar(n -> n > 100);

        assertNull(encontrado);
    }

    @Test
    public void ordenarDevuelveNuevaListaOrdenadaSinModificarLaOriginal(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
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
    public void ordenarListaVaciaDevuelveListaVacia(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();

        TDALista<Integer> ordenada = lista.ordenar((a, b) -> a - b);

        assertTrue(ordenada.esVacio());
    }

    @Test
    public void ordenarConElementosRepetidosMantieneLaCantidad(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(20);
        lista.agregar(10);
        lista.agregar(20);

        TDALista<Integer> ordenada = lista.ordenar((a, b) -> a - b);

        assertEquals(3, ordenada.tamaño());
        assertEquals(Integer.valueOf(10), ordenada.obtener(0));
        assertEquals(Integer.valueOf(20), ordenada.obtener(1));
        assertEquals(Integer.valueOf(20), ordenada.obtener(2));
    }

    @Test
    public void vaciarDejaLaListaSinElementos(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.agregar(20);

        lista.vaciar();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    @Test
    public void agregarLuegoDeVaciarVuelveAFuncionar(){
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        lista.agregar(10);
        lista.vaciar();

        lista.agregar(99);

        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(99), lista.obtener(0));
    }
}
