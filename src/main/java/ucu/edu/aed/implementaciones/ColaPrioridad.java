package ucu.edu.aed.implementaciones;

import java.util.Comparator;
import java.util.NoSuchElementException;

import ucu.edu.aed.tda.TDACola;

public class ColaPrioridad<T> extends ListaEnlazada<T> implements TDACola<T> {
    private final Comparator<T> comparador;
    public ColaPrioridad(Comparator<T> comparador){
        if (comparador == null){
            throw new NoSuchElementException("Se deben pasar 2 datos para comparar");
        }
        this.comparador = comparador;
        }

    @Override /* poner en cola lo que hace es basicamente es poner el nodo en la cabeza, luego de eso reviso si comparador no es null, y voy comparando un con un while contra la prioridad de todos los otros nodos hasta llegar a una que tenga menos prioridad, si no hay ninguno con más prioridad, se queda en 0 (el contador es la posición) y de esa manera se calcula la prioridad cada vez que se agrega un elemento nuevo a la cola */
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

    @Override /* exactamente igual que Cola */
    public T frente(){
    if (esVacio()){
        throw new NoSuchElementException();
    }else {
        return obtener(0);
    }
    }

    @Override /* exactamente igual que cola */
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
    throw new UnsupportedOperationException();
    }
    }

