package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.function.Predicate;

public interface TDALista<T> {

    void agregar(T elem);

    void agregar(int index, T elem);

    T obtener(int index);

    T remover(int index);

    boolean remover(T elem);

    boolean contiene(T elem);

    int indiceDe(T elem);

    T buscar(Predicate<T> criterio);

    TDALista<T> ordenar(Comparator<T> comparator);

    int tamaño();

    boolean esVacio();

    void vaciar();
}
