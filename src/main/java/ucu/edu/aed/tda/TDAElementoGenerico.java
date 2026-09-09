package ucu.edu.aed.tda;

import java.util.function.Consumer;

public interface TDAElementoGenerico<T> {

    void setDato(T dato);

    T getDato();

    void setPrimerHijo(TDAElementoGenerico<T> primerHijo);

    TDAElementoGenerico<T> getPrimerHijo();

    void setHermanoDerecho(TDAElementoGenerico<T> hermanoDerecho);

    TDAElementoGenerico<T> getHermanoDerecho();

    void agregarHijo(TDAElementoGenerico<T> nuevoHijo);

    TDAElementoGenerico<T> buscar(Comparable<T> criterioBusqueda);

    TDAElementoGenerico<T> eliminar(Comparable<T> criterioBusqueda);

    void preOrder(Consumer<TDAElementoGenerico<T>> consumidor);

    void postOrder(Consumer<TDAElementoGenerico<T>> consumidor);

    boolean esHoja();

    int cantidadHijos();

    int cantidadNodos();

    int cantidadHojas();

    int cantidadNodosInternos();

    int altura();

    int grado();

    int obtenerNivel(Comparable<T> criterioBusqueda);

    TDALista<T> hijos();

    TDALista<T> enNivel(int nivel);

    void enNivel(int nivel, TDALista<T> acumulador);
}
