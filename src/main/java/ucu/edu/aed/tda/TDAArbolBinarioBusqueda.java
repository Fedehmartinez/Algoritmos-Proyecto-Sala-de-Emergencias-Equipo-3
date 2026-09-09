package ucu.edu.aed.tda;

import java.util.function.Consumer;

public interface TDAArbolBinarioBusqueda<T extends Comparable<T>> extends TDAArbolBinario<T> {

    int getContador();

    void enRango(Comparable<T> desde, Comparable<T> hasta, Consumer<T> consumidor);

    TDALista<T> enRango(Comparable<T> desde, Comparable<T> hasta);
}
