package ucu.edu.aed.clases;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.NoSuchElementException;

import ucu.edu.aed.implementaciones.AVLImpl;
import ucu.edu.aed.implementaciones.Heap;
import ucu.edu.aed.tda.TDALista;

public class SalaEmergencia {

    public static final Comparator<Paciente> POR_URGENCIA =
            Comparator.comparingInt(p -> p.getUrgencia().getTiempoMaximoEsperaMinutos());

    private final AVLImpl<Paciente> pacientesRegistrados;
    private final Heap<Paciente> esperaAtencion;
    private final AVLImpl<Episodio> episodiosPorFecha;
    private final AVLImpl<EventoClinico> eventosPorFecha;
    private final CatalogoDiagnosticos catalogoDiagnosticos;

    public SalaEmergencia() {
        this.pacientesRegistrados = new AVLImpl<>();
        this.esperaAtencion = new Heap<>(POR_URGENCIA);
        this.episodiosPorFecha = new AVLImpl<>();
        this.eventosPorFecha = new AVLImpl<>();
        this.catalogoDiagnosticos = new CatalogoDiagnosticos();
    }

    public Paciente registrarPaciente(String nombre, String id) {
        if (nombre == null || id == null) {
            throw new IllegalArgumentException("Debe haber nombre e id");
        }
        Paciente paciente = new Paciente(id, nombre);
        if (!pacientesRegistrados.insertar(paciente)) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con id " + id);
        }
        return paciente;
    }

    public Paciente buscarPaciente(String idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("Debe haber un paciente");
        }
        return pacientesRegistrados.buscar(Paciente.porId(idPaciente));
    }

    public void eliminarPaciente(String idPaciente) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) {
            throw new NoSuchElementException("Paciente no encontrado: " + idPaciente);
        }
        esperaAtencion.remover(paciente);
        pacientesRegistrados.eliminar(Paciente.porId(idPaciente));
    }

    public String listarPacientes() {
        if (pacientesRegistrados.esVacio()) {
            return "No hay pacientes registrados";
        }
        return "Pacientes registrados:\n" + pacientesRegistrados.inOrderString();
    }

    public void agregarPacienteACola(Paciente pacienteNuevo, NivelUrgencia urgencia) {
        if (pacienteNuevo == null) {
            throw new IllegalArgumentException("Debe haber un paciente");
        }
        if (urgencia == null) {
            throw new IllegalArgumentException("Debe haber un nivel de urgencia");
        }

        Paciente registrado = buscarPaciente(pacienteNuevo.getId());
        if (registrado == null) {
            throw new IllegalArgumentException(
                    "El paciente " + pacienteNuevo.getId() + " no esta registrado");
        }

        registrado.setUrgencia(urgencia);
        registrado.setEstadoPaciente(EstadoPaciente.EN_ESPERA);
        esperaAtencion.insertar(registrado);
    }

    public void cambiarPrioridad(Paciente paciente, NivelUrgencia nuevaUrgencia) {
        if (paciente == null) {
            throw new IllegalArgumentException("Debe haber un paciente");
        }
        if (nuevaUrgencia == null) {
            throw new IllegalArgumentException("Debe haber un nivel de urgencia");
        }

        Paciente registrado = buscarPaciente(paciente.getId());
        if (registrado == null) {
            throw new IllegalArgumentException(
                    "El paciente " + paciente.getId() + " no esta registrado");
        }
        if (!esperaAtencion.remover(registrado)) {
            throw new IllegalStateException(
                    "El paciente " + registrado.getId() + " no esta en la cola de espera");
        }

        registrado.setUrgencia(nuevaUrgencia);
        esperaAtencion.insertar(registrado);
    }

    public int cantidadEnEspera() {
        return esperaAtencion.cantidad();
    }

    public Paciente proximoAAtender() {
        return esperaAtencion.minimo();
    }

    public Episodio abrirEpisodio(String idPaciente, String idEpisodio,
                                  String motivoConsulta, LocalDateTime momento) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) {
            throw new NoSuchElementException("Paciente no encontrado: " + idPaciente);
        }
        EventoClinico consultaInicial = new EventoClinico(
                idEpisodio + "-E0", idPaciente, TipoEvento.CONSULTA_INICIAL,
                motivoConsulta, momento);
        Episodio episodio = new Episodio(idEpisodio, paciente, consultaInicial);

        paciente.abrirEpisodio(episodio);
        episodiosPorFecha.insertar(episodio);
        eventosPorFecha.insertar(consultaInicial);
        if (paciente.getEstadoPaciente() == EstadoPaciente.EN_ESPERA) {
            esperaAtencion.remover(paciente);
        }
        paciente.setEstadoPaciente(EstadoPaciente.EN_ATENCION);
        return episodio;
    }

    public Episodio atenderSiguiente(String idEpisodio, String motivoConsulta,
                                     LocalDateTime momento) {
        Paciente siguiente = esperaAtencion.eliminar();
        if (siguiente == null) {
            throw new NoSuchElementException("No hay pacientes esperando");
        }
        return abrirEpisodio(siguiente.getId(), idEpisodio, motivoConsulta, momento);
    }

    public boolean registrarEvento(String idPaciente, String idEventoPadre,
                                   EventoClinico evento) {
        boolean agregado = episodioEnCursoDe(idPaciente).registrarEvento(idEventoPadre, evento);
        if (agregado) {
            eventosPorFecha.insertar(evento);
        }
        return agregado;
    }

    public void agregarInsumo(String idPaciente, String idEvento, Insumo insumo) {
        episodioEnCursoDe(idPaciente).agregarInsumo(idEvento, insumo);
    }

    public boolean cerrarEvento(String idPaciente, String idEvento, LocalDateTime momento) {
        return episodioEnCursoDe(idPaciente).cerrarEvento(idEvento, momento);
    }

    public TDALista<EventoClinico> queImpideCerrarEvento(String idPaciente, String idEvento) {
        return episodioEnCursoDe(idPaciente).queImpideCerrar(idEvento);
    }

    public double costoDe(String idPaciente, String idEvento) {
        return episodioEnCursoDe(idPaciente).costoDe(idEvento);
    }

    public double costoDelEpisodioEnCurso(String idPaciente) {
        return episodioEnCursoDe(idPaciente).costoTotal();
    }

    public Episodio cerrarEpisodio(String idPaciente, LocalDateTime momento) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) {
            throw new NoSuchElementException("Paciente no encontrado: " + idPaciente);
        }
        Episodio cerrado = paciente.cerrarEpisodioActual(momento);
        paciente.setEstadoPaciente(EstadoPaciente.ATENDIDO);
        return cerrado;
    }

    public Episodio episodioEnCursoDe(String idPaciente) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) {
            throw new NoSuchElementException("Paciente no encontrado: " + idPaciente);
        }
        if (!paciente.tieneEpisodioAbierto()) {
            throw new IllegalStateException(
                    "El paciente " + idPaciente + " no tiene ningun episodio abierto");
        }
        return paciente.getEpisodioActual();
    }

    public TDALista<EventoClinico> queImpideCerrarEpisodioDe(String idPaciente) {
        return episodioEnCursoDe(idPaciente).queImpideCerrarEpisodio();
    }

    public TDALista<Episodio> episodiosEnRango(LocalDateTime desde, LocalDateTime hasta) {
        return episodiosPorFecha.enRango(
                desde == null ? null : Episodio.porFecha(desde),
                hasta == null ? null : Episodio.porFecha(hasta));
    }

    public TDALista<Episodio> episodiosEnRango(String idPaciente,
                                               LocalDateTime desde,
                                               LocalDateTime hasta) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) {
            throw new NoSuchElementException("Paciente no encontrado: " + idPaciente);
        }
        return paciente.episodiosEnRango(desde, hasta);
    }

    public TDALista<EventoClinico> eventosEnRango(LocalDateTime desde, LocalDateTime hasta) {
        return eventosPorFecha.enRango(
                desde == null ? null : EventoClinico.porFecha(desde),
                hasta == null ? null : EventoClinico.porFecha(hasta));
    }

    public int cantidadEpisodios() {
        return episodiosPorFecha.cantidadNodos();
    }

    public int cantidadEventos() {
        return eventosPorFecha.cantidadNodos();
    }

    public String listarEpisodios() {
        if (episodiosPorFecha.esVacio()) {
            return "No hay episodios registrados";
        }
        return "Episodios:\n" + episodiosPorFecha.inOrderString();
    }

    public boolean agregarCapituloDiagnostico(String codigo, String nombre) {
        return catalogoDiagnosticos.agregarCapitulo(codigo, nombre);
    }

    public boolean agregarGrupoDiagnostico(String codigoCapitulo, String codigo, String nombre) {
        return catalogoDiagnosticos.agregarGrupo(codigoCapitulo, codigo, nombre);
    }

    public boolean agregarCodigoAlCatalogo(String codigoGrupo, String codigo, String nombre) {
        return catalogoDiagnosticos.agregarCodigo(codigoGrupo, codigo, nombre);
    }

    public NodoCatalogo buscarEnCatalogo(String codigo) {
        return catalogoDiagnosticos.buscarNodo(codigo);
    }

    public TDALista<NodoCatalogo> codigosDelCatalogo(String codigo) {
        return catalogoDiagnosticos.codigosBajo(codigo);
    }

    public void diagnosticar(String idPaciente, String idEvento, String codigoDiagnostico) {
        NodoCatalogo nodo = catalogoDiagnosticos.buscarNodo(codigoDiagnostico);
        if (nodo == null) {
            throw new NoSuchElementException(
                    "No existe el codigo " + codigoDiagnostico + " en el catalogo");
        }
        if (nodo.getNivel() != NivelCatalogo.CODIGO) {
            throw new IllegalArgumentException(
                    codigoDiagnostico + " no es un codigo diagnosticable (es " + nodo.getNivel() + ")");
        }
        EventoClinico evento = episodioEnCursoDe(idPaciente).buscarEvento(idEvento);
        if (evento == null) {
            throw new NoSuchElementException("No existe el evento " + idEvento);
        }
        evento.agregarCodigoDiagnostico(nodo.getCodigo());
    }

    @Override
    public String toString() {
        return "Sala de emergencias"
                + " | Registrados: " + pacientesRegistrados.cantidadNodos()
                + " | En espera: " + esperaAtencion.cantidad()
                + " | Episodios: " + episodiosPorFecha.cantidadNodos()
                + " | Eventos: " + eventosPorFecha.cantidadNodos()
                + " | Codigos en catalogo: " + catalogoDiagnosticos.cantidadNodos();
    }
}
