package ucu.edu.aed.implementaciones;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDALista;

/**
 * Implementación del TDA Cola utilizando un vector circular.
 *
 * @param <T> el tipo de los elementos almacenados en la cola
 */
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'agregar'");
  }

  @Override
  public void agregar(int index, T elem) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'agregar'");
  }

  @Override
  public T obtener(int index) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'obtener'");
  }

  @Override
  public T remover(int index) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'remover'");
  }

  @Override
  public boolean remover(T elem) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'remover'");
  }

  @Override
  public boolean contiene(T elem) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'contiene'");
  }

  @Override
  public int indiceDe(T elem) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'indiceDe'");
  }

  @Override
  public T buscar(Predicate<T> criterio) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'buscar'");
  }

  @Override
  public TDALista<T> ordenar(Comparator<T> comparator) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'ordenar'");
  }

}