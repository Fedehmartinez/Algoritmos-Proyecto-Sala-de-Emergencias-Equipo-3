package ucu.edu.aed.clases;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import ucu.edu.aed.implementaciones.ColaPrioridad;
import ucu.edu.aed.implementaciones.ListaArray;
import ucu.edu.aed.implementaciones.ListaEnlazada;
import ucu.edu.aed.implementaciones.Pila;

public class SalaEmergencia {

       /** Ordena por tiempo máximo de espera tolerable: menos tiempo, más prioridad. */
    public static final Comparator<Paciente> POR_URGENCIA =
            Comparator.comparingInt(p -> p.getUrgencia().getTiempoMaximoEsperaMinutos());

    private final ListaEnlazada<Paciente> pacientesRegistrados;
    private final ColaPrioridad<Paciente> esperaAtencion;
    private final Pila<Consulta> historialConsultas;
    private final ListaArray<Paciente> consultorios;
    private int capacidadConsultorios = 5;

    public SalaEmergencia() {
        this.pacientesRegistrados = new ListaEnlazada<>();
        this.esperaAtencion = new ColaPrioridad<>(POR_URGENCIA);
        this.historialConsultas = new Pila<>();
        this.consultorios = new ListaArray<>(capacidadConsultorios);
    }

    public Paciente registrarPaciente(String nombre, String id) {
        if (nombre == null || id == null) {
            throw new IllegalArgumentException("Debe haber nombre e id");
        }
        if (buscarPaciente(id) != null) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con id " + id);
        }
        Paciente paciente = new Paciente(id, nombre);
        pacientesRegistrados.agregar(paciente);
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
    esperaAtencion.agregar(registrado);
    }

    public String listarPacientes() {
    if (pacientesRegistrados.esVacio()) {
        return "No hay pacientes registrados";
    }
        return "Pacientes registrados:\n" + pacientesRegistrados;
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
    return pacientesRegistrados.buscar(new Predicate<Paciente>() {
            @Override
            public boolean test(Paciente p) {
                return p.getId().equals(idPaciente);
            }
        });
    }

    public void eliminarPaciente(String idPaciente) {
        Paciente paciente = buscarPaciente(idPaciente);
        if (paciente == null) {
            throw new NoSuchElementException("Paciente no encontrado: " + idPaciente);
        }
        esperaAtencion.remover(paciente);
        consultorios.remover(paciente);
        pacientesRegistrados.remover(paciente);
    }

    public String mostrarPacientesEnConsultorios() {
        if (consultorios.esVacio()) {
            return "No hay pacientes en consultorios";
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
                + " | Registrados: " + pacientesRegistrados.tamaño()
                + " | En espera: " + esperaAtencion.tamaño()
                + " | En consultorio: " + consultorios.tamaño()
                + " | Consultas realizadas: " + historialConsultas.tamaño();
    }

}