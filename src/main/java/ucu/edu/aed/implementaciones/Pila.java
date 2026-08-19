package ucu.edu.aed.implementaciones;
import ucu.edu.aed.tda.TDAPila;

/**
 * Define un Tipo de Dato Abstracto (TDA) Pila genérica.
 *
 * <p>Una pila es una estructura de datos lineal que sigue la política
 * LIFO ({@code Last In, First Out}), es decir, el último elemento en ingresar
 * es el primero en salir.</p>
 *
 * <p>Esta interfaz extiende {@link TDALista}, por lo que además hereda las
 * operaciones generales de una lista. Sin embargo, las operaciones propias
 * de la pila modelan el acceso restringido al extremo superior de la estructura.</p>
 *
 * @param <T> el tipo de los elementos almacenados en la pila
 */
public class Pila<T> extends ListaEnlazada<T> implements TDAPila<T> {

    /**
     * Retorna el elemento ubicado en el tope de la pila, sin removerlo.
     *
     * @return el elemento ubicado en el tope de la pila
     * @throws java.util.NoSuchElementException si la pila está vacía
     */
    public T tope(){
        return null;
    }

    /**
     * Remueve y retorna el elemento ubicado en el tope de la pila.
     *
     * <p>Luego de la operación, el elemento inmediatamente inferior,
     * si existe, pasa a ocupar el nuevo tope.</p>
     *
     * @return el elemento removido del tope de la pila
     * @throws java.util.NoSuchElementException si la pila está vacía
     */
    public T saca(){
        return null;
    }

    /**
     * Inserta un elemento en el tope de la pila.
     *
     * @param dato el elemento a insertar
     */
    public void mete(T dato){
    
    }
}