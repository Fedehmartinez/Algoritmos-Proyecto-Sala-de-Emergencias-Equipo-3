package ucu.edu.aed.clases;

import ucu.edu.aed.implementaciones.Cola;
import ucu.edu.aed.implementaciones.ListaArray;
import ucu.edu.aed.implementaciones.ListaEnlazada;
import ucu.edu.aed.implementaciones.Pila;

public class SalaEmergencia {

    private final ListaEnlazada<Paciente> pacientesRegistrados;
    private final Cola<Paciente> esperaAtencion;
    private final Pila<Consulta> historialConsultas;
    private final ListaArray<Paciente> consultorios;

    public SalaEmergencia() {
        this.pacientesRegistrados = new ListaEnlazada<>();
        this.esperaAtencion = new Cola<>();
        this.historialConsultas = new Pila<>();
        this.consultorios = new ListaArray<>();
    }

    public Paciente registrarPaciente(String nombre, String id) {
        return null; // Crea un paciente y lo agrega a la lista de pacientes registrados
    }

    public void agregarPaciente(Paciente pacienteNuevo, NivelUrgencia urgencia) {
    // Lo mete en cola
    }

    public void listarPacientes() {

    }

    public void listarConsultas() {

    }

    public Paciente buscarPaciente(String idPaciente) {
        return null;
    }

    public void eliminarPaciente(String idPaciente) {

    }

    public void mostrarPacientesEnConsultorios() {

    }

    public void ingresarPaciente(Paciente paciente) {

    }
    
    public void darDeAlta(int numeroDelConsultorio) {
    // Lo saca de la ListaArray y lo ingresa en la pila de consultas 
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