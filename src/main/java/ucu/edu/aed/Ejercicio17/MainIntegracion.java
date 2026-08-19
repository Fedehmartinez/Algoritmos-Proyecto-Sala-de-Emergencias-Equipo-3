package ucu.edu.aed.Ejercicio17;

public class MainIntegracion {

    public static void main(String[] args){
        Biblioteca biblioteca = new Biblioteca();

        Adquisiciones adquisiciones = new Adquisiciones();
        String rutaAdquisiciones = "ProblemSet_UT1\\src\\main\\java\\ucu\\edu\\aed\\resources ej17\\adquisiciones.txt";
        double valorTotalAgregado = adquisiciones.procesarArchivo(rutaAdquisiciones, biblioteca);

        Prestamos prestamos = new Prestamos();
        String rutaPrestamos = "ProblemSet_UT1\\src\\main\\java\\ucu\\edu\\aed\\resources ej17\\prestamos.txt";
        int variacionTotalPrestados = prestamos.procesarArchivo(rutaPrestamos, biblioteca);

        System.out.println("Valor total agregado al stock: " + valorTotalAgregado);
        System.out.println("Variación total de ejemplares prestados: " + variacionTotalPrestados);
        System.out.println("Catálogo final:");
        System.out.println(biblioteca.listarPorTitulo());
    }
}