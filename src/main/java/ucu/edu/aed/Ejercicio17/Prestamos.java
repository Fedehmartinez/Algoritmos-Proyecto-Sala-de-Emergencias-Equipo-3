package ucu.edu.aed.Ejercicio17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Prestamos {

    public int procesarArchivo(String rutaArchivo, Biblioteca biblioteca){
        int variacionTotal = 0;
        try{
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea = lector.readLine();
            while (linea != null){
                if (!linea.trim().isEmpty()){
                    String[] partes = linea.split(",");
                    String codigo = partes[0].trim();
                    String tipo = partes[1].trim();
                    int cantidad = Integer.parseInt(partes[2].trim());

                    if (tipo.equals("PRESTAMO")){
                        int prestados = 0;
                        int i = 0;
                        while (i < cantidad && biblioteca.registrarPrestamo(codigo)){
                            prestados++;
                            i++;
                        }
                        variacionTotal = variacionTotal + prestados;
                    }
                    else if (tipo.equals("DEVOLUCION")){
                        int i = 0;
                        while (i < cantidad){
                            biblioteca.registrarDevolucion(codigo);
                            i++;
                        }
                        variacionTotal = variacionTotal - cantidad;
                    }
                }
                linea = lector.readLine();
            }
            lector.close();
        }
        catch (IOException e){
            System.out.println("Error al leer el archivo de préstamos: " + e.getMessage());
        }
        return variacionTotal;
    }
}