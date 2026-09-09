package ucu.edu.aed.clases;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import ucu.edu.aed.implementaciones.ArbolGenerico;
import ucu.edu.aed.implementaciones.ListaEnlazada;
import ucu.edu.aed.tda.TDALista;

public class Episodio implements Comparable<Episodio> {

    private final String idEpisodio;
    private final Paciente paciente;
    private final ArbolGenerico<EventoClinico> arbol;
    private final LocalDateTime fechaApertura;
    private EstadoEpisodio estado;
    private LocalDateTime fechaCierre;

    public Episodio(String idEpisodio, Paciente paciente, EventoClinico consultaInicial) {
        if (idEpisodio == null || idEpisodio.isBlank()) {
            throw new IllegalArgumentException("El episodio debe tener id");
        }
        if (paciente == null) {
            throw new IllegalArgumentException("El episodio debe tener un paciente");
        }
        if (consultaInicial == null) {
            throw new IllegalArgumentException("El episodio debe abrirse con una consulta inicial");
        }
        if (!consultaInicial.getIdPaciente().equals(paciente.getId())) {
            throw new IllegalArgumentException(
                    "La consulta inicial es de otro paciente: " + consultaInicial.getIdPaciente());
        }
        this.idEpisodio = idEpisodio;
        this.paciente = paciente;
        this.arbol = new ArbolGenerico<>();
        this.arbol.insertarRaiz(consultaInicial);
        this.fechaApertura = consultaInicial.getFecha();
        this.estado = EstadoEpisodio.ABIERTO;
        this.fechaCierre = null;
    }

    public String getIdEpisodio() {
        return idEpisodio;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public EstadoEpisodio getEstado() {
        return estado;
    }

    public boolean estaAbierto() {
        return estado == EstadoEpisodio.ABIERTO;
    }

    public EventoClinico getConsultaInicial() {
        return arbol.obtenerRaiz().getDato();
    }

    public int cantidadEventos() {
        return arbol.cantidadNodos();
    }

    public int profundidad() {
        return arbol.altura();
    }

    public boolean registrarEvento(String idEventoPadre, EventoClinico evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Debe haber un evento");
        }
        if (estado == EstadoEpisodio.CERRADO) {
            throw new IllegalStateException(
                    "El episodio " + idEpisodio + " ya esta cerrado");
        }
        if (!evento.getIdPaciente().equals(paciente.getId())) {
            throw new IllegalArgumentException(
                    "El evento es de otro paciente: " + evento.getIdPaciente());
        }
        EventoClinico padre = buscarEvento(idEventoPadre);
        if (padre == null) {
            throw new NoSuchElementException(
                    "No existe el evento origen " + idEventoPadre + " en este episodio");
        }
        if (!padre.estaAbierto()) {
            throw new IllegalStateException(
                    "El evento origen " + idEventoPadre + " ya esta cerrado: "
                            + "no se le pueden colgar derivaciones nuevas");
        }
        return arbol.insertar(EventoClinico.porId(idEventoPadre), evento);
    }

    public boolean registrarEvento(String idEventoPadre, String idEvento, TipoEvento tipo,
                                   String descripcion, LocalDateTime momento) {
        return registrarEvento(idEventoPadre,
                new EventoClinico(idEvento, paciente.getId(), tipo, descripcion, momento));
    }

    public void agregarInsumo(String idEvento, Insumo insumo) {
        EventoClinico evento = buscarEvento(idEvento);
        if (evento == null) {
            throw new NoSuchElementException("No existe el evento " + idEvento);
        }
        if (!evento.estaAbierto()) {
            throw new IllegalStateException(
                    "El evento " + idEvento + " ya esta cerrado: no admite mas insumos");
        }
        evento.agregarInsumo(insumo);
    }

    public EventoClinico buscarEvento(String idEvento) {
        if (idEvento == null) {
            return null;
        }
        return arbol.buscar(EventoClinico.porId(idEvento));
    }

    public TDALista<EventoClinico> queImpideCerrar(String idEvento) {
        TDALista<EventoClinico> bloqueantes = new ListaEnlazada<>();
        if (buscarEvento(idEvento) == null) {
            throw new NoSuchElementException("No existe el evento " + idEvento);
        }
        arbol.postOrderDesde(EventoClinico.porId(idEvento), evento -> {

            if (evento.estaAbierto() && !evento.getIdEvento().equals(idEvento)) {
                bloqueantes.agregar(evento);
            }
        });
        return bloqueantes;
    }

    public boolean puedeCerrarse(String idEvento) {
        return queImpideCerrar(idEvento).esVacio();
    }

    public boolean cerrarEvento(String idEvento, LocalDateTime momento) {
        EventoClinico evento = buscarEvento(idEvento);
        if (evento == null) {
            throw new NoSuchElementException("No existe el evento " + idEvento);
        }
        TDALista<EventoClinico> bloqueantes = queImpideCerrar(idEvento);
        if (!bloqueantes.esVacio()) {
            StringBuilder detalle = new StringBuilder();
            for (int i = 0; i < bloqueantes.tamaño(); i++) {
                if (i > 0) {
                    detalle.append(", ");
                }
                detalle.append(bloqueantes.obtener(i).getIdEvento());
            }
            throw new IllegalStateException("No se puede cerrar " + idEvento
                    + ": siguen abiertos " + detalle);
        }
        return evento.cerrar(momento);
    }

    public TDALista<EventoClinico> queImpideCerrarEpisodio() {
        return queImpideCerrar(getConsultaInicial().getIdEvento());
    }

    public boolean cerrarEpisodio(LocalDateTime momento) {
        if (estado == EstadoEpisodio.CERRADO) {
            return false;
        }
        cerrarEvento(getConsultaInicial().getIdEvento(), momento);
        this.estado = EstadoEpisodio.CERRADO;
        this.fechaCierre = momento;
        return true;
    }

    public double costoDe(String idEvento) {
        if (buscarEvento(idEvento) == null) {
            throw new NoSuchElementException("No existe el evento " + idEvento);
        }
        double[] total = {0};
        arbol.postOrderDesde(EventoClinico.porId(idEvento),
                evento -> total[0] = total[0] + evento.costoInsumos());
        return total[0];
    }

    public double costoTotal() {
        return costoDe(getConsultaInicial().getIdEvento());
    }

    public long duracionAcumuladaMinutos(String idEvento) {
        if (buscarEvento(idEvento) == null) {
            throw new NoSuchElementException("No existe el evento " + idEvento);
        }
        long[] total = {0};
        arbol.postOrderDesde(EventoClinico.porId(idEvento), evento -> {
            if (!evento.estaAbierto()) {
                total[0] = total[0] + evento.duracionMinutos();
            }
        });
        return total[0];
    }

    public TDALista<EventoClinico> derivacionesDe(String idEvento) {
        return arbol.hijosDe(EventoClinico.porId(idEvento));
    }

    public TDALista<EventoClinico> todoLoQueDerivoDe(String idEvento) {
        return arbol.descendientesDe(EventoClinico.porId(idEvento));
    }

    public TDALista<EventoClinico> eventosAbiertos() {
        TDALista<EventoClinico> abiertos = new ListaEnlazada<>();
        arbol.preOrder(evento -> {
            if (evento.estaAbierto()) {
                abiertos.agregar(evento);
            }
        });
        return abiertos;
    }

    public TDALista<String> codigos() {
        TDALista<String> resultado = new ListaEnlazada<>();
        arbol.preOrder(evento -> {
            ListaEnlazada<String> delEvento = evento.getCodigosDiagnostico();
            for (int i = 0; i < delEvento.tamaño(); i++) {
                String codigo = delEvento.obtener(i);
                if (!resultado.contiene(codigo)) {
                    resultado.agregar(codigo);
                }
            }
        });
        return resultado;
    }

    public String porNivelesString() {
        return arbol.porNivelesString();
    }

    @Override
    public int compareTo(Episodio otro) {
        int porFecha = fechaApertura.compareTo(otro.fechaApertura);
        if (porFecha != 0) {
            return porFecha;
        }
        return idEpisodio.compareTo(otro.idEpisodio);
    }

    public static Comparable<Episodio> porId(String idEpisodio) {
        if (idEpisodio == null) {
            throw new IllegalArgumentException("Debe haber un id de episodio");
        }
        return otro -> idEpisodio.compareTo(otro.getIdEpisodio());
    }

    public static Comparable<Episodio> porFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("Debe haber una fecha");
        }
        return otro -> fecha.compareTo(otro.getFechaApertura());
    }

    @Override
    public String toString() {
        return "Episodio " + idEpisodio + " de " + paciente.getNombre()
                + " (" + estado + ") - " + cantidadEventos() + " eventos";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Episodio)) return false;
        return idEpisodio.equals(((Episodio) obj).idEpisodio);
    }

    @Override
    public int hashCode() {
        return idEpisodio.hashCode();
    }
}
