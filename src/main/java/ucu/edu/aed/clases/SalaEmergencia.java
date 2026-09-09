package ucu.edu.aed.clases;

import java.util.Comparator;
import java.util.NoSuchElementException;

import ucu.edu.aed.implementaciones.AVLImpl;
import ucu.edu.aed.implementaciones.Heap;
import ucu.edu.aed.implementaciones.ListaArray;
import ucu.edu.aed.implementaciones.Pila;

public class SalaEmergencia {

       // Ordena por tiempo máximo de espera tolerable: menos tiempo, más prioridad.
    public static final Comparator<Paciente> POR_URGENCIA =
            Comparator.comparingInt(p -> p.getUrgencia().getTiempoMaximoEsperaMinutos());

    private final AVLImpl<Paciente> pacientesRegistrados;
    private final Heap<Paciente> esperaAtencion;
    private final Pila<Consulta> historialConsultas;
    private final ListaArray<Paciente> consultorios;

    public SalaEmergencia() {
        this.pacientesRegistrados = new AVLImpl<>();
        this.esperaAtencion = new Heap<>(POR_URGENCIA);
        this.historialConsultas = new Pila<>();
        this.consultorios = new ListaArray<>(5);
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
     * hace en tres pasos: sacarlo (rompe el invariante de orden si se le cambiara la
     * urgencia estando adentro), reasignarle la urgencia nueva, y volver a insertarlo
     * para que el heap lo reacomode en la posición que le corresponde.</p>
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

    public String listarPacientes() {
    if (pacientesRegistrados.esVacio()) {
        return "No hay pacientes registrados";
    }
        return "Pacientes registrados:\n" + pacientesRegistrados.inOrderString();
    }

    public String listarConsultas() {
    if (historialConsultas.esVacio()) {
        return "No hay consultas registradas";
    }
        return "Historial de consultas:\n" + historialConsultas;
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
        consultorios.remover(paciente);
        pacientesRegistrados.eliminar(Paciente.porId(idPaciente));
    }

    public String mostrarPacientesEnConsultorios() {
        if (consultorios.esVacio()) {
            return "No hay pacientes en consultorios.";
        }
        return "Pacientes en consultorios:\n" + consultorios;
    }

    public void ingresarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("Debe haber un paciente");
        }
        if (consultorios.tamaño() >= consultorios.capacidad()) {
            throw new IllegalStateException("No hay consultorios libres");
        }
        esperaAtencion.remover(paciente);
        consultorios.agregar(paciente);
        paciente.setEstadoPaciente(EstadoPaciente.EN_CONSULTORIO);
    }

    public void agregarConsultorio() {
        consultorios.ampliarCapacidad(1);
    }


    public void darDeAlta(int numeroDelConsultorio, String procedimiento) {
        if (procedimiento == null) {
            throw new IllegalArgumentException("Debe haber un procedimiento");
        }
        Paciente paciente = consultorios.remover(numeroDelConsultorio);
        Consulta consulta = new Consulta(paciente.getId(), paciente.getUrgencia(), procedimiento);
        historialConsultas.mete(consulta);
        paciente.setEstadoPaciente(EstadoPaciente.ATENDIDO);
    }

        @Override
    public String toString() {
        return "Sala de emergencias"
                + " | Registrados: " + pacientesRegistrados.cantidadNodos()
                + " | En espera: " + esperaAtencion.cantidad()
                + " | En consultorio: " + consultorios.tamaño()
                + " | Consultas realizadas: " + historialConsultas.tamaño();
    }

}