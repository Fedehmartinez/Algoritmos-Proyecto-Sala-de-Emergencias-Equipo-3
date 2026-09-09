package ucu.edu.aed.clases;

import java.time.LocalDateTime;

import ucu.edu.aed.implementaciones.AVLImpl;
import ucu.edu.aed.implementaciones.ListaEnlazada;
import ucu.edu.aed.tda.TDALista;

public class Paciente implements Comparable<Paciente> {

    private final String id;
    private final String nombre;
    private final ListaEnlazada<String> caracteristicas;
    private NivelUrgencia urgencia;
    private EstadoPaciente estado;
    private Episodio episodioActual;
    private final AVLImpl<Episodio> episodios;

    public Paciente(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.caracteristicas = new ListaEnlazada<>();
        this.urgencia = null;
        this.estado = EstadoPaciente.REGISTRADO;
        this.episodioActual = null;
        this.episodios = new AVLImpl<>();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre).append(" (ID: ").append(id).append(") - ");
        sb.append(urgencia == null ? "sin clasificar" : urgencia.name());
        sb.append(" - Caracteristicas: ");
        sb.append(caracteristicas.esVacio() ? "ninguna" : caracteristicas.toString());
    return sb.toString();
    }

    public Episodio getEpisodioActual() {
        return episodioActual;
    }

    public boolean tieneEpisodioAbierto() {
        return episodioActual != null;
    }

    public AVLImpl<Episodio> getEpisodios() {
        return episodios;
    }

    public int cantidadEpisodios() {
        return episodios.cantidadNodos();
    }

    public void abrirEpisodio(Episodio episodio) {
        if (episodio == null) {
            throw new IllegalArgumentException("Debe haber un episodio");
        }
        if (!episodio.getPaciente().getId().equals(id)) {
            throw new IllegalArgumentException("El episodio es de otro paciente: " + episodio.getPaciente().getId());
        }
        if (episodioActual != null) {
            throw new IllegalStateException("El paciente " + id + " ya tiene el episodio " + episodioActual.getIdEpisodio() + " abierto");
        }
        episodios.insertar(episodio);
        this.episodioActual = episodio;
    }

    public Episodio cerrarEpisodioActual(LocalDateTime momento) {
        if (episodioActual == null) {
            throw new IllegalStateException(
                    "El paciente " + id + " no tiene ningun episodio abierto");
        }
        episodioActual.cerrarEpisodio(momento);
        Episodio cerrado = episodioActual;
        this.episodioActual = null;
        return cerrado;
    }

    public TDALista<Episodio> episodiosEnRango(LocalDateTime desde, LocalDateTime hasta) {
        return episodios.enRango(
                desde == null ? null : Episodio.porFecha(desde),
                hasta == null ? null : Episodio.porFecha(hasta));
    }

    public Episodio buscarEpisodio(String idEpisodio) {
        if (idEpisodio == null) {
            return null;
        }
        Episodio[] encontrado = {null};
        episodios.inOrder(episodio -> {
            if (episodio.getIdEpisodio().equals(idEpisodio)) {
                encontrado[0] = episodio;
            }
        });
        return encontrado[0];
    }

    @Override
    public int compareTo(Paciente otro) {
        return id.compareTo(otro.id);
    }

    public static Comparable<Paciente> porId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Debe haber un id");
        }
        return otro -> id.compareTo(otro.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Paciente)) return false;
        Paciente otro = (Paciente) obj;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
