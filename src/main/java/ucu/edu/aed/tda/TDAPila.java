package ucu.edu.aed.tda;

public interface TDAPila<T> extends TDALista<T> {

    T tope();

    T saca();

    void mete(T dato);
}
