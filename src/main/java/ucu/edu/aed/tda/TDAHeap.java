package ucu.edu.aed.tda;

public interface TDAHeap<T> {

    boolean insertar(T dato);

    T eliminar();

    T minimo();

    boolean remover(T elem);

    boolean esVacio();

    int cantidad();
}
