package ucu.edu.aed.clases;

/**
 * Un código del catálogo institucional de diagnósticos.
 *
 * <p>Es un objeto de valor inmutable. Los eventos guardan referencias a códigos, no
 * copias de la información del catálogo: el nombre de un diagnóstico vive en el
 * catálogo y no se duplica en cada evento que lo usa.</p>
 */
public class CodigoDiagnostico implements Comparable<CodigoDiagnostico> {

    private final String codigo;
    private final String nombre;

    public CodigoDiagnostico(String codigo, String nombre) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo no puede ser vacio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El diagnostico debe tener nombre");
        }
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Orden natural por código, consistente con {@link #equals(Object)}.
     */
    @Override
    public int compareTo(CodigoDiagnostico otro) {
        return codigo.compareTo(otro.codigo);
    }

    /**
     * Criterio de búsqueda por código.
     */
    public static Comparable<CodigoDiagnostico> porCodigo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Debe haber un codigo");
        }
        return otro -> codigo.compareTo(otro.getCodigo());
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CodigoDiagnostico)) return false;
        return codigo.equals(((CodigoDiagnostico) obj).codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}
