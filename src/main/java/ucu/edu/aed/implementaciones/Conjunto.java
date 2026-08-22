package ucu.edu.aed.implementaciones;

import ucu.edu.aed.tda.TDAConjunto;

/**
 * Define un Tipo de Dato Abstracto (TDA) Conjunto genérico.
 *
 * <p>Un conjunto es una colección de elementos sin duplicados, donde
 * no importa el orden de almacenamiento.</p>
 *
 * <p>Las operaciones principales permiten agregar, remover, consultar
 * pertenencia y realizar operaciones clásicas de teoría de conjuntos
 * como unión, intersección y diferencia.</p>
 *
 * @param <T> el tipo de los elementos almacenados en el conjunto
 */
public class Conjunto<T> extends ListaEnlazada<T> implements TDAConjunto<T> {
        @Override
    public void agregar(T elem) {
        if (!contiene(elem)) {
            super.agregar(elem);
        }
    }
    
        @Override
    public void agregar(int index, T elem) {
        if (!contiene(elem)) {
            super.agregar(index, elem);
        }
    }

    /**
     * Retorna un nuevo conjunto que representa la unión entre este conjunto
     * y el conjunto recibido como parámetro.
     *
     * @param otro el otro conjunto
     * @return un nuevo conjunto con todos los elementos de ambos conjuntos
     */
    @Override
    public TDAConjunto<T> union(TDAConjunto<T> otro) {
        Conjunto<T> resultado = new Conjunto<>();

        for (int i = 0; i < this.tamanio; i++) {
            resultado.agregar(this.obtener(i));
        }

        for (int i = 0; i < otro.tamaño(); i++) {
            resultado.agregar(otro.obtener(i));
        }

        return resultado;
    }

    /**
     * Retorna un nuevo conjunto que representa la intersección entre este conjunto
     * y el conjunto recibido como parámetro.
     *
     * @param otro el otro conjunto
     * @return un nuevo conjunto con los elementos comunes a ambos conjuntos
     */
    @Override
    public TDAConjunto<T> interseccion(TDAConjunto<T> otro) {
        Conjunto<T> resultado = new Conjunto<>();

        for (int i = 0; i < this.tamanio; i++) {
            T elemento = this.obtener(i);

            if (otro.contiene(elemento)) {
                resultado.agregar(elemento);
            }
        }

        return resultado;
    }
    /**
     * Retorna un nuevo conjunto que representa la diferencia entre este conjunto
     * y el conjunto recibido como parámetro.
     *
     * <p>El resultado contiene los elementos que pertenecen a este conjunto
     * pero no al conjunto dado.</p>
     *
     * @param otro el otro conjunto
     * @return un nuevo conjunto con la diferencia entre ambos conjuntos
     */
    @Override
    public TDAConjunto<T> diferencia(TDAConjunto<T> otro) {
        Conjunto<T> resultado = new Conjunto<>();

        for (int i = 0; i < this.tamanio; i++) {
            T elemento = this.obtener(i);

            if (!otro.contiene(elemento)) {
                resultado.agregar(elemento);
            }
        }

        return resultado;
    }
    /**
     * Determina si este conjunto es subconjunto del conjunto dado.
     *
     * @param otro el otro conjunto
     * @return {@code true} si todos los elementos de este conjunto pertenecen también a {@code otro};
     * {@code false} en caso contrario
     */
    @Override
    public boolean esSubconjuntoDe(TDAConjunto<T> otro) {
        for (int i = 0; i < this.tamanio; i++) {
            if (!otro.contiene(this.obtener(i))) {
                return false;
            }
        }

        return true;
    }
}
