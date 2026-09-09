package ucu.edu.aed.clases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ucu.edu.aed.implementaciones.ListaEnlazada;

/**
 * Un hecho ocurrido durante la atención de un paciente: la consulta inicial, un
 * estudio, una interconsulta, un procedimiento o una complicación.
 *
 * <p>El evento <b>no sabe qué lo originó ni qué derivó de él</b>. Esa relación es la
 * jerarquía, y vive en el árbol n-ario del {@link Episodio}. Un evento solo guarda lo
 * que le pasó a él: sus insumos, sus tiempos y sus diagnósticos.</p>
 *
 * <p>Por eso {@link #cerrar(LocalDateTime)} no verifica la regla de cierre: el evento
 * no puede saber si sus descendientes están cerrados porque no los conoce. Esa regla
 * la hace cumplir el {@code Episodio}, que sí conoce el árbol.</p>
 *
 * <h2>Dos criterios de comparación</h2>
 * <p>Este tipo se usa en dos estructuras distintas y cada una necesita algo diferente:</p>
 * <ul>
 *   <li>En el <b>AVL global de eventos</b> hace falta un <i>orden</i>, y es por fecha.
 *       Como dos eventos pueden ocurrir en el mismo instante, la clave es compuesta
 *       (fecha, idEvento): sin el desempate el árbol tomaría dos eventos distintos por
 *       el mismo y perdería uno. Eso es {@link #compareTo(EventoClinico)}.</li>
 *   <li>En el <b>árbol n-ario del episodio</b> no hay orden: el {@code Comparable} se
 *       usa sólo como criterio de igualdad. Ahí alcanza con el id, y para eso está
 *       {@link #porId(String)}.</li>
 * </ul>
 */
public class EventoClinico implements Comparable<EventoClinico> {

    private final String idEvento;
    private final String idPaciente;
    private final TipoEvento tipo;
    private final String descripcion;
    private final LocalDateTime fecha;
    private final ListaEnlazada<Insumo> insumos;
    private final ListaEnlazada<CodigoDiagnostico> codigosDiagnostico;
    private EstadoEvento estado;
    private LocalDateTime fechaCierre;

    public EventoClinico(String idEvento, String idPaciente, TipoEvento tipo,
                         String descripcion, LocalDateTime fecha) {
        if (idEvento == null || idEvento.isBlank()) {
            throw new IllegalArgumentException("El evento debe tener id");
        }
        if (idPaciente == null || idPaciente.isBlank()) {
            throw new IllegalArgumentException("El evento debe pertenecer a un paciente");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El evento debe tener tipo");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("El evento debe tener fecha");
        }
        this.idEvento = idEvento;
        this.idPaciente = idPaciente;
        this.tipo = tipo;
        this.descripcion = descripcion == null ? "" : descripcion;
        this.fecha = fecha;
        this.insumos = new ListaEnlazada<>();
        this.codigosDiagnostico = new ListaEnlazada<>();
        this.estado = EstadoEvento.ABIERTO;
        this.fechaCierre = null;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public boolean estaAbierto() {
        return estado == EstadoEvento.ABIERTO;
    }

    // ---------- insumos y costos ----------

    public ListaEnlazada<Insumo> getInsumos() {
        return insumos;
    }

    public void agregarInsumo(Insumo insumo) {
        if (insumo == null) {
            throw new IllegalArgumentException("Debe haber un insumo");
        }
        insumos.agregar(insumo);
    }

    /**
     * Costo de los insumos consumidos <b>en este evento</b>, sin mirar el resto del
     * árbol.
     *
     * <p>El costo de un subárbol completo lo calcula el {@code Episodio} sumando este
     * valor sobre sus nodos. Deliberadamente no se guarda ningún total acumulado en el
     * evento: sería el mismo dato en dos lugares y se podría desincronizar.</p>
     */
    public double costoInsumos() {
        double total = 0;
        for (int i = 0; i < insumos.tamaño(); i++) {
            total = total + insumos.obtener(i).costoTotal();
        }
        return total;
    }

    // ---------- diagnósticos ----------

    public ListaEnlazada<CodigoDiagnostico> getCodigosDiagnostico() {
        return codigosDiagnostico;
    }

    public void agregarCodigoDiagnostico(CodigoDiagnostico codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Debe haber un codigo de diagnostico");
        }
        if (!codigosDiagnostico.contiene(codigo)) {
            codigosDiagnostico.agregar(codigo);
        }
    }

    // ---------- cierre y tiempos ----------

    /**
     * Marca el evento como cerrado.
     *
     * <p>No valida la regla de cierre (que todo lo derivado esté cerrado) porque el
     * evento no conoce a sus descendientes. Usar {@code Episodio.cerrarEvento}, que sí
     * la verifica.</p>
     *
     * @return {@code false} si el evento ya estaba cerrado
     */
    public boolean cerrar(LocalDateTime momento) {
        if (momento == null) {
            throw new IllegalArgumentException("Debe haber una fecha de cierre");
        }
        if (momento.isBefore(fecha)) {
            throw new IllegalArgumentException("El cierre no puede ser anterior a la apertura");
        }
        if (estado == EstadoEvento.CERRADO) {
            return false;
        }
        this.estado = EstadoEvento.CERRADO;
        this.fechaCierre = momento;
        return true;
    }

    /**
     * Minutos que duró el evento, o {@code -1} si todavía está abierto.
     *
     * <p>El tiempo se registra acá, donde efectivamente se consume, igual que los
     * insumos. La duración de un subárbol la agrega el {@code Episodio}.</p>
     */
    public long duracionMinutos() {
        if (fechaCierre == null) {
            return -1;
        }
        return Duration.between(fecha, fechaCierre).toMinutes();
    }

    // ---------- comparación ----------

    /**
     * Orden por fecha, desempatando por id.
     *
     * <p>El desempate no es cosmético: es lo que permite que dos eventos simultáneos
     * convivan en el AVL de eventos. Si la clave fuera sólo la fecha, el árbol
     * consideraría iguales a dos eventos distintos y descartaría uno.</p>
     */
    @Override
    public int compareTo(EventoClinico otro) {
        int porFecha = fecha.compareTo(otro.fecha);
        if (porFecha != 0) {
            return porFecha;
        }
        return idEvento.compareTo(otro.idEvento);
    }

    /**
     * Criterio de igualdad por id, para buscar dentro del árbol n-ario del episodio.
     *
     * <p>Sirve sólo para igualdad, no para ordenar; es exactamente lo que el árbol
     * n-ario necesita, ya que ahí la posición la da la jerarquía y no una clave.</p>
     */
    public static Comparable<EventoClinico> porId(String idEvento) {
        if (idEvento == null) {
            throw new IllegalArgumentException("Debe haber un id de evento");
        }
        return otro -> idEvento.compareTo(otro.getIdEvento());
    }

    /**
     * Cota por fecha, para usar como extremo en una consulta por rango sobre el AVL de
     * eventos. Ignora el desempate por id, así los extremos quedan inclusivos.
     */
    public static Comparable<EventoClinico> porFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("Debe haber una fecha");
        }
        return otro -> fecha.compareTo(otro.getFecha());
    }

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public String toString() {
        return "[" + fecha.format(FORMATO_FECHA) + "] " + idEvento + " " + tipo
                + " (" + estado + ") " + descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EventoClinico)) return false;
        return idEvento.equals(((EventoClinico) obj).idEvento);
    }

    @Override
    public int hashCode() {
        return idEvento.hashCode();
    }
}
