package ucu.edu.aed.clases;

import java.time.LocalDateTime;
import ucu.edu.aed.implementaciones.ListaEnlazada;

public class Paciente {

    private final String id;
    private final String nombre;
    private final ListaEnlazada<String> caracteristicas;
    private NivelUrgencia urgencia;
    private EstadoPaciente estado;
 // private final LocalDateTime horaLlegada;

    public Paciente(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.caracteristicas = new ListaEnlazada<>();
        this.urgencia = null; // Se asigna la urgencia más adelante, cuando se agregue a la cola de prioridad
        this.estado = EstadoPaciente.REGISTRADO;
//      this.horaLlegada = LocalDateTime.now();
    }

    public String getId() { 
        return id; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public NivelUrgencia getUrgencia() { 
        return urgencia; 
    }

    public void setUrgencia(NivelUrgencia urgencia) { 
        this.urgencia = urgencia; 
    }

    public EstadoPaciente getEstadoPaciente(){
        return estado;
    }

    public void setEstadoPaciente(EstadoPaciente estado){
        this.estado = estado; 
    }
    public ListaEnlazada<String> getCaracteristicas() { 
        return caracteristicas; 
    }

   public void agregarCaracteristica(String caracteristicaNueva) {
        caracteristicas.agregar(caracteristicaNueva);
    }

    public boolean eliminarCaracteristica(String caracteristica) {
        return caracteristicas.remover(caracteristica);
    }
    

   
  //  public LocalDateTime getHoraLlegada() { return horaLlegada; }

  //  public long minutosEsperando(LocalDateTime momento) { }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre).append(" (ID: ").append(id).append(") - ");
        sb.append(urgencia == null ? "sin clasificar" : urgencia.name());
        sb.append(" - Caracteristicas: ");
        sb.append(caracteristicas.esVacio() ? "ninguna" : caracteristicas.toString());
    return sb.toString();
}

}