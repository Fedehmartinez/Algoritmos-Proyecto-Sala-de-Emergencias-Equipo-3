package ucu.edu.aed.clases;

import java.util.Comparator;
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

    public SalaEmergencia() {
        this.pacientesRegistrados = new ListaEnlazada<>();
        this.esperaAtencion = new ColaPrioridad<>(POR_URGENCIA);
        this.historialConsultas = new Pila<>();
        this.consultorios = new ListaArray<>();
    }

    public Paciente registrarPaciente(String nombre, String id) {
        return null; // Crea un paciente y lo agrega a la lista de pacientes registrados
    }

    public void agregarPaciente(Paciente pacienteNuevo, NivelUrgencia urgencia) {
    // Lo mete en cola
    if (urgencia == null){
        throw new IllegalArgumentException("Debe haber un nivel de urgencia");
    }
    pacienteNuevo.setUrgencia(urgencia);
    pacienteNuevo.setEstadoPaciente(EstadoPaciente.EN_ESPERA);
    esperaAtencion.agregar(pacienteNuevo);
    }


    /* 

    Todavía no podemos listar pacientes ni consultas, el obtener de ListaEnlazada es por ID, así que puede hacerse un for para leer todo
    pero cuando se lo dije a claude me dijo que eso es de orden O(n²) y que lo mejor es tocar tocar el toString de listaEnlazada
    para que haga un solo recorrido y pase a O(n), también me dijo que algo mejor aún sería utilizar algo estilo Iterable, es una biblioteca
    que nos ayuda con esto, pero como no tengo idea lo dejo por acá y me voy a dormir 😁

    */
    public void listarPacientes() {
    
    }

    public void listarConsultas() {

    }

    // fede revisa esto porque esto si es IA full
    public Paciente buscarPaciente(String idPaciente) {
    return pacientesRegistrados.buscar(new Predicate<Paciente>() {
            @Override
            public boolean test(Paciente p) {
                return p.getId().equals(idPaciente);
            }
        });
    }

    public void eliminarPaciente(String idPaciente) {

    }

    public void mostrarPacientesEnConsultorios() {

    }

    public void ingresarPaciente(Paciente paciente) {

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