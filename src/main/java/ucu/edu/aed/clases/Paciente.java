package ucu.edu.aed.clases;

import ucu.edu.aed.implementaciones.ListaEnlazada;

/**
 * Un paciente de la sala de emergencias.
 *
 * <p>Su orden natural es por id, que es el criterio de <i>identidad</i>: dos pacientes
 * son el mismo si comparten el documento. Ese es el orden con el que se guarda en el
 * árbol de pacientes registrados.</p>
 *
 * <p>La <i>prioridad</i> de atención es otro criterio distinto, y no vive acá: la
 * define un {@code Comparator} externo (POR_URGENCIA) que usa el heap de espera. Que
 * identidad y prioridad sean criterios separados es deliberado; un paciente no cambia
 * de identidad cuando cambia de urgencia.</p>
 */
public class Paciente implements Comparable<Paciente> {

    private final String id;
    private final String nombre;
    private final ListaEnlazada<String> caracteristicas;
    private NivelUrgencia urgencia;
    private EstadoPaciente estado;

    public Paciente(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.caracteristicas = new ListaEnlazada<>();
        this.urgencia = null; // Se asigna la urgencia más adelante, cuando se agregue a la cola de prioridad
        this.estado = EstadoPaciente.REGISTRADO;
    }

    public String getId() { 
        return id; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public NivelUrgencia getUrgencia() { 
        return urgencia; 
    }

    public void setUrgencia(NivelUrgencia urgencia) { 
        this.urgencia = urgencia; 
    }

    public EstadoPaciente getEstadoPaciente(){
        return estado;
    }

    public void setEstadoPaciente(EstadoPaciente estado){
        this.estado = estado; 
    }
    public ListaEnlazada<String> getCaracteristicas() { 
        return caracteristicas; 
    }

   public void agregarCaracteristica(String caracteristicaNueva) {
        caracteristicas.agregar(caracteristicaNueva);
    }

    public boolean eliminarCaracteristica(String caracteristica) {
        return caracteristicas.remover(caracteristica);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre).append(" (ID: ").append(id).append(") - ");
        sb.append(urgencia == null ? "sin clasificar" : urgencia.name());
        sb.append(" - Caracteristicas: ");
        sb.append(caracteristicas.esVacio() ? "ninguna" : caracteristicas.toString());
    return sb.toString();
    }

    /**
     * Orden natural por id, consistente con {@link #equals(Object)}:
     * {@code compareTo(otro) == 0} si y sólo si {@code equals(otro)}.
     */
    @Override
    public int compareTo(Paciente otro) {
        return id.compareTo(otro.id);
    }

    /**
     * Criterio de búsqueda por documento, para usar con las estructuras de búsqueda.
     *
     * <p>El árbol baja comparando el criterio contra el dato de cada nodo, así que el
     * criterio <b>tiene que ordenar igual que el árbol</b>. Si no, la búsqueda se va
     * por la rama equivocada y no encuentra un paciente que sí está. Por eso conviene
     * pedirlo siempre por acá y no armarlo a mano en cada llamador.</p>
     *
     * {@snippet :
     * Paciente encontrado = pacientesRegistrados.buscar(Paciente.porId("A1"));
     *}
     */
    public static Comparable<Paciente> porId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Debe haber un id");
        }
        return otro -> id.compareTo(otro.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Paciente)) return false;
        Paciente otro = (Paciente) obj;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}