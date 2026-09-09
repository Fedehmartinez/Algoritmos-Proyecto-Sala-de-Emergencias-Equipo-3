package ucu.edu.aed.clases;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.NoSuchElementException;

import ucu.edu.aed.implementaciones.AVLImpl;
import ucu.edu.aed.implementaciones.Heap;
import ucu.edu.aed.tda.TDALista;

/**
 * La sala de emergencias.
 *
 * <h2>Estructuras</h2>
 * <ul>
 *   <li><b>pacientesRegistrados</b>: AVL ordenado por documento. Registrar, buscar y
 *       eliminar cuestan O(log n). En el primer hito era una lista ordenada sobre
 *       arreglo: la búsqueda ya era O(log n) por binaria, pero insertar y borrar
 *       costaban O(n) por el desplazamiento.</li>
 *   <li><b>esperaAtencion</b>: heap binario con {@link #POR_URGENCIA}. El más urgente
 *       sale en O(log n) y no hace falta mantener la cola entera ordenada.</li>
 *   <li><b>episodiosPorFecha</b>: AVL de todos los episodios de la sala, ordenado por
 *       fecha de apertura. Es un índice secundario: guarda referencias a los mismos
 *       objetos que viven en cada {@link Paciente}, no copias.</li>
 * </ul>
 *
 * <p>La atención se modela con {@link Episodio}: abrir un episodio es lo que en el
 * primer hito era "entrar al consultorio", pero conservando qué originó qué. No hay
 * tope de episodios abiertos simultáneos.</p>
 */
public class SalaEmergencia {

    // Ordena por tiempo máximo de espera tolerable: menos tiempo, más prioridad.
    public static final Comparator<Paciente> POR_URGENCIA =
            Comparator.comparingInt(p -> p.getUrgencia().getTiempoMaximoEsperaMinutos());

    private final AVLImpl<Paciente> pacientesRegistrados;
    private final Heap<Paciente> esperaAtencion;
    private final AVLImpl<Episodio> episodiosPorFecha;

    public SalaEmergencia() {
        this.pacientesRegistrados = new AVLImpl<>();
        this.esperaAtencion = new Heap<>(POR_URGENCIA);
        this.episodiosPorFecha = new AVLImpl<>();
    }

    // ---------- registro de pacientes ----------

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

    // ---------- gestión de la espera ----------

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

    /**
     * Cambia la urgencia de un paciente que ya está esperando.
     *
     * <p>El heap no ofrece una operación de "actualizar prioridad" directa, así que se
     * hace en tres pasos: sacarlo (cambiarle la urgencia estando adentro rompería el
     * invariante de orden), reasignarle la urgencia nueva, y volver a insertarlo para
     * que el heap lo reacomode.</p>
     */
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

    /**
     * El próximo paciente a atender según la urgencia, sin sacarlo de la cola.
     *
     * @return {@code null} si no hay nadie esperando
     */
    public Paciente proximoAAtender() {
        return esperaAtencion.minimo();
    }

    // ---------- episodios de atención ----------

    /**
     * Abre un episodio para un paciente registrado: crea la consulta inicial, la deja
     * como raíz del árbol del episodio y lo indexa por fecha.
     *
     * <p>Si el paciente estaba esperando, sale de la cola.</p>
     *
     * @throws NoSuchElementException si el paciente no está registrado
     * @throws IllegalStateException si el paciente ya tiene un episodio abierto
     */
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
        esperaAtencion.remover(paciente);
        paciente.setEstadoPaciente(EstadoPaciente.EN_ATENCION);
        return episodio;
    }

    /**
     * Saca de la cola al paciente más urgente y le abre el episodio.
     *
     * <p>Es la operación que efectivamente usa el heap: la sala no elige a quién
     * atender, la prioridad lo decide.</p>
     *
     * @throws NoSuchElementException si no hay nadie esperando
     */
    public Episodio atenderSiguiente(String idEpisodio, String motivoConsulta,
                                     LocalDateTime momento) {
        Paciente siguiente = esperaAtencion.eliminar();
        if (siguiente == null) {
            throw new NoSuchElementException("No hay pacientes esperando");
        }
        return abrirEpisodio(siguiente.getId(), idEpisodio, motivoConsulta, momento);
    }

    /**
     * Registra un evento dentro del episodio en curso del paciente, colgándolo de
     * aquello que lo originó.
     */
    public boolean registrarEvento(String idPaciente, String idEventoPadre,
                                   EventoClinico evento) {
        return episodioEnCursoDe(idPaciente).registrarEvento(idEventoPadre, evento);
    }

    /**
     * Registra un evento armándolo acá, sin que el llamador tenga que construirlo.
     */
    public boolean registrarEvento(String idPaciente, String idEventoPadre, String idEvento,
                                   TipoEvento tipo, String descripcion, LocalDateTime momento) {
        return episodioEnCursoDe(idPaciente)
                .registrarEvento(idEventoPadre, idEvento, tipo, descripcion, momento);
    }

    /**
     * Registra un insumo en el evento donde se consumió, dentro del episodio en curso.
     */
    public void agregarInsumo(String idPaciente, String idEvento, Insumo insumo) {
        episodioEnCursoDe(idPaciente).agregarInsumo(idEvento, insumo);
    }

    /**
     * Cierra un evento del episodio en curso, si todo lo que derivó de él está cerrado.
     *
     * @throws IllegalStateException si quedan descendientes abiertos; el mensaje dice
     *         cuáles
     */
    public boolean cerrarEvento(String idPaciente, String idEvento, LocalDateTime momento) {
        return episodioEnCursoDe(idPaciente).cerrarEvento(idEvento, momento);
    }

    /**
     * Qué impide cerrar un evento puntual del episodio en curso.
     */
    public TDALista<EventoClinico> queImpideCerrarEvento(String idPaciente, String idEvento) {
        return episodioEnCursoDe(idPaciente).queImpideCerrar(idEvento);
    }

    /**
     * Costo del episodio en curso, o de cualquier parte de él.
     *
     * <p>Pasar la raíz del episodio da el total; pasar un evento intermedio da lo que
     * costó esa rama. Es el mismo recorrido cambiando desde dónde arranca.</p>
     */
    public double costoDe(String idPaciente, String idEvento) {
        return episodioEnCursoDe(idPaciente).costoDe(idEvento);
    }

    public double costoDelEpisodioEnCurso(String idPaciente) {
        return episodioEnCursoDe(idPaciente).costoTotal();
    }

    /**
     * Minutos acumulados del episodio en curso, o de cualquier parte de él. Los eventos
     * todavía abiertos no suman.
     */
    public long duracionDe(String idPaciente, String idEvento) {
        return episodioEnCursoDe(idPaciente).duracionAcumuladaMinutos(idEvento);
    }

    /**
     * Cierra el episodio en curso del paciente. El episodio queda en la historia del
     * paciente y en el índice por fecha, ya cerrado.
     *
     * @throws IllegalStateException si quedan eventos abiertos; el mensaje dice cuáles
     */
    public Episodio cerrarEpisodio(String idPaciente, LocalDateTime momento) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) {
            throw new NoSuchElementException("Paciente no encontrado: " + idPaciente);
        }
        Episodio cerrado = paciente.cerrarEpisodioActual(momento);
        paciente.setEstadoPaciente(EstadoPaciente.ATENDIDO);
        return cerrado;
    }

    /**
     * El episodio en curso del paciente.
     *
     * @throws IllegalStateException si no tiene ninguno abierto
     */
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

    /**
     * Qué impide cerrar el episodio en curso de un paciente, en este momento.
     */
    public TDALista<EventoClinico> queImpideCerrarEpisodioDe(String idPaciente) {
        return episodioEnCursoDe(idPaciente).queImpideCerrarEpisodio();
    }

    // ---------- consultas por fecha ----------

    /**
     * Episodios de <b>toda la sala</b> abiertos dentro del período, ambos extremos
     * incluidos.
     *
     * <p>O(log n + k) gracias a la poda del AVL. En el primer hito la consulta
     * equivalente obligaba a recorrer el historial completo, O(n), porque una pila no
     * tiene ningún orden aprovechable.</p>
     */
    public TDALista<Episodio> episodiosEnRango(LocalDateTime desde, LocalDateTime hasta) {
        return episodiosPorFecha.enRango(
                desde == null ? null : Episodio.porFecha(desde),
                hasta == null ? null : Episodio.porFecha(hasta));
    }

    /**
     * Atenciones de <b>un paciente</b> dentro del período.
     *
     * <p>Dos podas encadenadas: O(log n) para llegar al paciente por su documento, y
     * O(log m + k) para el rango dentro de sus episodios. Nunca se recorre la totalidad
     * de los registros.</p>
     */
    public TDALista<Episodio> episodiosDePacienteEnRango(String idPaciente,
                                                        LocalDateTime desde,
                                                        LocalDateTime hasta) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) {
            throw new NoSuchElementException("Paciente no encontrado: " + idPaciente);
        }
        return paciente.episodiosEnRango(desde, hasta);
    }

    public int cantidadEpisodios() {
        return episodiosPorFecha.cantidadNodos();
    }

    public String listarEpisodios() {
        if (episodiosPorFecha.esVacio()) {
            return "No hay episodios registrados";
        }
        return "Episodios:\n" + episodiosPorFecha.inOrderString();
    }

    @Override
    public String toString() {
        return "Sala de emergencias"
                + " | Registrados: " + pacientesRegistrados.cantidadNodos()
                + " | En espera: " + esperaAtencion.cantidad()
                + " | Episodios: " + episodiosPorFecha.cantidadNodos();
    }
}
