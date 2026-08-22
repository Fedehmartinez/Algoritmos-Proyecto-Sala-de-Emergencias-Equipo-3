package ucu.edu.aed.clases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consulta {

    private final String idPaciente;
    private final NivelUrgencia urgencia;
    private final String procedimiento;
    private final LocalDateTime fecha;

    public Consulta(String idPaciente, NivelUrgencia urgencia, String procedimiento) {
        this.idPaciente = idPaciente;
        this.urgencia = urgencia;
        this.procedimiento = procedimiento;
        this.fecha = LocalDateTime.now();
    }

    public String getIdPaciente() { 
        return idPaciente; 
    }

    public NivelUrgencia getUrgencia() {
        return urgencia; 
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public LocalDateTime getFecha() {
        return fecha; 
    }

        
    private static final DateTimeFormatter FORMATO_FECHA =
       DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public String toString() {
        return "[" + fecha.format(FORMATO_FECHA) + "] Paciente " + idPaciente
                + " - " + urgencia + " - " + procedimiento;
    }
}