package ucu.edu.aed.clases;

import java.util.NoSuchElementException;

import ucu.edu.aed.implementaciones.ArbolGenerico;
import ucu.edu.aed.tda.TDALista;

public class CatalogoDiagnosticos {

    private static final String codigoRaiz = "Raiz";

    private final ArbolGenerico<NodoCatalogo> arbol;

    public CatalogoDiagnosticos() {
        this.arbol = new ArbolGenerico<>();
        this.arbol.insertarRaiz(new NodoCatalogo(codigoRaiz, "Catalogo", NivelCatalogo.RAIZ));
    }

    public boolean agregarCapitulo(String codigo, String nombre) {
        return agregarBajo(codigoRaiz, NivelCatalogo.RAIZ, codigo, nombre, NivelCatalogo.CAPITULO);
    }

    public boolean agregarGrupo(String codigoCapitulo, String codigo, String nombre) {
        return agregarBajo(codigoCapitulo, NivelCatalogo.CAPITULO, codigo, nombre, NivelCatalogo.GRUPO);
    }

    public boolean agregarCodigo(String codigoGrupo, String codigo, String nombre) {
        return agregarBajo(codigoGrupo, NivelCatalogo.GRUPO, codigo, nombre, NivelCatalogo.CODIGO);
    }

    private boolean agregarBajo(String codigoPadre, NivelCatalogo nivelPadreEsperado, String codigo, String nombre, NivelCatalogo nivelNuevo) {
        NodoCatalogo padre = buscarNodo(codigoPadre);
        if (padre == null) {
            throw new NoSuchElementException("No existe " + codigoPadre + " en el catalogo");
        }
        if (padre.getNivel() != nivelPadreEsperado) {
            throw new IllegalArgumentException(codigoPadre + " no es un " + nivelPadreEsperado);
        }
        return arbol.insertar(NodoCatalogo.porCodigo(codigoPadre), new NodoCatalogo(codigo, nombre, nivelNuevo));
    }

    public NodoCatalogo buscarNodo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Debe haber un codigo");
        }
        return arbol.buscar(NodoCatalogo.porCodigo(codigo));
    }

    public TDALista<NodoCatalogo> codigosBajo(String codigo) {
        if (buscarNodo(codigo) == null) {
            throw new NoSuchElementException("No existe el nodo " + codigo + " en el catalogo");
        }
        return arbol.hojasDe(NodoCatalogo.porCodigo(codigo));
    }

    public boolean esVacio() {
        return arbol.cantidadNodos() <= 1;
    }

    public int cantidadNodos() {
        return arbol.cantidadNodos() - 1;
    }
}
