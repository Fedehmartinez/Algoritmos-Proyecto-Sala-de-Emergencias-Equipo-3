package ucu.edu.aed.implementaciones;

import java.util.function.Consumer;

import ucu.edu.aed.tda.TDAArbolBinarioBusqueda;
import ucu.edu.aed.tda.TDAElemento;
import ucu.edu.aed.tda.TDALista;

public class ArbolBinarioBusqueda<T extends Comparable<T>> extends ArbolBinario<T> implements TDAArbolBinarioBusqueda<T> {

    private int contador;

    @Override
    public T buscar(Comparable<T> predicate){
        if (raiz == null){
            return null;
        }
        else {
            TDAElemento<T> resultado = raiz.buscar(predicate);
            if (resultado == null){
                return null;
            }
            else{
                return resultado.getDato();
            }
        }
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda){
        if (raiz == null){
            return false;
        }
        if (raiz.buscar(criterioBusqueda) == null){
            return false;
        }
        else {
            raiz = raiz.eliminar(criterioBusqueda);
            cantidadNodos--;
            return true;
        }
    }

    @Override
    public boolean insertar(T dato){
        if (raiz == null){
            raiz = new Elemento<>(dato);
            contador = 1;
        }
        else{
            contador = raiz.insertarContando(dato);
        }
        boolean insertado = contador > 0;
        if (insertado){
            cantidadNodos++;
        }
        return insertado;
    }

    @Override
    public int obtenerNivel(Comparable<T> criterioBusqueda){
        if (raiz == null){
            return -1;
        }
        return raiz.obtenerNivel(criterioBusqueda);
    }

    @Override
    public int getContador(){
        return contador;
    }

    @Override
    public T claveMenor(){
        if (raiz == null){
            return null;
        }
        return raiz.claveMenor();
    }

    @Override
    public void enRango(Comparable<T> desde, Comparable<T> hasta, Consumer<T> consumidor){
        enRango(raiz, desde, hasta, consumidor);
    }

    @Override
    public TDALista<T> enRango(Comparable<T> desde, Comparable<T> hasta){
        TDALista<T> resultado = new ListaEnlazada<>();
        enRango(desde, hasta, resultado::agregar);
        return resultado;
    }

    /**
     * In-order con poda.
     *
     * <p>Las dos banderas son lo que evita recorrer el árbol entero. Si el dato del nodo
     * ya es menor que {@code desde}, todo lo que cuelga a su izquierda es todavía menor
     * y no hace falta mirarlo; lo mismo del otro lado con {@code hasta}. Sólo se baja
     * por donde puede haber resultados.</p>
     *
     * <p>Los extremos {@code null} significan "sin cota de ese lado".</p>
     */
    private void enRango(TDAElemento<T> nodo, Comparable<T> desde, Comparable<T> hasta,
                         Consumer<T> consumidor){
        if (nodo == null){
            return;
        }
        boolean alcanzaElDesde = desde == null || desde.compareTo(nodo.getDato()) <= 0;
        boolean noPasaElHasta = hasta == null || hasta.compareTo(nodo.getDato()) >= 0;

        if (alcanzaElDesde){
            enRango(nodo.getHijoIzquierdo(), desde, hasta, consumidor);
        }
        if (alcanzaElDesde && noPasaElHasta){
            consumidor.accept(nodo.getDato());
        }
        if (noPasaElHasta){
            enRango(nodo.getHijoDerecho(), desde, hasta, consumidor);
        }
    }
}
