package ucu.edu.aed.implementaciones;

public class AVLElemento<T extends Comparable<T>> extends Elemento<T> {

    private int altura;

    public AVLElemento(T dato) {
        super(dato);
        this.altura = 1;
    }

    @Override
    public int altura() {
        return altura;
    }

    public void actualizarAltura() {
        int alturaIzq = getHijoIzquierdo() != null ? getHijoIzquierdo().altura() : 0;
        int alturaDer = getHijoDerecho() != null ? getHijoDerecho().altura() : 0;
        this.altura = 1 + Math.max(alturaIzq, alturaDer);
    }
}
