package ucu.edu.aed.implementaciones;

public class Nodo<T> {
    private T dato;
    Nodo<T> siguiente;

    Nodo(T dato) {
        this.dato = dato;
    }

    public T getDato(){
        return dato;
    }

    public void setDato( T datoNuevo){
        dato = datoNuevo;
    }

    public Nodo<T> getSiguiente(){
        return siguiente;
    }

}