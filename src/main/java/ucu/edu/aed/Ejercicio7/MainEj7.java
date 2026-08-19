package ucu.edu.aed.Ejercicio7;

public class MainEj7 {
  public static void main(String[] args) {
    Funcion f = new Funcion();
    f.cargarDesdeArchivo("src\\main\\java\\ucu\\edu\\aed\\Ejercicio7\\resources ej7\\numeros.txt");
    f.ordenar();
    f.mostrarResultados();
  }
}
