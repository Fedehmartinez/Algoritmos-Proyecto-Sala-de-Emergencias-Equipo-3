package ucu.edu.aed.clases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ucu.edu.aed.implementaciones.ListaEnlazada;

public class EventoClinico implements Comparable<EventoClinico> {

    private final String idEvento;
    private final String idPaciente;
    private final TipoEvento tipo;
    private final String descripcion;
    private final LocalDateTime fecha;
    private final ListaEnlazada<Insumo> insumos;
    private final ListaEnlazada<String> codigosDiagnostico;
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

    public ListaEnlazada<Insumo> getInsumos() {
        return insumos;
    }

    public void agregarInsumo(Insumo insumo) {
        if (insumo == null) {
            throw new IllegalArgumentException("Debe haber un insumo");
        }
        insumos.agregar(insumo);
    }

    public double costoInsumos() {
        double total = 0;
        for (int i = 0; i < insumos.tamaño(); i++) {
            total = total + insumos.obtener(i).costoTotal();
        }
        return total;
    }

    public ListaEnlazada<String> getCodigosDiagnostico() {
        return codigosDiagnostico;
    }

    public void agregarCodigoDiagnostico(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Debe haber un codigo de diagnostico");
        }
        if (!codigosDiagnostico.contiene(codigo)) {
            codigosDiagnostico.agregar(codigo);
        }
    }

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

    public long duracionMinutos() {
        if (fechaCierre == null) {
            return -1;
        }
        return Duration.between(fecha, fechaCierre).toMinutes();
    }

    @Override
    public int compareTo(EventoClinico otro) {
        int porFecha = fecha.compareTo(otro.fecha);
        if (porFecha != 0) {
            return porFecha;
        }
        return idEvento.compareTo(otro.idEvento);
    }

    public static Comparable<EventoClinico> porId(String idEvento) {
        if (idEvento == null) {
            throw new IllegalArgumentException("Debe haber un id de evento");
        }
        return otro -> idEvento.compareTo(otro.getIdEvento());
    }

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
