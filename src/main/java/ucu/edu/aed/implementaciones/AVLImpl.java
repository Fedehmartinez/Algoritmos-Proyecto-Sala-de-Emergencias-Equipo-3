package ucu.edu.aed.implementaciones;

import ucu.edu.aed.tda.TDAElemento;

public class AVLImpl<T extends Comparable<T>> extends ArbolBinarioBusqueda<T> {

    @Override
    public boolean insertar(T dato) {
        if (dato == null) {
            return false;
        }

        if (buscar(dato) != null) {
            return false;
        }

        raiz = insertarAVL(raiz, dato);
        cantidadNodos++;
        return true;
    }

    private TDAElemento<T> insertarAVL(TDAElemento<T> nodo, T dato) {

        if (nodo == null) {
            return new AVLElemento<>(dato);
        }
        if (dato.compareTo(nodo.getDato()) < 0) {
            nodo.setHijoIzquierdo(insertarAVL(nodo.getHijoIzquierdo(), dato));
        }
        else if (dato.compareTo(nodo.getDato()) > 0) {
            nodo.setHijoDerecho(insertarAVL(nodo.getHijoDerecho(), dato));
        }

        actualizarAltura(nodo);

        int balance = calcularBalance(nodo);

        if (balance > 1 && dato.compareTo(nodo.getHijoIzquierdo().getDato()) < 0) {

            return rotacionDerecha(nodo);
        }

        if (balance > 1 && dato.compareTo(nodo.getHijoIzquierdo().getDato()) > 0) {

            nodo.setHijoIzquierdo(rotacionIzquierda(nodo.getHijoIzquierdo()));

            return rotacionDerecha(nodo);
        }

        if (balance < -1 && dato.compareTo(nodo.getHijoDerecho().getDato()) > 0) {

            return rotacionIzquierda(nodo);
        }

        if (balance < -1 && dato.compareTo(nodo.getHijoDerecho().getDato()) < 0) {

            nodo.setHijoDerecho(rotacionDerecha(nodo.getHijoDerecho()));

            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {

        if (raiz == null) {
            return false;
        }

        if (buscar(criterioBusqueda) == null) {
            return false;
        }

        raiz = eliminarAVL(raiz, criterioBusqueda);
        cantidadNodos--;

        return true;
    }

    private TDAElemento<T> eliminarAVL(TDAElemento<T> nodo, Comparable<T> criterio) {

        if (nodo == null) {
            return null;
        }

        if (criterio.compareTo(nodo.getDato()) < 0) {

            nodo.setHijoIzquierdo(eliminarAVL(nodo.getHijoIzquierdo(), criterio));
        }
        else if (criterio.compareTo(nodo.getDato()) > 0) {

            nodo.setHijoDerecho(eliminarAVL(nodo.getHijoDerecho(), criterio));
        }
        else {
            nodo = quitarNodoAVL(nodo);
        }

        if (nodo == null) {
            return null;
        }

        return balancearTrasEliminar(nodo);
    }

    private TDAElemento<T> balancearTrasEliminar(TDAElemento<T> tmp) {

        actualizarAltura(tmp);

        int balance = calcularBalance(tmp);

        if (balance > 1 && calcularBalance(tmp.getHijoIzquierdo()) >= 0) {

            return rotacionDerecha(tmp);
        }

        if (balance > 1 && calcularBalance(tmp.getHijoIzquierdo()) < 0) {

            tmp.setHijoIzquierdo(rotacionIzquierda(tmp.getHijoIzquierdo()));

            return rotacionDerecha(tmp);
        }

        if (balance < -1 && calcularBalance(tmp.getHijoDerecho()) <= 0) {

            return rotacionIzquierda(tmp);
        }

        if (balance < -1 && calcularBalance(tmp.getHijoDerecho()) > 0) {

            tmp.setHijoDerecho(rotacionDerecha(tmp.getHijoDerecho()));

            return rotacionIzquierda(tmp);
        }

        return tmp;
    }

    private TDAElemento<T> quitarNodoAVL(TDAElemento<T> nodo) {
        if (nodo.getHijoIzquierdo() == null) {
            return nodo.getHijoDerecho();
        }
        if (nodo.getHijoDerecho() == null) {
            return nodo.getHijoIzquierdo();
        }
        // El predecesor (el mas a la derecha del subarbol izquierdo) se saca
        // reusando la misma eliminacion recursiva, para que las alturas y el
        // balanceo se actualicen correctamente en todo el camino, no solo en
        // el padre inmediato del predecesor.
        TDAElemento<T> elPredecesor = masALaDerecha(nodo.getHijoIzquierdo());
        TDAElemento<T> nuevaIzquierda = eliminarAVL(nodo.getHijoIzquierdo(), elPredecesor.getDato());
        elPredecesor.setHijoIzquierdo(nuevaIzquierda);
        elPredecesor.setHijoDerecho(nodo.getHijoDerecho());
        return balancearTrasEliminar(elPredecesor);
    }

    private TDAElemento<T> masALaDerecha(TDAElemento<T> nodo) {
        while (nodo.getHijoDerecho() != null) {
            nodo = nodo.getHijoDerecho();
        }
        return nodo;
    }

    private void actualizarAltura(TDAElemento<T> nodo) {
        if (nodo instanceof AVLElemento) {
            ((AVLElemento<T>) nodo).actualizarAltura();
        }
    }

    private int calcularBalance(TDAElemento<T> nodo) {

        if (nodo == null) {
            return 0;
        }

        int alturaIzq = 0;
        int alturaDer = 0;

        if (nodo.getHijoIzquierdo() != null) {
            alturaIzq = nodo.getHijoIzquierdo().altura();
        }

        if (nodo.getHijoDerecho() != null) {
            alturaDer = nodo.getHijoDerecho().altura();
        }

        return alturaIzq - alturaDer;
    }

    private TDAElemento<T> rotacionDerecha(TDAElemento<T> nodo) {

        TDAElemento<T> nuevoRaiz = nodo.getHijoIzquierdo();
        TDAElemento<T> subArbol = nuevoRaiz.getHijoDerecho();

        nuevoRaiz.setHijoDerecho(nodo);
        nodo.setHijoIzquierdo(subArbol);

        actualizarAltura(nodo);
        actualizarAltura(nuevoRaiz);

        return nuevoRaiz;
    }

    private TDAElemento<T> rotacionIzquierda(TDAElemento<T> nodo) {

        TDAElemento<T> nuevoRaiz = nodo.getHijoDerecho();
        TDAElemento<T> subArbol = nuevoRaiz.getHijoIzquierdo();

        nuevoRaiz.setHijoIzquierdo(nodo);
        nodo.setHijoDerecho(subArbol);

        actualizarAltura(nodo);
        actualizarAltura(nuevoRaiz);

        return nuevoRaiz;
    }
}
