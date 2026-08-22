package ucu.edu.aed.implementaciones;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDALista;

public class ColaCircular<T> implements TDACola<T> {

  private Object[] vector;
  private int front;
  private int rear;
  private int cantidad;
  private int capacidad;

  public ColaCircular() {
    this(10);
  }

  public ColaCircular(int capacidad) {
    this.capacidad = capacidad;
    this.vector = new Object[capacidad];

    this.front = 0;
    this.rear = 0;
    this.cantidad = 0;
  }

  private int fisico(int indiceLogico) {
    return (front + indiceLogico) % capacidad;
  }

  private void asegurarCapacidad() {
    if (cantidad == capacidad) {
      Object[] nuevo = new Object[capacidad * 2];
      for (int i = 0; i < cantidad; i++) {
        nuevo[i] = vector[fisico(i)];
      }
      vector = nuevo;
      capacidad = capacidad * 2;
      front = 0;
      rear = cantidad;
    }
  }

  @Override
  public boolean poneEnCola(T dato) {
    if (cantidad == capacidad) {
      return false;
    }
    vector[rear] = dato;
    rear = (rear + 1) % capacidad;
    cantidad++;
    return true;
  }

  @Override
  public T frente() {
    if (esVacio()) {
      throw new NoSuchElementException("La cola está vacía");
    }
    return (T) vector[front];
  }

  @Override
  public T quitaDeCola() {
    if (esVacio()) {
      throw new NoSuchElementException("La cola está vacía");
    }
    T dato = (T) vector[front];
    vector[front] = null;
    front = (front + 1) % capacidad;
    cantidad--;
    return dato;
  }

  @Override
  public boolean esVacio() {
    return cantidad == 0;
  }

  @Override
  public int tamaño() {
    return cantidad;
  }

  @Override
  public void vaciar() {
    Arrays.fill(vector, null);
    front = 0;
    rear = 0;
    cantidad = 0;
  }

  @Override
  public void agregar(T elem) {
    asegurarCapacidad();
    vector[rear] = elem;
    rear = (rear + 1) % capacidad;
    cantidad++;
  }

  @Override
  public void agregar(int index, T elem) {
    if (index < 0 || index > cantidad) {
      throw new IndexOutOfBoundsException();
    }
    asegurarCapacidad();
    int cantidadPrevia = cantidad;
    for (int i = cantidadPrevia; i > index; i--) {
      vector[fisico(i)] = vector[fisico(i - 1)];
    }
    vector[fisico(index)] = elem;
    cantidad++;
    rear = fisico(cantidad);
  }

  @Override
  public T obtener(int index) {
    if (index < 0 || index >= cantidad) {
      throw new IndexOutOfBoundsException();
    }
    return (T) vector[fisico(index)];
  }

  @Override
  public T remover(int index) {
    if (index < 0 || index >= cantidad) {
      throw new IndexOutOfBoundsException();
    }
    T removido = (T) vector[fisico(index)];
    for (int i = index; i < cantidad - 1; i++) {
      vector[fisico(i)] = vector[fisico(i + 1)];
    }
    cantidad--;
    vector[fisico(cantidad)] = null;
    rear = fisico(cantidad);
    return removido;
  }

  @Override
  public boolean remover(T elem) {
    int index = indiceDe(elem);
    if (index == -1) {
      return false;
    }
    remover(index);
    return true;
  }

  @Override
  public boolean contiene(T elem) {
    return indiceDe(elem) != -1;
  }

  @Override
  public int indiceDe(T elem) {
    for (int i = 0; i < cantidad; i++) {
      if (((T) vector[fisico(i)]).equals(elem)) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public T buscar(Predicate<T> criterio) {
    for (int i = 0; i < cantidad; i++) {
      T dato = (T) vector[fisico(i)];
      if (criterio.test(dato)) {
        return dato;
      }
    }
    return null;
  }

  @Override
  public TDALista<T> ordenar(Comparator<T> comparator) {
    ColaCircular<T> resultado = new ColaCircular<>(Math.max(capacidad, 1));
    for (int i = 0; i < cantidad; i++) {
      T dato = (T) vector[fisico(i)];
      int pos = 0;
      while (pos < resultado.cantidad && comparator.compare(dato, resultado.obtener(pos)) >= 0) {
        pos++;
      }
      resultado.agregar(pos, dato);
    }
    return resultado;
  }

}
