package ucu.edu.aed.clases;

import java.util.NoSuchElementException;

import ucu.edu.aed.implementaciones.ArbolGenerico;
import ucu.edu.aed.tda.TDALista;

public class CatalogoDiagnosticos {

    private static final String CODIGO_RAIZ = "__RAIZ_CATALOGO__";

    private final ArbolGenerico<NodoCatalogo> arbol;

    public CatalogoDiagnosticos() {
        this.arbol = new ArbolGenerico<>();
        this.arbol.insertarRaiz(new NodoCatalogo(CODIGO_RAIZ, "Catalogo", NivelCatalogo.RAIZ));
    }

    public boolean agregarCapitulo(String codigo, String nombre) {
        return arbol.insertar(NodoCatalogo.porCodigo(CODIGO_RAIZ),
                new NodoCatalogo(codigo, nombre, NivelCatalogo.CAPITULO));
    }

    public boolean agregarGrupo(String codigoCapitulo, String codigo, String nombre) {
        NodoCatalogo capitulo = buscarNodo(codigoCapitulo);
        if (capitulo == null) {
            throw new NoSuchElementException("No existe el capitulo " + codigoCapitulo);
        }
        if (capitulo.getNivel() != NivelCatalogo.CAPITULO) {
            throw new IllegalArgumentException(codigoCapitulo + " no es un capitulo");
        }
        return arbol.insertar(NodoCatalogo.porCodigo(codigoCapitulo),
                new NodoCatalogo(codigo, nombre, NivelCatalogo.GRUPO));
    }

    public boolean agregarCodigo(String codigoGrupo, String codigo, String nombre) {
        NodoCatalogo grupo = buscarNodo(codigoGrupo);
        if (grupo == null) {
            throw new NoSuchElementException("No existe el grupo " + codigoGrupo);
        }
        if (grupo.getNivel() != NivelCatalogo.GRUPO) {
            throw new IllegalArgumentException(codigoGrupo + " no es un grupo");
        }
        return arbol.insertar(NodoCatalogo.porCodigo(codigoGrupo),
                new NodoCatalogo(codigo, nombre, NivelCatalogo.CODIGO));
    }

    public NodoCatalogo buscarNodo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Debe haber un codigo");
        }
        return arbol.buscar(NodoCatalogo.porCodigo(codigo));
    }

    public boolean esDiagnosticable(String codigo) {
        NodoCatalogo nodo = buscarNodo(codigo);
        return nodo != null && nodo.getNivel() == NivelCatalogo.CODIGO;
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
