package ucu.edu.aed.clases;

import java.util.Random;

import ucu.edu.aed.implementaciones.ListaArray;

// Desafío 3: medimos cuanto tarda registrar y eliminar pacientes, con cada
// vez mas pacientes, para ver como crece el tiempo.
// Este mismo archivo, sin cambios, se
// corre tambien en el proyecto de Hito 1 para comparar los dos.
//
// Como correrlo: ejecutar el main y guardar lo que imprime en un archivo,
// por ejemplo: java -cp target/classes ucu.edu.aed.clases.BenchmarkRegistroPacientes > resultados.csv
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
    // elimina a todos y mide cuanto tardo eso.
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
        // redondeamos a 3 decimales a mano, asi no hace falta usar printf
        // (printf usa la configuracion regional y en esta pc pone coma en
        // vez de punto, lo que rompe el csv)
        double redondeado = Math.round(milisegundos * 1000.0) / 1000.0;
        return redondeado;
    }

    // Genera los ids en orden descendente: P00999, P00998, ..., P00000.
    // Era el peor caso para el ListaArray ordenado del primer hito (cada id
    // nuevo se inserta al principio y corre todo el arreglo). Para el AVL
    // de este hito no tiene por que seguir siendo un caso especialmente
    // malo, y esa comparacion es justamente parte de lo que queremos ver.
    private static ListaArray<String> generarIdsDescendentes(int n) {
        ListaArray<String> ids = new ListaArray<>(n);
        for (int numero = n - 1; numero >= 0; numero--) {
            ids.agregar(armarId(numero, n));
        }
        return ids;
    }

    // Genera los ids en un orden al azar (caso promedio, sin ventaja para
    // ninguna de las dos implementaciones).
    //
    // Usamos un arreglo comun de enteros (no una coleccion de Java) para
    // mezclar el orden con el metodo de Fisher-Yates: recorremos de atras
    // para adelante y en cada paso intercambiamos la posicion actual con
    // una posicion al azar entre las que quedan.
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
