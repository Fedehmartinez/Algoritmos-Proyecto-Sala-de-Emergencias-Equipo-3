package ucu.edu.aed.Ejercicio17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Adquisiciones {

    
    public double procesarArchivo(String rutaArchivo, Biblioteca biblioteca){
        double valorTotalAgregado = 0;
        try{
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea = lector.readLine();
            while (linea != null){
                if (!linea.trim().isEmpty()){
                    String[] partes = linea.split(",");
                    String codigo = partes[0].trim();
                    String titulo = partes[1].trim();
                    double precio = Double.parseDouble(partes[2].trim());
                    int cantidad = Integer.parseInt(partes[3].trim());

                    boolean yaExiste = biblioteca.consultarExistencias(codigo) != -1;

                    if (yaExiste){
                        biblioteca.agregarEjemplares(codigo, cantidad);
                    }
                    else{
                        Libro nuevoLibro = new Libro(titulo, codigo, precio, cantidad);
                        biblioteca.incorporarLibro(nuevoLibro);
                    }

                    valorTotalAgregado = valorTotalAgregado + (precio * cantidad);
                }
                linea = lector.readLine();
            }
            lector.close();
        }
        catch (IOException e){
            System.out.println("Error al leer el archivo de adquisiciones: " + e.getMessage());
        }
        return valorTotalAgregado;
    }
}