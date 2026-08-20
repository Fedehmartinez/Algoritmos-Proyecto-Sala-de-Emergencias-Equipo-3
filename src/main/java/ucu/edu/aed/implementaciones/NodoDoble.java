package ucu.edu.aed.implementaciones;

public class NodoDoble<T> {

    T dato;
    NodoDoble<T> siguiente;
    NodoDoble<T> anterior;

    public NodoDoble(T dato){
        this.dato = dato;
    }

    public T getDato(){
        return dato;
    }
}