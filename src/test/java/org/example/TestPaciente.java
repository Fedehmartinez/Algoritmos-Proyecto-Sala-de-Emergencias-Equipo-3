package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ucu.edu.aed.clases.EstadoPaciente;
import ucu.edu.aed.clases.NivelUrgencia;
import ucu.edu.aed.clases.Paciente;

public class TestPaciente {

  @Test
  public void constructorInicializaIdNombreEstadoYUrgencia() {
    Paciente p = new Paciente("A1", "Ana");
    assertEquals("A1", p.getId());
    assertEquals("Ana", p.getNombre());
    assertNull(p.getUrgencia());                       
    assertEquals(EstadoPaciente.REGISTRADO, p.getEstadoPaciente());
  }

  @Test
  public void caracteristicasArrancaVacia() {
    Paciente p = new Paciente("A1", "Ana");
    assertTrue(p.getCaracteristicas().esVacio());
  }

  @Test
  public void setUrgenciaCambiaLaUrgencia() {
    Paciente p = new Paciente("A1", "Ana");
    p.setUrgencia(NivelUrgencia.CRITICO);
    assertEquals(NivelUrgencia.CRITICO, p.getUrgencia());
  }

  @Test
  public void setEstadoPacienteCambiaElEstado() {
    Paciente p = new Paciente("A1", "Ana");
    p.setEstadoPaciente(EstadoPaciente.EN_ESPERA);
    assertEquals(EstadoPaciente.EN_ESPERA, p.getEstadoPaciente());
  }

  @Test
  public void agregarCaracteristicaLaGuarda() {
    Paciente p = new Paciente("A1", "Ana");
    p.agregarCaracteristica("fiebre");
    p.agregarCaracteristica("tos");
    assertEquals(2, p.getCaracteristicas().tamaño());
    assertTrue(p.getCaracteristicas().contiene("fiebre"));
    assertTrue(p.getCaracteristicas().contiene("tos"));
  }

  @Test
  public void eliminarCaracteristicaExistenteDevuelveTrue() {
    Paciente p = new Paciente("A1", "Ana");
    p.agregarCaracteristica("fiebre");
    assertTrue(p.eliminarCaracteristica("fiebre"));
    assertFalse(p.getCaracteristicas().contiene("fiebre"));
  }

  @Test
  public void eliminarCaracteristicaInexistenteDevuelveFalse() {
    Paciente p = new Paciente("A1", "Ana");
    assertFalse(p.eliminarCaracteristica("fiebre"));
  }

  @Test
  public void equalsComparaSoloPorId() {
    Paciente p1 = new Paciente("A1", "Ana");
    Paciente p2 = new Paciente("A1", "Beto");
    Paciente p3 = new Paciente("A2", "Ana");
    assertTrue(p1.equals(p2));
    assertFalse(p1.equals(p3));
  }

  @Test
  public void equalsConsigoMismoEsTrueYConOtroTipoEsFalse() {
    Paciente p = new Paciente("A1", "Ana");
    assertTrue(p.equals(p));
    assertFalse(p.equals("A1"));
    assertFalse(p.equals(null));
  }

  @Test
  public void hashCodeEsElDelId() {
    Paciente p = new Paciente("A1", "Ana");
    assertEquals("A1".hashCode(), p.hashCode());
  }

  @Test
  public void toStringMuestraSinClasificarCuandoNoHayUrgencia() {
    Paciente p = new Paciente("A1", "Ana");
    String s = p.toString();
    assertTrue(s.contains("Ana"));
    assertTrue(s.contains("A1"));
    assertTrue(s.contains("sin clasificar"));
    assertTrue(s.contains("ninguna"));
  }

  @Test
  public void toStringMuestraUrgenciaYCaracteristicasCuandoLasHay() {
    Paciente p = new Paciente("A1", "Ana");
    p.setUrgencia(NivelUrgencia.URGENTE);
    p.agregarCaracteristica("fiebre");
    String s = p.toString();
    assertTrue(s.contains("URGENTE"));
    assertTrue(s.contains("fiebre"));
    assertFalse(s.contains("sin clasificar"));
  }
}