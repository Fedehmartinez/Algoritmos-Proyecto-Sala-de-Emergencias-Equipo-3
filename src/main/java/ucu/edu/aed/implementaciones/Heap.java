package ucu.edu.aed.implementaciones;

import java.util.Comparator;

import ucu.edu.aed.tda.TDAHeap;


public class Heap<T> implements TDAHeap<T> {

    private static final int CAPACIDAD_INICIAL = 16;

    private ListaArray<T> datos;
    private final Comparator<T> comparador;

    public Heap(Comparator<T> comparador){
        this(CAPACIDAD_INICIAL, comparador);
    }

    public Heap(int capacidadInicial, Comparator<T> comparador){
        if (comparador == null){
            throw new IllegalArgumentException("El comparador no puede ser null");
        }
        if (capacidadInicial < 1){
            capacidadInicial = 1;
        }
        datos = new ListaArray<>(capacidadInicial);
        this.comparador = comparador;
    }

    @Override
    public boolean esVacio(){
        return datos.esVacio();
    }

    @Override
    public int cantidad(){
        return datos.tamaño();
    }

    @Override
    public T minimo(){
        if (esVacio()){
            return null;
        }
        return datos.obtener(0);
    }

    /**
     * Inserta en la última posición libre y flota. O(log n).
     */
    @Override
    public boolean insertar(T dato){
        if (dato == null){
            return false;
        }
        datos.agregar(dato);
        flotar(datos.tamaño() - 1);
        return true;
    }

    @Override
    public T eliminar(){
        if (esVacio()){
            return null;
        }
        T minimo = datos.obtener(0);
        T ultimo = datos.remover(datos.tamaño() - 1);
        if (!datos.esVacio()){
            datos.establecer(0, ultimo);
            hundir(0);
        }
        return minimo;
    }

    private void flotar(int i){
        while (i > 0){
            int padre = (i - 1) / 2;
            if (comparador.compare(datos.obtener(i), datos.obtener(padre)) >= 0){
                return;
            }
            intercambiar(i, padre);
            i = padre;
        }
    }

    private void hundir(int i){
        while (true){
            int izq = 2 * i + 1;
            int der = 2 * i + 2;
            int menor = i;

            if (izq < datos.tamaño() && comparador.compare(datos.obtener(izq), datos.obtener(menor)) < 0){
                menor = izq;
            }
            if (der < datos.tamaño() && comparador.compare(datos.obtener(der), datos.obtener(menor)) < 0){
                menor = der;
            }
            if (menor == i){
                return;
            }
            intercambiar(i, menor);
            i = menor;
        }
    }

    private void intercambiar(int i, int j){
        T temporal = datos.obtener(i);
        datos.establecer(i, datos.obtener(j));
        datos.establecer(j, temporal);
    }

    @Override
    public String toString(){
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < datos.tamaño(); i++){
            if (i > 0){
                resultado.append(",");
            }
            resultado.append(datos.obtener(i));
        }
        return resultado.toString();
    }
}
