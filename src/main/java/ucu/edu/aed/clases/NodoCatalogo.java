package ucu.edu.aed.clases;

public class NodoCatalogo implements Comparable<NodoCatalogo> {

    private final String codigo;
    private final String nombre;
    private final NivelCatalogo nivel;

    public NodoCatalogo(String codigo, String nombre, NivelCatalogo nivel) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El nodo del catalogo debe tener codigo");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nodo del catalogo debe tener nombre");
        }
        if (nivel == null) {
            throw new IllegalArgumentException("El nodo del catalogo debe tener nivel");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.nivel = nivel;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public NivelCatalogo getNivel() {
        return nivel;
    }

    @Override
    public int compareTo(NodoCatalogo otro) {
        return codigo.compareTo(otro.codigo);
    }

    public static Comparable<NodoCatalogo> porCodigo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Debe haber un codigo");
        }
        return otro -> codigo.compareTo(otro.getCodigo());
    }

    @Override
    public String toString() {
        return nivel + " " + codigo + " - " + nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NodoCatalogo)) return false;
        return codigo.equals(((NodoCatalogo) obj).codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}
