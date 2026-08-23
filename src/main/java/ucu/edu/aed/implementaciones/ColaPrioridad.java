package ucu.edu.aed.implementaciones;

import java.util.Comparator;
import java.util.NoSuchElementException;

import ucu.edu.aed.tda.TDACola;

public class ColaPrioridad<T> extends ListaEnlazada<T> implements TDACola<T> {
    private final Comparator<T> comparador;
    public ColaPrioridad(Comparator<T> comparador){
        if (comparador == null){
            throw new NoSuchElementException("El comparador no puede ser null");
        }
        this.comparador = comparador;
        }

    @Override
    public boolean poneEnCola(T dato) {
    Nodo<T> actual = cabeza;
    int contador = 0;
    while (actual != null && comparador.compare(dato, actual.getDato()) >= 0) {
        contador++;
        actual = actual.siguiente;
    }
    super.agregar(contador, dato);
    return true;
    }

    @Override
    public T frente(){
    if (esVacio()){
        throw new NoSuchElementException();
    }else {
        return obtener(0);
    }
    }

    @Override
    public T quitaDeCola(){
        if (esVacio()) {
            throw new NoSuchElementException();
        } else {
            return remover(0);
        }
    }

    @Override
    public void agregar(T elem){ 
    poneEnCola(elem);
    }

    @Override
    public void agregar(int index, T elem){ 
    throw new UnsupportedOperationException("La posición la determina la prioridad");
    }
}

