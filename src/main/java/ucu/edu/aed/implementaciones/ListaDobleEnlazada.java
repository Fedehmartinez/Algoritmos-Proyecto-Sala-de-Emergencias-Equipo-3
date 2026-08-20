package ucu.edu.aed.implementaciones;

import java.util.Comparator;
import java.util.function.Predicate;

import ucu.edu.aed.tda.TDALista;

public class ListaDobleEnlazada<T> implements TDALista<T> {

    protected NodoDoble<T> cabeza;
    protected NodoDoble<T> cola;
    protected int tamanio;

    /**
     * Busca el nodo ubicado en la posición indicada, recorriendo desde
     * el extremo más cercano (cabeza o cola) según convenga.
     *
     * <p>Costo O(n) en el peor caso, pero como máximo tamaño/2 pasos,
     * a diferencia de una lista simplemente enlazada que siempre debe
     * recorrer desde la cabeza.</p>
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

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param elem el elemento a agregar
     */
    @Override
    public void agregar(T elem){
        NodoDoble<T> nuevoNodo = new NodoDoble<>(elem);
        if (cabeza == null){
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        }
        else{
            nuevoNodo.anterior = cola;
            cola.siguiente = nuevoNodo;
            cola = nuevoNodo;
        }
        tamanio++;
    }

    /**
     * Agrega un elemento en la posición indicada.
     *
     * <p>Los elementos ubicados desde esa posición en adelante
     * desplazan su índice una posición hacia la derecha.</p>
     *
     * @param index la posición en la que se insertará el elemento
     * @param elem el elemento a agregar
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    @Override
    public void agregar(int index, T elem){
        if (index < 0 || index > tamanio){
            throw new IndexOutOfBoundsException();
        }
        if (index == tamanio){
            agregar(elem);
            return;
        }
        if (index == 0){
            NodoDoble<T> nuevoNodo = new NodoDoble<>(elem);
            nuevoNodo.siguiente = cabeza;
            cabeza.anterior = nuevoNodo;
            cabeza = nuevoNodo;
            tamanio++;
            return;
        }
        NodoDoble<T> siguienteNodo = obtenerNodo(index);
        NodoDoble<T> anteriorNodo = siguienteNodo.anterior;
        NodoDoble<T> nuevoNodo = new NodoDoble<>(elem);
        nuevoNodo.anterior = anteriorNodo;
        nuevoNodo.siguiente = siguienteNodo;
        anteriorNodo.siguiente = nuevoNodo;
        siguienteNodo.anterior = nuevoNodo;
        tamanio++;
    }

    /**
     * Obtiene el elemento almacenado en la posición indicada.
     *
     * @param index la posición del elemento a recuperar
     * @return el elemento ubicado en la posición indicada
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    @Override
    public T obtener(int index){
        if (index < 0 || index >= tamanio){
            throw new IndexOutOfBoundsException();
        }
        return obtenerNodo(index).getDato();
    }

    /**
     * Remueve y devuelve el elemento almacenado en la posición indicada.
     *
     * <p>Los elementos posteriores, si existen, desplazan su índice
     * una posición hacia la izquierda.</p>
     *
     * <p>Comportamiento "Quitar": el nodo se desconecta de la lista y
     * sus campos siguiente/anterior se dejan en null para evitar
     * referencias residuales hacia el resto de la lista.</p>
     *
     * @param index la posición del elemento a remover
     * @return el elemento removido
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    @Override
    public T remover(int index){
        if (index < 0 || index >= tamanio){
            throw new IndexOutOfBoundsException();
        }
        NodoDoble<T> nodoRemovido = obtenerNodo(index);
        NodoDoble<T> anteriorNodo = nodoRemovido.anterior;
        NodoDoble<T> siguienteNodo = nodoRemovido.siguiente;

        if (anteriorNodo != null){
            anteriorNodo.siguiente = siguienteNodo;
        }
        else{
            cabeza = siguienteNodo;
        }

        if (siguienteNodo != null){
            siguienteNodo.anterior = anteriorNodo;
        }
        else{
            cola = anteriorNodo;
        }

        nodoRemovido.siguiente = null;
        nodoRemovido.anterior = null;
        tamanio--;
        return nodoRemovido.getDato();
    }

    /**
     * Remueve la primera ocurrencia del elemento indicado en la lista.
     *
     * <p>La comparación del elemento queda sujeta al criterio definido
     * por la implementación, normalmente mediante {@code equals}.</p>
     *
     * <p>Comportamiento "Eliminar": el nodo se desconecta de la lista y
     * sus campos siguiente/anterior se dejan en null para evitar
     * referencias residuales hacia el resto de la lista.</p>
     *
     * @param elem el elemento a remover
     * @return {@code true} si el elemento fue encontrado y removido;
     *         {@code false} en caso contrario
     */
    @Override
    public boolean remover(T elem){
        NodoDoble<T> actual = cabeza;
        while (actual != null){
            if (actual.getDato().equals(elem)){
                NodoDoble<T> anteriorNodo = actual.anterior;
                NodoDoble<T> siguienteNodo = actual.siguiente;

                if (anteriorNodo != null){
                    anteriorNodo.siguiente = siguienteNodo;
                }
                else{
                    cabeza = siguienteNodo;
                }

                if (siguienteNodo != null){
                    siguienteNodo.anterior = anteriorNodo;
                }
                else{
                    cola = anteriorNodo;
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

    /**
     * Determina si la lista contiene el elemento indicado.
     *
     * @param elem el elemento a buscar
     * @return {@code true} si el elemento está presente en la lista;
     *         {@code false} en caso contrario
     */
    @Override
    public boolean contiene(T elem){
        NodoDoble<T> actual = cabeza;
        while (actual != null){
            if (actual.getDato().equals(elem)){
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Retorna el índice de la primera ocurrencia del elemento indicado.
     *
     * @param elem el elemento a buscar
     * @return el índice de la primera ocurrencia del elemento, o {@code -1}
     *         si el elemento no se encuentra en la lista
     */
    @Override
    public int indiceDe(T elem){
        NodoDoble<T> actual = cabeza;
        int contador = 0;
        while (actual != null){
            if (actual.getDato().equals(elem)){
                return contador;
            }
            contador++;
            actual = actual.siguiente;
        }
        return -1;
    }

    /**
     * Busca y retorna el primer elemento que cumple con el criterio dado.
     *
     * @param criterio el predicado que define la condición de búsqueda
     * @return el primer elemento que cumple el criterio, o {@code null}
     *         si no existe ninguno
     */
    @Override
    public T buscar(Predicate<T> criterio){
        NodoDoble<T> actual = cabeza;
        while (actual != null){
            if (criterio.test(actual.getDato())){
                return actual.getDato();
            }
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Retorna una nueva lista con los elementos ordenados según el comparador dado.
     *
     * @param comparator el comparador que define el orden de los elementos
     * @return una lista ordenada según el criterio indicado
     */
    @Override
    public TDALista<T> ordenar(Comparator<T> comparator){
        ListaDobleEnlazada<T> resultado = new ListaDobleEnlazada<>();
        NodoDoble<T> actual = cabeza;
        while (actual != null){
            resultado.agregarOrdenado(actual.getDato(), comparator);
            actual = actual.siguiente;
        }
        return resultado;
    }

    private void agregarOrdenado(T dato, Comparator<T> comparator){
        NodoDoble<T> nuevoNodo = new NodoDoble<>(dato);
        if (cabeza == null || comparator.compare(dato, cabeza.getDato()) < 0){
            nuevoNodo.siguiente = cabeza;
            if (cabeza != null){
                cabeza.anterior = nuevoNodo;
            }
            cabeza = nuevoNodo;
            if (cola == null){
                cola = nuevoNodo;
            }
        }
        else{
            NodoDoble<T> actual = cabeza;
            while (actual.siguiente != null && comparator.compare(dato, actual.siguiente.getDato()) >= 0){
                actual = actual.siguiente;
            }
            nuevoNodo.siguiente = actual.siguiente;
            nuevoNodo.anterior = actual;
            if (actual.siguiente != null){
                actual.siguiente.anterior = nuevoNodo;
            }
            actual.siguiente = nuevoNodo;
            if (nuevoNodo.siguiente == null){
                cola = nuevoNodo;
            }
        }
        tamanio++;
    }

    /**
     * Retorna la cantidad de elementos almacenados en la lista.
     *
     * @return la cantidad de elementos de la lista
     */
    @Override
    public int tamaño(){
        return tamanio;
    }

    /**
     * Determina si la lista no contiene elementos.
     *
     * @return {@code true} si la lista está vacía;
     *         {@code false} en caso contrario
     */
    @Override
    public boolean esVacio(){
        return cabeza == null;
    }

    /**
     * Elimina todos los elementos de la lista.
     */
    @Override
    public void vaciar(){
        cabeza = null;
        cola = null;
        tamanio = 0;
    }
}