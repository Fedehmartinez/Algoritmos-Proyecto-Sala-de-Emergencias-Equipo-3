package ucu.edu.aed.implementaciones;

import java.util.Comparator;
import java.util.function.Predicate;

import ucu.edu.aed.tda.TDALista;

public class ListaDobleEnlazada<T> implements TDALista<T> {

    protected NodoDoble<T> cabeza;
    protected NodoDoble<T> cola;
    protected int tamanio;

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
        }
        else{
            nuevoNodo.anterior = cola;
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
