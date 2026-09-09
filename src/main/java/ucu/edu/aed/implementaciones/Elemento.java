package ucu.edu.aed.implementaciones;

import java.util.function.Consumer;

import ucu.edu.aed.tda.TDAElemento;
import ucu.edu.aed.tda.TDALista;

public class Elemento<T extends Comparable<T>> implements TDAElemento<T>{

    private T dato;
    private TDAElemento<T> hijoIzq;
    private TDAElemento<T> hijoDer;

    public Elemento(T datoElemento){
        this.dato = datoElemento;
        hijoDer = null;
        hijoIzq = null;
    }

    @Override
    public void setHijoIzquierdo(TDAElemento<T> hijoIzquierdo){
        this.hijoIzq = hijoIzquierdo;
    }

    @Override
    public void setHijoDerecho(TDAElemento<T> hijoDerecho){
        this.hijoDer = hijoDerecho;
    }

    @Override
    public TDAElemento<T> getHijoIzquierdo(){
        return hijoIzq;
    }

    @Override
    public TDAElemento<T> getHijoDerecho(){
        return hijoDer;
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
    public TDAElemento<T> buscar(Comparable<T> criterioBusqueda){
        TDAElemento<T> resultado = null;
        if (criterioBusqueda.compareTo(this.dato) == 0){
            resultado = this;
        }
        else{
            if (criterioBusqueda.compareTo(this.dato) < 0){

                if (hijoIzq != null){
                resultado = hijoIzq.buscar(criterioBusqueda);
                }
            }
            else{

                if (hijoDer != null){
                    resultado = hijoDer.buscar(criterioBusqueda);
                }
            }
        }
        return resultado;
    }

    @Override
    public TDAElemento<T> eliminar(Comparable<T> criterioBusqueda){
        if (criterioBusqueda.compareTo(dato) < 0){

            if (this.hijoIzq != null){
                this.hijoIzq = this.hijoIzq.eliminar(criterioBusqueda);
            }
            return this;
        }
        else{
            if (criterioBusqueda.compareTo(dato) > 0){

                if (this.hijoDer != null){
                    this.hijoDer = this.hijoDer.eliminar(criterioBusqueda);
                }
                return this;
            }
        }
        return quitarNodo();
    }

    private TDAElemento<T> quitarNodo(){
        if (this.hijoIzq == null){
            return this.hijoDer;
        }
        else{
            if (this.hijoDer == null){
                return this.hijoIzq;
            }
            else{

                TDAElemento<T> elHijo = this.hijoIzq;
                TDAElemento<T> elPadre = this;
                while (elHijo.getHijoDerecho() != null){
                    elPadre = elHijo;
                    elHijo = elHijo.getHijoDerecho();
                }
                if (elPadre != this){
                    elPadre.setHijoDerecho(elHijo.getHijoIzquierdo());
                    elHijo.setHijoIzquierdo(this.hijoIzq);  
                }
                elHijo.setHijoDerecho(hijoDer);
                return elHijo;
            }
        }
    }

    @Override
    public boolean insertar(T nuevoDato){
        if (nuevoDato.compareTo(this.dato) > 0){

            if (hijoDer == null){
                hijoDer = new Elemento<>(nuevoDato);
                return true;
            }
            else{
                return hijoDer.insertar(nuevoDato);
            }
        }
        else{
            if (nuevoDato.compareTo(this.dato) < 0){

                if (hijoIzq == null){
                    hijoIzq = new Elemento<>(nuevoDato);
                    return true;
                }
                else{
                    return hijoIzq.insertar(nuevoDato);
                }
            }
        }
        return false;
    }

    @Override
    public int insertarContando(T nuevoDato){
    int dato = nuevoDato.compareTo(this.dato);

    if (dato == 0){
        return 0;
    }

    if (dato > 0){

        if (hijoDer == null){
            hijoDer = new Elemento<>(nuevoDato);
            return 1;
        }
        int contadorHijo = hijoDer.insertarContando(nuevoDato);
        if (contadorHijo == 0){
            return 0;
        }
        return 1 + contadorHijo;
    }
    else{

        if (hijoIzq == null){
            hijoIzq = new Elemento<>(nuevoDato);
            return 1;
        }
        int contadorHijo = hijoIzq.insertarContando(nuevoDato);
        if (contadorHijo == 0){
            return 0;
        }
        return 1 + contadorHijo;
        }
    }

    @Override
    public void inOrder(Consumer<TDAElemento<T>> consumidor){
        if (this.hijoIzq != null){
            this.hijoIzq.inOrder(consumidor);
        }
        consumidor.accept(this);
        if (this.hijoDer != null){
            this.hijoDer.inOrder(consumidor);
        }
    }

    @Override
    public void preOrder(Consumer<TDAElemento<T>> consumidor){
        consumidor.accept(this);
        if (hijoIzq != null){
            this.hijoIzq.preOrder(consumidor);
        }
        if (hijoDer != null){
            this.hijoDer.preOrder(consumidor);
        }
    }

    @Override
    public void postOrder(Consumer<TDAElemento<T>> consumidor){
        if (hijoIzq != null){
            this.hijoIzq.postOrder(consumidor);
        }
        if (hijoDer != null){
            this.hijoDer.postOrder(consumidor);
        }
        consumidor.accept(this);
    }

    @Override
    public boolean esHoja(){
        if (hijoDer == null && hijoIzq == null){
            return true;
        }
        return false;
    }

    @Override
    public int cantidadHojas(){

        if (this.hijoIzq == null && this.hijoDer == null){
            return 1;
        }
        int contadorIzq = 0;
        int contadorDer = 0;

        if (this.hijoIzq != null){
            contadorIzq = this.hijoIzq.cantidadHojas();
        }

        if (this.hijoDer != null){
            contadorDer = this.hijoDer.cantidadHojas();
        }
        return contadorIzq + contadorDer;
    }

    @Override
    public int cantidadNodosInternos(){

        if (this.hijoIzq == null && this.hijoDer == null){
            return 0;
        }
        int contadorIzq = 0;
        int contadorDer = 0;
        if (this.hijoIzq != null){
            contadorIzq = this.hijoIzq.cantidadNodosInternos();
        }
        if (this.hijoDer != null){
            contadorDer = this.hijoDer.cantidadNodosInternos();
        }

        return 1 + contadorIzq + contadorDer;
    }

    @Override
    public int cantidadNodos(){
        int contadorIzq = 0;
        int contadorDer = 0;
        if (this.hijoIzq != null){
            contadorIzq = this.hijoIzq.cantidadNodos();
        }
        if (this.hijoDer != null){
            contadorDer = this.hijoDer.cantidadNodos();
        }

        return 1 + contadorIzq + contadorDer;
    }

    @Override
    public int altura(){

        if (this.hijoIzq == null && this.hijoDer == null){
            return 1;
        }
        int alturaIzq = 0;
        int alturaDer = 0;
        if (this.hijoIzq != null){
            alturaIzq = this.hijoIzq.altura();
        }
        if (this.hijoDer != null){
            alturaDer = this.hijoDer.altura();
        }

        return 1 + Math.max(alturaIzq, alturaDer);
    }

    @Override
    public int obtenerNivel(Comparable<T> criterioBusqueda){

        if (criterioBusqueda.compareTo(dato) == 0){
            return 0;
        }

        if (criterioBusqueda.compareTo(dato) < 0){
            if (this.hijoIzq != null){
                int nivel = this.hijoIzq.obtenerNivel(criterioBusqueda);
                if (nivel != -1){
                    return 1 + nivel;
                }
            }
        }
        else{

            if (this.hijoDer != null){
                int nivel = this.hijoDer.obtenerNivel(criterioBusqueda);
                if (nivel != -1){
                    return 1 + nivel;
                }
            }
        }

        return -1;
    }

    @Override
    public T claveMenor(){
        TDAElemento<T> elementoActual = this;
        while (elementoActual.getHijoIzquierdo() != null){
            elementoActual = elementoActual.getHijoIzquierdo();
        }
        return elementoActual.getDato();
    }

    @Override
    public TDALista<T> completos(){
        TDALista<T> resultado = new ListaEnlazada<>();
        if (this.hijoIzq != null && this.hijoDer != null){
            resultado.agregar(this.dato);
        }
        if (this.hijoIzq != null){
            TDALista<T> completosIzq = this.hijoIzq.completos();
            for (int i = 0; i < completosIzq.tamaño(); i++){
                resultado.agregar(completosIzq.obtener(i));
            }
        }
        if (this.hijoDer != null){
            TDALista<T> completosDer = this.hijoDer.completos();
            for (int i = 0; i < completosDer.tamaño(); i++){
                resultado.agregar(completosDer.obtener(i));
            }
        }
        return resultado;
    }

    @Override
    public TDALista<T> enNivel(int nivel){
        TDALista<T> resultado = new ListaEnlazada<>();
        if (nivel == 0){
            resultado.agregar(this.dato);
            return resultado;
        }
        if (this.hijoIzq != null){
            TDALista<T> izq = this.hijoIzq.enNivel(nivel - 1);
            for (int i = 0; i < izq.tamaño(); i++){
                resultado.agregar(izq.obtener(i));
            }
        }
        if (this.hijoDer != null){
            TDALista<T> der = this.hijoDer.enNivel(nivel - 1);
            for (int i = 0; i < der.tamaño(); i++){
                resultado.agregar(der.obtener(i));
            }
        }
        return resultado;
    }

}
