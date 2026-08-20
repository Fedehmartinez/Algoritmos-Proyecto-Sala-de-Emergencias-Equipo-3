package ucu.edu.aed.implementaciones;

import java.util.Comparator;
import java.util.function.Predicate;

import ucu.edu.aed.tda.TDALista;

/**
 * Implementación de TDALista&lt;T&gt; mediante una lista doblemente enlazada circular.
 *
 * <p>{@code cola.siguiente == cabeza} y {@code cabeza.anterior == cola}, en lugar
 * de {@code null} en los extremos.</p>
 */
public class ListaCircularDoble<T> implements TDALista<T> {

    protected NodoDoble<T> cabeza;
    protected NodoDoble<T> cola;
    protected int tamanio;

    /**
     * Busca el nodo ubicado en la posición indicada, recorriendo desde
     * el extremo más cercano (cabeza o cola) según convenga.
     *
     * <p>Costo O(n) en el peor caso, pero como máximo tamanio/2 pasos,
     * a diferencia de recorrer siempre desde la cabeza.</p>
     *
     * @param index la posición del nodo a recuperar (ya validada por el llamador)
     * @return el nodo ubicado en esa posición
     */
    private NodoDoble<T> obtenerNodo(int index){
        if (index < tamanio / 2){
            NodoDoble<T> actual = cabeza;
            for (int i = 0; i < index; i++){
                actual = actual.siguiente;
            }
            return actual;
        }
        else{
            NodoDoble<T> actual = cola;
            for (int i = tamanio - 1; i > index; i--){
                actual = actual.anterior;
            }
            return actual;
        }
    }

    @Override
    public void agregar(T elem){
        NodoDoble<T> nuevoNodo = new NodoDoble<>(elem);
        if (cabeza == null){
            cabeza = nuevoNodo;
            cola = nuevoNodo;
            nuevoNodo.siguiente = nuevoNodo;
            nuevoNodo.anterior = nuevoNodo;
        }
        else{
            nuevoNodo.anterior = cola;
            nuevoNodo.siguiente = cabeza;
            cola.siguiente = nuevoNodo;
            cabeza.anterior = nuevoNodo;
            cola = nuevoNodo;
        }
        tamanio++;
    }

    /**
     * Agrega un elemento en la posición indicada.
     *
     * <p>A diferencia de una implementación basada en "detenerse un paso antes",
     * esta versión ubica directamente el nodo que hoy ocupa {@code index}
     * (vía {@link #obtenerNodo}) y lo enlaza usando su propio puntero
     * {@code anterior}, sin necesidad de recorrer desde el principio.</p>
     */
    @Override
    public void agregar(int index, T elem){
        if (index < 0 || index > tamanio){
            throw new IndexOutOfBoundsException();
        }
        if (tamanio == 0){
            agregar(elem);
            return;
        }
        if (index == tamanio){
            agregar(elem);
            return;
        }
        NodoDoble<T> nuevoNodo = new NodoDoble<>(elem);
        NodoDoble<T> siguienteNodo = obtenerNodo(index);
        NodoDoble<T> anteriorNodo = siguienteNodo.anterior;

        nuevoNodo.anterior = anteriorNodo;
        nuevoNodo.siguiente = siguienteNodo;
        anteriorNodo.siguiente = nuevoNodo;
        siguienteNodo.anterior = nuevoNodo;

        if (index == 0){
            cabeza = nuevoNodo;
        }
        tamanio++;
    }

    @Override
    public T obtener(int index){
        if (index < 0 || index >= tamanio){
            throw new IndexOutOfBoundsException();
        }
        return obtenerNodo(index).getDato();
    }

    @Override
    public T remover(int index){
        if (index < 0 || index >= tamanio){
            throw new IndexOutOfBoundsException();
        }
        NodoDoble<T> nodoRemovido = obtenerNodo(index);
        if (tamanio == 1){
            cabeza = null;
            cola = null;
        }
        else{
            NodoDoble<T> anteriorNodo = nodoRemovido.anterior;
            NodoDoble<T> siguienteNodo = nodoRemovido.siguiente;
            anteriorNodo.siguiente = siguienteNodo;
            siguienteNodo.anterior = anteriorNodo;
            if (nodoRemovido == cabeza){
                cabeza = siguienteNodo;
            }
            if (nodoRemovido == cola){
                cola = anteriorNodo;
            }
        }
        nodoRemovido.siguiente = null;
        nodoRemovido.anterior = null;
        tamanio--;
        return nodoRemovido.getDato();
    }

    /**
     * Remueve la primera ocurrencia del elemento indicado en la lista.
     *
     * <p>Recorrido único: encuentra y desconecta el nodo en el mismo
     * pasaje, sin volver a recorrer la lista como en una implementación
     * que primero llama a {@code indiceDe} y luego a {@code remover(index)}.</p>
     */
    @Override
    public boolean remover(T elem){
        if (cabeza == null){
            return false;
        }
        NodoDoble<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++){
            if (actual.getDato().equals(elem)){
                if (tamanio == 1){
                    cabeza = null;
                    cola = null;
                }
                else{
                    NodoDoble<T> anteriorNodo = actual.anterior;
                    NodoDoble<T> siguienteNodo = actual.siguiente;
                    anteriorNodo.siguiente = siguienteNodo;
                    siguienteNodo.anterior = anteriorNodo;
                    if (actual == cabeza){
                        cabeza = siguienteNodo;
                    }
                    if (actual == cola){
                        cola = anteriorNodo;
                    }
                }
                actual.siguiente = null;
                actual.anterior = null;
                tamanio--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public boolean contiene(T elem){
        return indiceDe(elem) != -1;
    }

    @Override
    public int indiceDe(T elem){
        NodoDoble<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++){
            if (actual.getDato().equals(elem)){
                return i;
            }
            actual = actual.siguiente;
        }
        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio){
        NodoDoble<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++){
            if (criterio.test(actual.getDato())){
                return actual.getDato();
            }
            actual = actual.siguiente;
        }
        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator){
        ListaCircularDoble<T> resultado = new ListaCircularDoble<>();
        NodoDoble<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++){
            T dato = actual.getDato();
            int pos = 0;
            while (pos < resultado.tamanio && comparator.compare(dato, resultado.obtener(pos)) >= 0){
                pos++;
            }
            resultado.agregar(pos, dato);
            actual = actual.siguiente;
        }
        return resultado;
    }

    @Override
    public int tamaño(){
        return tamanio;
    }

    @Override
    public boolean esVacio(){
        return cabeza == null;
    }

    @Override
    public void vaciar(){
        cabeza = null;
        cola = null;
        tamanio = 0;
    }
}