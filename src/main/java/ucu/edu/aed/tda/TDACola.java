package ucu.edu.aed.tda;

public interface TDACola<T> extends TDALista<T> {

    T frente();

    boolean poneEnCola(T dato);

    T quitaDeCola();
}
