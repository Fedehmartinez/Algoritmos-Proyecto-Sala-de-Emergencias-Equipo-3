package ucu.edu.aed.implementaciones;

import java.util.function.Consumer;

import ucu.edu.aed.tda.TDAElementoGenerico;
import ucu.edu.aed.tda.TDALista;

public class ElementoGenerico<T extends Comparable<T>> implements TDAElementoGenerico<T>{

    private T dato;
    private TDAElementoGenerico<T> primerHijo;
    private TDAElementoGenerico<T> hermanoDerecho;

    public ElementoGenerico(T datoElemento){
        this.dato = datoElemento;
        primerHijo = null;
        hermanoDerecho = null;
    }

    @Override
    public void setDato(T datoNuevo){
        dato = datoNuevo;
    }

    @Override
    public T getDato(){
        return dato;
    }

    @Override
    public void setPrimerHijo(TDAElementoGenerico<T> nuevoPrimerHijo){
        this.primerHijo = nuevoPrimerHijo;
    }

    @Override
    public TDAElementoGenerico<T> getPrimerHijo(){
        return primerHijo;
    }

    @Override
    public void setHermanoDerecho(TDAElementoGenerico<T> nuevoHermanoDerecho){
        this.hermanoDerecho = nuevoHermanoDerecho;
    }

    @Override
    public TDAElementoGenerico<T> getHermanoDerecho(){
        return hermanoDerecho;
    }

    @Override
    public void agregarHijo(TDAElementoGenerico<T> nuevoHijo){
        if (primerHijo == null){
            primerHijo = nuevoHijo;
        }
        else{
            TDAElementoGenerico<T> ultimoHijo = primerHijo;
            while (ultimoHijo.getHermanoDerecho() != null){
                ultimoHijo = ultimoHijo.getHermanoDerecho();
            }
            ultimoHijo.setHermanoDerecho(nuevoHijo);
        }
    }

    @Override
    public TDAElementoGenerico<T> buscar(Comparable<T> criterioBusqueda){
        if (criterioBusqueda.compareTo(this.dato) == 0){
            return this;
        }
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            TDAElementoGenerico<T> resultado = hijoActual.buscar(criterioBusqueda);
            if (resultado != null){
                return resultado;
            }
            hijoActual = hijoActual.getHermanoDerecho();
        }
        return null;
    }

    @Override
    public TDAElementoGenerico<T> eliminar(Comparable<T> criterioBusqueda){
        if (criterioBusqueda.compareTo(this.dato) == 0){
            return null;
        }
        TDAElementoGenerico<T> hijoActual = primerHijo;
        TDAElementoGenerico<T> hermanoAnterior = null;
        while (hijoActual != null){
            TDAElementoGenerico<T> siguienteHermano = hijoActual.getHermanoDerecho();
            if (hijoActual.eliminar(criterioBusqueda) == null){

                hijoActual.setHermanoDerecho(null);
                if (hermanoAnterior == null){
                    primerHijo = siguienteHermano;
                }
                else{
                    hermanoAnterior.setHermanoDerecho(siguienteHermano);
                }
            }
            else{
                hermanoAnterior = hijoActual;
            }
            hijoActual = siguienteHermano;
        }
        return this;
    }

    @Override
    public void preOrder(Consumer<TDAElementoGenerico<T>> consumidor){
        consumidor.accept(this);
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            hijoActual.preOrder(consumidor);
            hijoActual = hijoActual.getHermanoDerecho();
        }
    }

    @Override
    public void postOrder(Consumer<TDAElementoGenerico<T>> consumidor){
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            hijoActual.postOrder(consumidor);
            hijoActual = hijoActual.getHermanoDerecho();
        }
        consumidor.accept(this);
    }

    @Override
    public boolean esHoja(){
        return primerHijo == null;
    }

    @Override
    public int cantidadHijos(){
        int contador = 0;
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            contador++;
            hijoActual = hijoActual.getHermanoDerecho();
        }
        return contador;
    }

    @Override
    public int cantidadNodos(){
        int contador = 1;
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            contador = contador + hijoActual.cantidadNodos();
            hijoActual = hijoActual.getHermanoDerecho();
        }
        return contador;
    }

    @Override
    public int cantidadHojas(){

        if (primerHijo == null){
            return 1;
        }
        int contador = 0;
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            contador = contador + hijoActual.cantidadHojas();
            hijoActual = hijoActual.getHermanoDerecho();
        }
        return contador;
    }

    @Override
    public int cantidadNodosInternos(){

        if (primerHijo == null){
            return 0;
        }
        int contador = 1;
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            contador = contador + hijoActual.cantidadNodosInternos();
            hijoActual = hijoActual.getHermanoDerecho();
        }
        return contador;
    }

    @Override
    public int altura(){
        int alturaMayor = 0;
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            alturaMayor = Math.max(alturaMayor, hijoActual.altura());
            hijoActual = hijoActual.getHermanoDerecho();
        }

        return 1 + alturaMayor;
    }

    @Override
    public int grado(){
        int gradoMayor = cantidadHijos();
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            gradoMayor = Math.max(gradoMayor, hijoActual.grado());
            hijoActual = hijoActual.getHermanoDerecho();
        }
        return gradoMayor;
    }

    @Override
    public int obtenerNivel(Comparable<T> criterioBusqueda){

        if (criterioBusqueda.compareTo(dato) == 0){
            return 0;
        }
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            int nivel = hijoActual.obtenerNivel(criterioBusqueda);
            if (nivel != -1){
                return 1 + nivel;
            }
            hijoActual = hijoActual.getHermanoDerecho();
        }

        return -1;
    }

    @Override
    public TDALista<T> hijos(){
        TDALista<T> resultado = new ListaEnlazada<>();
        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            resultado.agregar(hijoActual.getDato());
            hijoActual = hijoActual.getHermanoDerecho();
        }
        return resultado;
    }

    @Override
    public TDALista<T> enNivel(int nivel){
        TDALista<T> resultado = new ListaEnlazada<>();
        enNivel(nivel, resultado);
        return resultado;
    }

    @Override
    public void enNivel(int nivel, TDALista<T> acumulador){
        if (nivel < 0){
            return;
        }
        if (nivel == 0){
            acumulador.agregar(this.dato);
            return;
        }

        TDAElementoGenerico<T> hijoActual = primerHijo;
        while (hijoActual != null){
            hijoActual.enNivel(nivel - 1, acumulador);
            hijoActual = hijoActual.getHermanoDerecho();
        }
    }

}
