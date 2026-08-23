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
        return null; // Crea un paciente y lo agrega a la lista de pacientes registrados
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

    public void listarPacientes() {
    if (pacientesRegistrados.esVacio()) {
        System.out.println("No hay pacientes registrados");
        return;
    }
        System.out.println("Pacientes registrados:");
        System.out.println(pacientesRegistrados);
    }

    public void listarConsultas() {
    if (historialConsultas.esVacio()) {
        System.out.println("No hay consultas registradas");
        return;
    }
        System.out.println("Historial de consultas:");
        System.out.println(historialConsultas);
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
        pacientesRegistrados.remover(paciente);
    }

    public void mostrarPacientesEnConsultorios() {
        if (consultorios.esVacio()) {
            System.out.println("No hay pacientes en consultorios");
            return;
        }
        System.out.println("Pacientes en consultorios:");
        System.out.println(consultorios);
    }

    public void ingresarPaciente(Paciente paciente) {
        if (consultorios.tamaño() >= capacidadConsultorios) {
            throw new IllegalStateException("No hay consultorios libres");
        }
        esperaAtencion.remover(paciente);
        consultorios.agregar(paciente);
        paciente.setEstadoPaciente(EstadoPaciente.EN_CONSULTORIO);
    }

    public void agregarConsultorio() {
        capacidadConsultorios++;
        consultorios.ampliarCapacidad(1);
    }


    public void darDeAlta(int numeroDelConsultorio) {
    // Lo saca de la ListaArray y lo ingresa en la pila de consultas
    // no se refiere a dar de alta en el sistema, si no a dar de alta de la consulta/sanatorio/quirofano/lo que sea
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