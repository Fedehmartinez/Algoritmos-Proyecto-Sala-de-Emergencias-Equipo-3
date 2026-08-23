package ucu.edu.aed.clases;

public enum EstadoPaciente {
    REGISTRADO,      // Registrado, pero todavía no ha hecho consultas
    EN_ESPERA,       // en la cola de prioridad
    EN_CONSULTORIO,  // siendo atendido
    ATENDIDO         // dado de alta, su consulta está en el historial
}