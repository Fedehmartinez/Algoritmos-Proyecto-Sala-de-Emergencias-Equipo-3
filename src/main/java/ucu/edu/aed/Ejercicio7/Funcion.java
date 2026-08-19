package ucu.edu.aed.Ejercicio7;

import ucu.edu.aed.utils.FileUtils;

public class Funcion {
  private int n;
  private int[] arreglo;
  private int indiceCarga; // para saber en qué línea vamos mientras leemos
  private int contadorSi; // cuenta invocaciones al "si"
  private int intercambios;

  public void cargarDesdeArchivo(String path) {
    indiceCarga = 0; // arrancamos en la línea 0

    FileUtils.leerLineas(path, linea -> {
      if (indiceCarga == 0) {
        // primera linea: es N
        n = Integer.parseInt(linea.trim());
        arreglo = new int[n];
      } else {
        // líneas siguientes: son los elementos del arreglo
        arreglo[indiceCarga - 1] = Integer.parseInt(linea.trim());
      }
      indiceCarga++;
    });
  }

  public void ordenar() { // basicamente el algoritmo del ej. 6
    for (int i = 1; i <= n - 1; i++) {
      for (int j = n - 1; j >= i; j--) {
        contadorSi++;
        if (arreglo[j] < arreglo[j - 1]) {
          int temp = arreglo[j];
          arreglo[j] = arreglo[j - 1];
          arreglo[j - 1] = temp;
          intercambios++;
        }
      }
    }
  }

  public void mostrarResultados() {
    System.out.println("N = " + n);
    System.out.println("Contador de invocaciones al Si = " + contadorSi);
    System.out.println("Cantidad de intercambios = " + intercambios);
    System.out.println("Primer elemento = " + arreglo[0]);
    System.out.println("Último elemento = " + arreglo[n - 1]);
  }
}
