package ucu.edu.aed.benchmark;

import java.util.Random;

import ucu.edu.aed.clases.SalaEmergencia;
import ucu.edu.aed.implementaciones.ListaArray;

// Desafío 3: medimos cuanto tarda registrar y eliminar pacientes, con cada vez mas pacientes, para ver como crece el tiempo.
// Este mismo archivo, sin cambios, se corre tambien en el proyecto de Hito 1 para comparar los dos.
public class BenchmarkRegistroPacientes {

    public static void main(String[] args) {
        int[] volumenes = {1000, 2000, 4000, 8000, 16000, 32000};
        int repeticiones = 7;

        System.out.println("caso,n,repeticion,registrarMs,eliminarMs");

        for (int i = 0; i < volumenes.length; i++) {
            int n = volumenes[i];
            for (int rep = 1; rep <= repeticiones; rep++) {
                ListaArray<String> idsAlAzar = generarIdsAlAzar(n);
                medir("promedio", n, rep, idsAlAzar);

                ListaArray<String> idsDescendentes = generarIdsDescendentes(n);
                medir("peor_caso", n, rep, idsDescendentes);
            }
        }
    }

    // Registra todos los ids de la lista y mide cuanto tardo, despues los
    // elimina a todos y mide cuanto tardó eso.
    private static void medir(String caso, int n, int repeticion, ListaArray<String> ids) {
        SalaEmergencia sala = new SalaEmergencia();

        long inicioRegistrar = System.nanoTime();
        for (int i = 0; i < ids.tamaño(); i++) {
            String id = ids.obtener(i);
            sala.registrarPaciente("Paciente " + id, id);
        }
        long finRegistrar = System.nanoTime();

        long inicioEliminar = System.nanoTime();
        for (int i = 0; i < ids.tamaño(); i++) {
            String id = ids.obtener(i);
            sala.eliminarPaciente(id);
        }
        long finEliminar = System.nanoTime();

        double registrarMs = nanosAMilisegundos(finRegistrar - inicioRegistrar);
        double eliminarMs = nanosAMilisegundos(finEliminar - inicioEliminar);

        System.out.println(caso + "," + n + "," + repeticion + "," + registrarMs + "," + eliminarMs);
    }

    private static double nanosAMilisegundos(long nanos) {
        double milisegundos = nanos / 1000000.0;
        // redondeamos a 3 decimales a mano
        double redondeado = Math.round(milisegundos * 1000.0) / 1000.0;
        return redondeado;
    }

    // Generamos los ids de forma descendiente asi se presencia el peor caso de ListaArray
    private static ListaArray<String> generarIdsDescendentes(int n) {
        ListaArray<String> ids = new ListaArray<>(n);
        for (int numero = n - 1; numero >= 0; numero--) {
            ids.agregar(armarId(numero, n));
        }
        return ids;
    }

    // Genera los ids de forma aleatoria
    private static ListaArray<String> generarIdsAlAzar(int n) {
        int[] numeros = new int[n];
        for (int i = 0; i < n; i++) {
            numeros[i] = i;
        }

        Random random = new Random();
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temporal = numeros[i];
            numeros[i] = numeros[j];
            numeros[j] = temporal;
        }

        ListaArray<String> ids = new ListaArray<>(n);
        for (int i = 0; i < n; i++) {
            ids.agregar(armarId(numeros[i], n));
        }
        return ids;
    }

    // Arma un id con ceros adelante, por ejemplo armarId(7, 1000) da "P007".
    private static String armarId(int numero, int n) {
        int cantidadDeDigitos = ("" + n).length();
        String texto = "" + numero;
        while (texto.length() < cantidadDeDigitos) {
            texto = "0" + texto;
        }
        return "P" + texto;
    }
}
