package ucu.edu.aed.clases;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import ucu.edu.aed.implementaciones.ArbolGenerico;
import ucu.edu.aed.implementaciones.ListaEnlazada;
import ucu.edu.aed.tda.TDALista;

/**
 * Un episodio de atención: todo lo que pasó desde que el paciente ingresó hasta que
 * se cerró la atención.
 *
 * <h2>Por qué un árbol n-ario</h2>
 * <p>El ingreso abre una consulta inicial, que deriva en estudios, que pueden motivar
 * interconsultas, que pueden terminar en procedimientos, que pueden traer
 * complicaciones que abren nuevas intervenciones. Ni la cantidad de derivaciones ni su
 * profundidad se conocen de antemano, y lo que importa registrar es <b>qué originó
 * qué</b>.</p>
 *
 * <p>Eso es exactamente un árbol general: cada evento es un nodo, y ser hijo de otro
 * nodo significa "surgió de él". Una lista no alcanza porque perdería la relación de
 * origen, y un árbol binario no alcanza porque un evento puede derivar en más de dos
 * cosas. La raíz es siempre la consulta inicial.</p>
 *
 * <h2>Invariantes</h2>
 * <ul>
 *   <li>El árbol nunca está vacío: se crea con la consulta inicial como raíz.</li>
 *   <li>Todo evento del árbol pertenece al mismo paciente que el episodio.</li>
 *   <li>Un evento sólo puede cerrarse si todos sus descendientes están cerrados.</li>
 *   <li>El episodio está CERRADO si y sólo si su evento raíz está cerrado, lo que por
 *       el punto anterior implica que todo el árbol lo está.</li>
 * </ul>
 */
public class Episodio implements Comparable<Episodio> {

    private final String idEpisodio;
    private final Paciente paciente;
    private final ArbolGenerico<EventoClinico> arbol;
    private final LocalDateTime fechaApertura;
    private EstadoEpisodio estado;
    private LocalDateTime fechaCierre;

    /**
     * Abre un episodio con su consulta inicial, que queda como raíz del árbol.
     */
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

    /**
     * Profundidad de derivación alcanzada: 1 si el episodio se resolvió en la consulta
     * inicial, más si hubo cadenas de derivaciones.
     */
    public int profundidad() {
        return arbol.altura();
    }

    // ---------- registro de eventos ----------

    /**
     * Registra un evento asociándolo a aquello que lo originó.
     *
     * <p>No hay límite de cuántos hijos puede tener un evento ni de cuán profundo puede
     * llegar la cadena: eso es lo que aporta el árbol n-ario frente a una lista.</p>
     *
     * <p>El evento origen tiene que estar <b>abierto</b>. Si ya se cerró, colgarle algo
     * nuevo dejaría un evento cerrado con un descendiente abierto, que es exactamente
     * lo que la regla de cierre prohíbe. Una complicación que aparece después de haber
     * cerrado el procedimiento no es una derivación de ese procedimiento: es un evento
     * nuevo, y va colgado de algo que siga abierto.</p>
     *
     * @param idEventoPadre el evento del que se desprende éste
     * @return {@code false} si ya existía un evento con ese id
     * @throws NoSuchElementException si el evento padre no está en el episodio
     * @throws IllegalStateException si el episodio ya está cerrado, o si el evento
     *         origen ya está cerrado
     */
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

    /**
     * Igual que {@link #registrarEvento(String, EventoClinico)}, pero armando el evento
     * acá. Evita que el llamador tenga que repetir el id del paciente, que es el del
     * episodio.
     */
    public boolean registrarEvento(String idEventoPadre, String idEvento, TipoEvento tipo,
                                   String descripcion, LocalDateTime momento) {
        return registrarEvento(idEventoPadre,
                new EventoClinico(idEvento, paciente.getId(), tipo, descripcion, momento));
    }

    // ---------- insumos ----------

    /**
     * Registra un insumo en el evento donde efectivamente se consumió.
     *
     * <p>Pasa por el episodio y no directamente por el evento para poder verificar dos
     * cosas: que el evento sea de este episodio, y que siga abierto. Un evento cerrado
     * ya rindió sus costos y no debería seguir acumulando.</p>
     *
     * @throws NoSuchElementException si el evento no está en el episodio
     * @throws IllegalStateException si el evento ya está cerrado
     */
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

    /**
     * Busca un evento del episodio por su id, o {@code null} si no está.
     *
     * <p>Es O(n) sobre los eventos del episodio: en el árbol n-ario no hay criterio de
     * orden que permita podar, así que hay que mirar todos los nodos. Como un episodio
     * tiene pocas decenas de eventos, el costo es aceptable; el acceso rápido por fecha
     * lo da el AVL de eventos, no este árbol.</p>
     */
    public EventoClinico buscarEvento(String idEvento) {
        if (idEvento == null) {
            return null;
        }
        return arbol.buscar(EventoClinico.porId(idEvento));
    }

    // ---------- cierre ----------

    /**
     * Qué impide cerrar un evento: sus descendientes que siguen abiertos.
     *
     * <p>Se recorre en <b>post-orden</b>, que es el recorrido que procesa a los
     * descendientes antes que al nodo: es justo el orden en que hay que mirar las cosas
     * para responder "¿puedo cerrar esto?".</p>
     *
     * @return lista vacía si nada lo impide
     */
    public TDALista<EventoClinico> queImpideCerrar(String idEvento) {
        TDALista<EventoClinico> bloqueantes = new ListaEnlazada<>();
        if (buscarEvento(idEvento) == null) {
            throw new NoSuchElementException("No existe el evento " + idEvento);
        }
        arbol.postOrderDesde(EventoClinico.porId(idEvento), evento -> {
            // el propio evento no se bloquea a sí mismo: se lo está por cerrar
            if (evento.estaAbierto() && !evento.getIdEvento().equals(idEvento)) {
                bloqueantes.agregar(evento);
            }
        });
        return bloqueantes;
    }

    public boolean puedeCerrarse(String idEvento) {
        return queImpideCerrar(idEvento).esVacio();
    }

    /**
     * Cierra un evento, si todo lo que se desprendió de él ya está cerrado.
     *
     * @return {@code false} si el evento ya estaba cerrado
     * @throws NoSuchElementException si el evento no está en el episodio
     * @throws IllegalStateException si hay descendientes abiertos; el mensaje dice
     *         cuáles son
     */
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

    /**
     * Qué impide cerrar el episodio completo: los eventos abiertos que cuelgan de la
     * consulta inicial.
     */
    public TDALista<EventoClinico> queImpideCerrarEpisodio() {
        return queImpideCerrar(getConsultaInicial().getIdEvento());
    }

    /**
     * Cierra el episodio cerrando su evento raíz, lo que sólo es posible si todo el
     * árbol está cerrado.
     *
     * @return {@code false} si el episodio ya estaba cerrado
     */
    public boolean cerrarEpisodio(LocalDateTime momento) {
        if (estado == EstadoEpisodio.CERRADO) {
            return false;
        }
        cerrarEvento(getConsultaInicial().getIdEvento(), momento);
        this.estado = EstadoEpisodio.CERRADO;
        this.fechaCierre = momento;
        return true;
    }

    // ---------- costos y tiempos ----------

    /**
     * Costo del subárbol que cuelga de un evento, incluido ese evento.
     *
     * <p>Se calcula recorriendo y sumando, no leyendo un acumulado guardado: cada
     * insumo está registrado en un único lugar, el evento donde se consumió, y el total
     * es una consecuencia. Por eso pedir el costo de una parte y el del episodio
     * completo usan el mismo mecanismo, cambiando sólo desde dónde arranca.</p>
     */
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

    /**
     * Minutos acumulados del subárbol que cuelga de un evento, incluido ese evento.
     * Los eventos todavía abiertos no suman.
     */
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

    // ---------- consultas sobre la jerarquía ----------

    /**
     * Los eventos que se desprendieron directamente del indicado.
     */
    public TDALista<EventoClinico> derivacionesDe(String idEvento) {
        return arbol.hijosDe(EventoClinico.porId(idEvento));
    }

    /**
     * Todo lo que derivó del evento indicado, directa o indirectamente.
     */
    public TDALista<EventoClinico> todoLoQueDerivoDe(String idEvento) {
        return arbol.descendientesDe(EventoClinico.porId(idEvento));
    }

    /**
     * Los eventos del episodio que siguen abiertos.
     */
    public TDALista<EventoClinico> eventosAbiertos() {
        TDALista<EventoClinico> abiertos = new ListaEnlazada<>();
        arbol.preOrder(evento -> {
            if (evento.estaAbierto()) {
                abiertos.agregar(evento);
            }
        });
        return abiertos;
    }

    /**
     * Todos los códigos de diagnóstico del episodio, tomados de sus eventos y sin
     * repetir.
     *
     * <p>Los códigos se registran en el evento que los diagnosticó; el episodio no
     * guarda una copia, los reúne cuando se los piden.</p>
     */
    public TDALista<CodigoDiagnostico> codigos() {
        TDALista<CodigoDiagnostico> resultado = new ListaEnlazada<>();
        arbol.preOrder(evento -> {
            ListaEnlazada<CodigoDiagnostico> delEvento = evento.getCodigosDiagnostico();
            for (int i = 0; i < delEvento.tamaño(); i++) {
                CodigoDiagnostico codigo = delEvento.obtener(i);
                if (!resultado.contiene(codigo)) {
                    resultado.agregar(codigo);
                }
            }
        });
        return resultado;
    }

    /**
     * El episodio visto por niveles: primero la consulta inicial, después lo que salió
     * de ella, después lo que salió de eso.
     */
    public String porNivelesString() {
        return arbol.porNivelesString();
    }

    // ---------- comparación ----------

    /**
     * Orden por fecha de apertura, desempatando por id.
     *
     * <p>Mismo motivo que en {@link EventoClinico}: dos episodios pueden abrirse en el
     * mismo instante y sin el desempate el árbol de episodios del paciente perdería
     * uno.</p>
     */
    @Override
    public int compareTo(Episodio otro) {
        int porFecha = fechaApertura.compareTo(otro.fechaApertura);
        if (porFecha != 0) {
            return porFecha;
        }
        return idEpisodio.compareTo(otro.idEpisodio);
    }

    /**
     * Criterio de igualdad por id, para buscar un episodio sin conocer su fecha.
     */
    public static Comparable<Episodio> porId(String idEpisodio) {
        if (idEpisodio == null) {
            throw new IllegalArgumentException("Debe haber un id de episodio");
        }
        return otro -> idEpisodio.compareTo(otro.getIdEpisodio());
    }

    /**
     * Cota por fecha de apertura, para usar como extremo en una consulta por rango.
     *
     * <p>Compara sólo por fecha, ignorando el id. Eso es lo correcto para acotar: el
     * árbol está ordenado por (fecha, id), así que una cota por fecha sigue siendo
     * consistente con ese orden y la poda del rango sigue valiendo. Al ignorar el
     * desempate, los extremos quedan inclusivos: entran todos los episodios de esa
     * fecha, sin importar su id.</p>
     */
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
