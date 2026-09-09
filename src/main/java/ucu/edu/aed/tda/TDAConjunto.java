package ucu.edu.aed.tda;

public interface TDAConjunto<T> extends TDALista<T> {

    TDAConjunto<T> union(TDAConjunto<T> otro);

    TDAConjunto<T> interseccion(TDAConjunto<T> otro);

    TDAConjunto<T> diferencia(TDAConjunto<T> otro);

    boolean esSubconjuntoDe(TDAConjunto<T> otro);
}
