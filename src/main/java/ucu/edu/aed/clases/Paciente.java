package ucu.edu.aed.clases;

import java.time.LocalDateTime;
import ucu.edu.aed.implementaciones.ListaEnlazada;

public class Paciente {

    private final String id;
    private final String nombre;
    private final ListaEnlazada<String> caracteristicas;
    private NivelUrgencia urgencia;
 // private final LocalDateTime horaLlegada;

    public Paciente(String id, String nombre, NivelUrgencia urgencia) {
        this.id = id;
        this.nombre = nombre;
        this.caracteristicas = new ListaEnlazada<>();
        this.urgencia = urgencia;
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

    public ListaEnlazada<String> getCaracteristicas() { 
        return caracteristicas; 
    }

    public void setCaracteristica(String caracteristicaNueva){
        
    }

   
  //  public LocalDateTime getHoraLlegada() { return horaLlegada; }

  //  public long minutosEsperando(LocalDateTime momento) { }
  
}