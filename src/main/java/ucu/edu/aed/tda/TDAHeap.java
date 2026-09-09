package ucu.edu.aed.tda;

public interface TDAHeap<T> {

    boolean insertar(T dato);

    T eliminar();

    T minimo();

    boolean esVacio();

    int cantidad();
}
