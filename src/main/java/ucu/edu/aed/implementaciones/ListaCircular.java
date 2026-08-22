package ucu.edu.aed.implementaciones;

import java.util.Comparator;
import java.util.function.Predicate;

import ucu.edu.aed.tda.TDALista;

public class ListaCircular<T> implements TDALista<T> {

    protected Nodo<T> cabeza;
    protected Nodo<T> cola;
    protected int tamanio;

    @Override
    public void agregar(T elem){
        Nodo<T> nuevoNodo = new Nodo<>(elem);
        if (cabeza == null){
            cabeza = nuevoNodo;
            cola = nuevoNodo;
            nuevoNodo.siguiente = nuevoNodo;
        }
        else{
            nuevoNodo.siguiente = cabeza;
            cola.siguiente = nuevoNodo;
            cola = nuevoNodo;
        }
        tamanio++;
    }

    @Override
    public void agregar(int index, T elem){
        if (index < 0 || index > tamanio){
            throw new IndexOutOfBoundsException();
        }
        if (index == 0){
            Nodo<T> nuevoNodo = new Nodo<>(elem);
            if (tamanio == 0){
                cabeza = nuevoNodo;
                cola = nuevoNodo;
                nuevoNodo.siguiente = nuevoNodo;
            }
            else{
                nuevoNodo.siguiente = cabeza;
                cola.siguiente = nuevoNodo;
                cabeza = nuevoNodo;
            }
            tamanio++;
            return;
        }
        Nodo<T> actual = cabeza;
        int contador = 0;
        while (contador < index - 1){
            actual = actual.siguiente;
            contador++;
        }
        Nodo<T> nuevoNodo = new Nodo<>(elem);
        nuevoNodo.siguiente = actual.siguiente;
        actual.siguiente = nuevoNodo;
        if (actual == cola){
            cola = nuevoNodo;
        }
        tamanio++;
    }

    @Override
    public T obtener(int index){
        if (index < 0 || index >= tamanio){
            throw new IndexOutOfBoundsException();
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < index; i++){
            actual = actual.siguiente;
        }
        return actual.getDato();
    }

    @Override
    public T remover(int index){
        if (index < 0 || index >= tamanio){
            throw new IndexOutOfBoundsException();
        }
        Nodo<T> nodoRemovido;
        if (index == 0){
            nodoRemovido = cabeza;
            if (tamanio == 1){
                cabeza = null;
                cola = null;
            }
            else{
                cabeza = cabeza.siguiente;
                cola.siguiente = cabeza;
            }
        }
        else{
            Nodo<T> actual = cabeza;
            int contador = 0;
            while (contador < index - 1){
                actual = actual.siguiente;
                contador++;
            }
            nodoRemovido = actual.siguiente;
            actual.siguiente = nodoRemovido.siguiente;
            if (nodoRemovido == cola){
                cola = actual;
            }
        }
        nodoRemovido.siguiente = null;
        tamanio--;
        return nodoRemovido.getDato();
    }

    @Override
    public boolean remover(T elem){
        if (cabeza == null){
            return false;
        }
        if (cabeza.getDato().equals(elem)){
            if (tamanio == 1){
                cabeza = null;
                cola = null;
            }
            else{
                cabeza = cabeza.siguiente;
                cola.siguiente = cabeza;
            }
            tamanio--;
            return true;
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio - 1; i++){
            if (actual.siguiente.getDato().equals(elem)){
                Nodo<T> nodoRemovido = actual.siguiente;
                actual.siguiente = nodoRemovido.siguiente;
                if (nodoRemovido == cola){
                    cola = actual;
                }
                nodoRemovido.siguiente = null;
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
        Nodo<T> actual = cabeza;
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
        Nodo<T> actual = cabeza;
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
        ListaCircular<T> resultado = new ListaCircular<>();
        Nodo<T> actual = cabeza;
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
