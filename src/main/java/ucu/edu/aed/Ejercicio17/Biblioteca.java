package ucu.edu.aed.Ejercicio17;

import java.util.Comparator;
import java.util.function.Predicate;

import ucu.edu.aed.implementaciones.ListaEnlazada;
import ucu.edu.aed.tda.TDALista;

public class Biblioteca {

    private ListaEnlazada<Libro> catalogo;

    public Biblioteca(){
        catalogo = new ListaEnlazada<>();
    }

    public boolean incorporarLibro(Libro libro){
        if (catalogo.contiene(libro)){
            return false;
        }
        else{
            catalogo.agregar(libro);
            return true;
        }
    }

    public boolean agregarEjemplares(String codigo, int cantidad){
        Libro libro = buscarPorCodigo(codigo);
        if (libro == null){
            return false;
        }
        else{
            libro.agregarEjemplares(cantidad);
            return true;
        }
    }

    public boolean registrarPrestamo(String codigo){
        Libro libro = buscarPorCodigo(codigo);
        if (libro == null){
            return false;
        }
        else{
            return libro.prestar();
        }
    }

    public boolean registrarDevolucion(String codigo){
        Libro libro = buscarPorCodigo(codigo);
        if (libro == null){
            return false;
        }
        else{
            libro.devolver();
            return true;
        }
    }

    public boolean retirarLibro(String codigo){
        Libro libro = buscarPorCodigo(codigo);
        if (libro == null){
            return false;
        }
        else{
            return catalogo.remover(libro);
        }
    }

    public int consultarExistencias(String codigo){
        Libro libro = buscarPorCodigo(codigo);
        if (libro == null){
            return -1;
        }
        else{
            return libro.getCantidadDisponible();
        }
    }

    public TDALista<Libro> listarPorTitulo(){
        return catalogo.ordenar(new Comparator<Libro>(){
            @Override
            public int compare(Libro a, Libro b){
                return a.getTitulo().compareTo(b.getTitulo());
            }
        });
    }

    private Libro buscarPorCodigo(final String codigo){
        return catalogo.buscar(new Predicate<Libro>(){
            @Override
            public boolean test(Libro libro){
                return libro.getCodigo().equals(codigo);
            }
        });
    }
}