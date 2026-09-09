package ucu.edu.aed.clases;

import java.time.LocalDateTime;

import ucu.edu.aed.implementaciones.AVLImpl;
import ucu.edu.aed.implementaciones.ListaEnlazada;
import ucu.edu.aed.tda.TDALista;

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
    private Episodio episodioActual;
    private final AVLImpl<Episodio> episodios;

    public Paciente(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.caracteristicas = new ListaEnlazada<>();
        this.urgencia = null; // Se asigna la urgencia más adelante, cuando se agregue a la cola de prioridad
        this.estado = EstadoPaciente.REGISTRADO;
        this.episodioActual = null;
        this.episodios = new AVLImpl<>();
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

    // ---------- episodios ----------

    /**
     * El episodio que el paciente tiene abierto ahora, o {@code null} si no está siendo
     * atendido.
     *
     * <p>Es una referencia al mismo objeto que está en {@link #getEpisodios()}, no una
     * copia: sirve para llegar en O(1) al episodio en curso sin buscarlo por fecha.</p>
     */
    public Episodio getEpisodioActual() {
        return episodioActual;
    }

    public boolean tieneEpisodioAbierto() {
        return episodioActual != null;
    }

    /**
     * Todos los episodios del paciente, abiertos y cerrados, ordenados por fecha de
     * apertura.
     *
     * <p>Es un AVL y no una lista porque la consulta que interesa es por período: con el
     * árbol, pedir las atenciones de un rango de fechas cuesta O(log m + k) en vez de
     * recorrer los m episodios del paciente.</p>
     */
    public AVLImpl<Episodio> getEpisodios() {
        return episodios;
    }

    public int cantidadEpisodios() {
        return episodios.cantidadNodos();
    }

    /**
     * Abre un episodio y lo deja como el actual.
     *
     * @throws IllegalStateException si el paciente ya tiene uno abierto: un paciente no
     *         puede estar en dos atenciones en curso al mismo tiempo
     */
    public void abrirEpisodio(Episodio episodio) {
        if (episodio == null) {
            throw new IllegalArgumentException("Debe haber un episodio");
        }
        if (!episodio.getPaciente().getId().equals(id)) {
            throw new IllegalArgumentException(
                    "El episodio es de otro paciente: " + episodio.getPaciente().getId());
        }
        if (episodioActual != null) {
            throw new IllegalStateException(
                    "El paciente " + id + " ya tiene el episodio "
                            + episodioActual.getIdEpisodio() + " abierto");
        }
        episodios.insertar(episodio);
        this.episodioActual = episodio;
    }

    /**
     * Cierra el episodio en curso y lo devuelve ya cerrado.
     *
     * <p>El episodio no se saca del árbol: sigue siendo parte de la historia clínica del
     * paciente, y ahí queda consultable por fecha. Lo único que se libera es la
     * referencia al actual.</p>
     *
     * @throws IllegalStateException si no hay episodio abierto, o si algo impide
     *         cerrarlo (el mensaje dice qué)
     */
    public Episodio cerrarEpisodioActual(LocalDateTime momento) {
        if (episodioActual == null) {
            throw new IllegalStateException(
                    "El paciente " + id + " no tiene ningun episodio abierto");
        }
        episodioActual.cerrarEpisodio(momento);
        Episodio cerrado = episodioActual;
        this.episodioActual = null;
        return cerrado;
    }

    /**
     * Los episodios del paciente abiertos dentro del período, ambos extremos incluidos.
     *
     * <p>Se apoya en la poda del AVL: no recorre los episodios que quedan fuera del
     * rango.</p>
     */
    public TDALista<Episodio> episodiosEnRango(LocalDateTime desde, LocalDateTime hasta) {
        return episodios.enRango(
                desde == null ? null : Episodio.porFecha(desde),
                hasta == null ? null : Episodio.porFecha(hasta));
    }

    /**
     * Busca un episodio del paciente por su id.
     *
     * <p>Es O(m) sobre los episodios del paciente, y no O(log m), porque este árbol está
     * ordenado <b>por fecha de apertura</b>: contra una clave que no es la del orden no
     * hay nada que podar y hay que mirarlos todos. No se puede usar
     * {@code episodios.buscar(Episodio.porId(...))}, porque la búsqueda del árbol baja
     * comparando y con un criterio ajeno al orden se iría por la rama equivocada,
     * devolviendo {@code null} para un episodio que sí está.</p>
     *
     * <p>El acceso barato de esta estructura es por fecha ({@link #episodiosEnRango}) y
     * al episodio en curso ({@link #getEpisodioActual()}). Si hiciera falta buscar por
     * id en O(log m) habría que mantener un segundo índice ordenado por id.</p>
     */
    public Episodio buscarEpisodio(String idEpisodio) {
        if (idEpisodio == null) {
            return null;
        }
        Episodio[] encontrado = {null};
        episodios.inOrder(episodio -> {
            if (episodio.getIdEpisodio().equals(idEpisodio)) {
                encontrado[0] = episodio;
            }
        });
        return encontrado[0];
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