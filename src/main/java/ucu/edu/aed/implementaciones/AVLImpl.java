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

        // LL
        if (balance > 1 && dato.compareTo(nodo.getHijoIzquierdo().getDato()) < 0) {

            return rotacionDerecha(nodo);
        }

        // LR
        if (balance > 1 && dato.compareTo(nodo.getHijoIzquierdo().getDato()) > 0) {

            nodo.setHijoIzquierdo(rotacionIzquierda(nodo.getHijoIzquierdo()));

            return rotacionDerecha(nodo);
        }

        // RR
        if (balance < -1 && dato.compareTo(nodo.getHijoDerecho().getDato()) > 0) {

            return rotacionIzquierda(nodo);
        }

        // RL
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

        TDAElemento<T> tmp = nodo;

        if (criterio.compareTo(nodo.getDato()) < 0) {

            nodo.setHijoIzquierdo(eliminarAVL(nodo.getHijoIzquierdo(), criterio));
        }
        else if (criterio.compareTo(nodo.getDato()) > 0) {

            nodo.setHijoDerecho(eliminarAVL(nodo.getHijoDerecho(), criterio));
        }
        else {
            tmp = quitarNodoAVL(nodo);
        }

        if (tmp == null) {
            return null;
        }

        actualizarAltura(tmp);

        int balance = calcularBalance(tmp);

        // LL
        if (balance > 1 && calcularBalance(tmp.getHijoIzquierdo()) >= 0) {

            return rotacionDerecha(tmp);
        }

        // LR
        if (balance > 1 && calcularBalance(tmp.getHijoIzquierdo()) < 0) {

            tmp.setHijoIzquierdo(rotacionIzquierda(tmp.getHijoIzquierdo()));

            return rotacionDerecha(tmp);
        }

        // RR
        if (balance < -1 && calcularBalance(tmp.getHijoDerecho()) <= 0) {

            return rotacionIzquierda(tmp);
        }

        // RL
        if (balance < -1 && calcularBalance(tmp.getHijoDerecho()) > 0) {

            tmp.setHijoDerecho(rotacionDerecha(tmp.getHijoDerecho()));

            return rotacionIzquierda(tmp);
        }

        return tmp;
    }

    /**
     * Quita un nodo con dos hijos reemplazándolo por su predecesor inorden,
     * actualizando la altura cacheada de los nodos cuyos hijos cambiaron
     * (el propio quitarNodo de Elemento no lo hace, porque esa clase nunca
     * cachea altura).
     */
    private TDAElemento<T> quitarNodoAVL(TDAElemento<T> nodo) {
        if (nodo.getHijoIzquierdo() == null) {
            return nodo.getHijoDerecho();
        }
        if (nodo.getHijoDerecho() == null) {
            return nodo.getHijoIzquierdo();
        }
        TDAElemento<T> elHijo = nodo.getHijoIzquierdo();
        TDAElemento<T> elPadre = nodo;
        while (elHijo.getHijoDerecho() != null) {
            elPadre = elHijo;
            elHijo = elHijo.getHijoDerecho();
        }
        if (elPadre != nodo) {
            elPadre.setHijoDerecho(elHijo.getHijoIzquierdo());
            actualizarAltura(elPadre);
            elHijo.setHijoIzquierdo(nodo.getHijoIzquierdo());
        }
        elHijo.setHijoDerecho(nodo.getHijoDerecho());
        return elHijo;
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
