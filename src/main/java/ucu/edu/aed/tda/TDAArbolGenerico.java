package ucu.edu.aed.tda;

import java.util.function.Consumer;

public interface TDAArbolGenerico<T> {

    boolean insertarRaiz(T dato);

    boolean insertar(Comparable<T> criterioPadre, T dato);

    T buscar(Comparable<T> criterioBusqueda);

    TDAElementoGenerico<T> obtenerRaiz();

    boolean eliminar(Comparable<T> criterioBusqueda);

    void preOrder(Consumer<T> consumidor);

    void postOrder(Consumer<T> consumidor);

    void porNiveles(Consumer<T> consumidor);

    void preOrderDesde(Comparable<T> criterioSubarbol, Consumer<T> consumidor);

    void postOrderDesde(Comparable<T> criterioSubarbol, Consumer<T> consumidor);

    TDALista<T> descendientesDe(Comparable<T> criterioSubarbol);

    TDALista<T> hojasDe(Comparable<T> criterioSubarbol);

    TDALista<T> hijosDe(Comparable<T> criterioPadre);

    TDALista<T> enNivel(int nivel);

    String preOrderString();

    String postOrderString();

    String porNivelesString();

    boolean esVacio();

    int altura();

    int cantidadNodos();

    int cantidadHojas();

    int cantidadNodosInternos();

    int grado();

    int obtenerNivel(Comparable<T> criterioBusqueda);
}
