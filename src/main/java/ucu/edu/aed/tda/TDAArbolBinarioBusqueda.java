package ucu.edu.aed.tda;

import java.util.function.Consumer;

public interface TDAArbolBinarioBusqueda<T extends Comparable<T>> extends TDAArbolBinario<T> {

    int getContador();

    /**
     * Recorre en orden los datos que caen dentro del rango, ambos extremos incluidos.
     *
     * <p>A diferencia de {@code inOrder}, no visita el árbol entero: en cada nodo usa el
     * orden para descartar el subárbol que no puede contener resultados. Si el nodo ya
     * quedó por debajo de {@code desde}, todo su subárbol izquierdo también, y no se
     * baja; lo simétrico con {@code hasta} y el derecho.</p>
     *
     * <p>Por eso el costo es O(log n + k), con k la cantidad de resultados, en vez del
     * O(n) que cuesta recorrer todo y filtrar.</p>
     *
     * @param desde extremo inferior, o {@code null} para no acotar por abajo
     * @param hasta extremo superior, o {@code null} para no acotar por arriba
     */
    void enRango(Comparable<T> desde, Comparable<T> hasta, Consumer<T> consumidor);

    /**
     * Los datos del rango, en orden, como lista.
     */
    TDALista<T> enRango(Comparable<T> desde, Comparable<T> hasta);
}
