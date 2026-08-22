package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ucu.edu.aed.implementaciones.ListaCircularDoble;
import ucu.edu.aed.tda.TDALista;

public class ListaCircularDobleTest {

    @Test
    public void listaRecienCreadaEsVacia(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    @Test
    public void agregarUnElemento(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();

        lista.agregar(10);

        assertFalse(lista.esVacio());
        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(10), lista.obtener(0));
    }

    @Test
    public void agregarVariosElementosAlFinal(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();

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
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
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
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);
        lista.agregar(30);

        lista.agregar(1, 20);

        assertEquals(Integer.valueOf(20), lista.obtener(1));
        assertEquals(3, lista.tamaño());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void agregarConIndiceFueraDeRango(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);

        lista.agregar(5, 20);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void obtenerIndiceFueraDeRango(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);

        lista.obtener(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void obtenerEnListaVacia(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();

        lista.obtener(0);
    }

    @Test
    public void obtenerRecorreDesdeElExtremoMasCercano(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        for (int i = 0; i < 10; i++){
            lista.agregar(i);
        }

        assertEquals(Integer.valueOf(0), lista.obtener(0));
        assertEquals(Integer.valueOf(9), lista.obtener(9));
        assertEquals(Integer.valueOf(7), lista.obtener(7));
    }

    @Test
    public void laColaApuntaOtraVezALaCabezaYViceversa(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        lista.agregar(0, 5);

        assertEquals(4, lista.tamaño());
        assertEquals(Integer.valueOf(5), lista.obtener(0));
        assertEquals(Integer.valueOf(30), lista.obtener(3));
    }

    @Test
    public void removerIndiceElementoExistente(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
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
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);

        Integer removido = lista.remover(0);

        assertEquals(Integer.valueOf(10), removido);
        assertTrue(lista.esVacio());
    }

    @Test
    public void removerPrimeroYUltimoActualizaCabezaYCola(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        lista.remover(0);
        lista.remover(lista.tamaño() - 1);

        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(20), lista.obtener(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removerIndiceFueraDeRango(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);

        lista.remover(5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removerIndiceListaVacia(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();

        lista.remover(0);
    }

    @Test
    public void removerElementoExistente(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
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
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);

        boolean resultado = lista.remover(Integer.valueOf(99));

        assertFalse(resultado);
        assertEquals(1, lista.tamaño());
    }

    @Test
    public void removerElementoListaVacia(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();

        boolean resultado = lista.remover(Integer.valueOf(10));

        assertFalse(resultado);
        assertTrue(lista.esVacio());
    }

    @Test
    public void contieneElementoExistenteEInexistente(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);
        lista.agregar(20);

        assertTrue(lista.contiene(10));
        assertFalse(lista.contiene(99));
    }

    @Test
    public void indiceDeElementoExistenteEInexistente(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(1, lista.indiceDe(20));
        assertEquals(-1, lista.indiceDe(99));
    }

    @Test
    public void buscarPorCriterio(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);
        lista.agregar(15);
        lista.agregar(20);

        Integer encontrado = lista.buscar(n -> n % 2 != 0);

        assertEquals(Integer.valueOf(15), encontrado);
    }

    @Test
    public void buscarSinCoincidenciasDevuelveNull(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);

        assertNull(lista.buscar(n -> n > 100));
    }

    @Test
    public void ordenarDevuelveNuevaListaOrdenadaSinModificarLaOriginal(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
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
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);
        lista.agregar(20);

        lista.vaciar();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    @Test
    public void agregarLuegoDeVaciarVuelveAFuncionar(){
        ListaCircularDoble<Integer> lista = new ListaCircularDoble<>();
        lista.agregar(10);
        lista.vaciar();

        lista.agregar(99);

        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(99), lista.obtener(0));
    }
}
