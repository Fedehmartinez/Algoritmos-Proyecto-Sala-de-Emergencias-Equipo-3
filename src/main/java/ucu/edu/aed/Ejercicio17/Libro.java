package ucu.edu.aed.Ejercicio17;

public class Libro {

    private String titulo;
    private String codigo;
    private double precioReposicion;
    private int cantidadDisponible;

    public Libro(String titulo, String codigo, double precioReposicion, int cantidadDisponible){
        this.titulo = titulo;
        this.codigo = codigo;
        this.precioReposicion = precioReposicion;
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getCodigo(){
        return codigo;
    }

    public double getPrecioReposicion(){
        return precioReposicion;
    }

    public int getCantidadDisponible(){
        return cantidadDisponible;
    }

    public void agregarEjemplares(int cantidad){
        cantidadDisponible = cantidadDisponible + cantidad;
    }

    public boolean prestar(){
        if (cantidadDisponible <= 0){
            return false;
        }
        else{
            cantidadDisponible = cantidadDisponible - 1;
            return true;
        }
    }

    public void devolver(){
        cantidadDisponible = cantidadDisponible + 1;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Libro otro = (Libro) obj;
        return codigo.equals(otro.codigo);
    }

    @Override
    public String toString(){
        return titulo + " (código: " + codigo + ") - stock: " + cantidadDisponible;
    }
}