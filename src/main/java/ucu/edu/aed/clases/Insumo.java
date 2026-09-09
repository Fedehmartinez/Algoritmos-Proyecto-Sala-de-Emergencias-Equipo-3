package ucu.edu.aed.clases;

public class Insumo {

    private final String nombre;
    private final double costoUnitario;
    private final int cantidad;

    public Insumo(String nombre, double costoUnitario, int cantidad) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El insumo debe tener nombre");
        }
        if (costoUnitario < 0) {
            throw new IllegalArgumentException("El costo unitario no puede ser negativo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.nombre = nombre;
        this.costoUnitario = costoUnitario;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double costoTotal() {
        return costoUnitario * cantidad;
    }

    @Override
    public String toString() {
        return nombre + " x" + cantidad + " ($" + String.format("%.2f", costoTotal()) + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Insumo)) return false;
        Insumo otro = (Insumo) obj;
        return nombre.equals(otro.nombre)
                && Double.compare(costoUnitario, otro.costoUnitario) == 0
                && cantidad == otro.cantidad;
    }

    @Override
    public int hashCode() {
        return nombre.hashCode() * 31 + cantidad;
    }
}
