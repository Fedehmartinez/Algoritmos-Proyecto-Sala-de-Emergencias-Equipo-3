package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import ucu.edu.aed.clases.EstadoPaciente;
import ucu.edu.aed.clases.NivelUrgencia;
import ucu.edu.aed.clases.Paciente;
import ucu.edu.aed.clases.SalaEmergencia;

public class TestSalaEmergencia {

  private SalaEmergencia sala;

  @Before
  public void setUp() {
    sala = new SalaEmergencia();
  }

  @Test
  public void registrarPacienteDevuelveElPacienteConEstadoRegistrado() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    assertNotNull(p);
    assertEquals("A1", p.getId());
    assertEquals("Ana", p.getNombre());
    assertEquals(EstadoPaciente.REGISTRADO, p.getEstadoPaciente());
  }

  @Test
  public void pacienteRegistradoSePuedeBuscarPorId() {
      sala.registrarPaciente("Ana", "A1");
      Paciente encontrado = sala.buscarPaciente("A1");
      assertNotNull(encontrado);
      assertEquals("Ana", encontrado.getNombre());
  }

  @Test(expected = IllegalArgumentException.class)
  public void registrarConNombreNuloLanzaExcepcion() {
    sala.registrarPaciente(null, "A1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void registrarConIdNuloLanzaExcepcion() {
    sala.registrarPaciente("Ana", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void registrarIdDuplicadoLanzaExcepcion() {
    sala.registrarPaciente("Ana", "A1");
    sala.registrarPaciente("Beto", "A1");
  }

  @Test
  public void registrosSeMantienenOrdenadosPorIdParaLaBusquedaBinaria() {
    sala.registrarPaciente("Carla", "C3");
    sala.registrarPaciente("Ana", "A1");
    sala.registrarPaciente("Beto", "B2");
    assertEquals("Ana", sala.buscarPaciente("A1").getNombre());
    assertEquals("Beto", sala.buscarPaciente("B2").getNombre());
    assertEquals("Carla", sala.buscarPaciente("C3").getNombre());
  }

  @Test
  public void buscarPacienteInexistenteDevuelveNull() {
    assertNull(sala.buscarPaciente("NOPE"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void buscarPacienteConIdNuloLanzaExcepcion() {
    sala.buscarPaciente(null);
  }

  @Test
  public void agregarPacienteAColaLoPoneEnEspera() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    assertEquals(EstadoPaciente.EN_ESPERA, p.getEstadoPaciente());
    assertEquals(NivelUrgencia.URGENTE, p.getUrgencia());
  }

  @Test(expected = IllegalArgumentException.class)
  public void agregarPacienteNuloAColaLanzaExcepcion() {
    sala.agregarPacienteACola(null, NivelUrgencia.URGENTE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void agregarConUrgenciaNulaLanzaExcepcion() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void agregarAColaUnPacienteNoRegistradoLanzaExcepcion() {
    Paciente fantasma = new Paciente("X9", "Fantasma");
    sala.agregarPacienteACola(fantasma, NivelUrgencia.LEVE);
  }

  @Test
  public void ingresarPacienteLoPasaAConsultorio() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    sala.ingresarPaciente(p);
    assertEquals(EstadoPaciente.EN_CONSULTORIO, p.getEstadoPaciente());
    assertTrue(sala.mostrarPacientesEnConsultorios().contains("Ana"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void ingresarPacienteNuloLanzaExcepcion() {
    sala.ingresarPaciente(null);
  }

  @Test(expected = IllegalStateException.class)
  public void ingresarSinConsultoriosLibresLanzaExcepcion() {
    for (int i = 1; i <= 6; i++) {
      Paciente p = sala.registrarPaciente("P" + i, "ID" + i);
      sala.agregarPacienteACola(p, NivelUrgencia.MODERADO);
      sala.ingresarPaciente(p);
    }
}

  @Test
  public void agregarConsultorioPermiteIngresarUnoMas() {
    for (int i = 1; i <= 5; i++) {
      Paciente p = sala.registrarPaciente("P" + i, "ID" + i);
      sala.agregarPacienteACola(p, NivelUrgencia.MODERADO);
      sala.ingresarPaciente(p);
    }
    sala.agregarConsultorio();
    Paciente extra = sala.registrarPaciente("Extra", "IDX");
    sala.agregarPacienteACola(extra, NivelUrgencia.MODERADO);
    sala.ingresarPaciente(extra);
    assertEquals(EstadoPaciente.EN_CONSULTORIO, extra.getEstadoPaciente());
  }

  @Test(expected = IllegalArgumentException.class)
  public void darDeAltaConProcedimientoNuloLanzaExcepcion() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    sala.ingresarPaciente(p);
    sala.darDeAlta(0, null);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void darDeAltaEnConsultorioInexistenteLanzaExcepcion() {
    sala.darDeAlta(0, "nada"); 
  }

  @Test
  public void eliminarPacienteLoSacaDelRegistro() {
    sala.registrarPaciente("Ana", "A1");
    sala.eliminarPaciente("A1");
    assertNull(sala.buscarPaciente("A1"));
  }

  @Test(expected = NoSuchElementException.class)
  public void eliminarPacienteInexistenteLanzaExcepcion() {
    sala.eliminarPaciente("NOPE");
  }

  @Test
  public void listarPacientesVacioDevuelveMensaje() {
    assertEquals("No hay pacientes registrados", sala.listarPacientes());
  }

  @Test
  public void listarPacientesConDatosIncluyeElNombre() {
    sala.registrarPaciente("Ana", "A1");
    assertTrue(sala.listarPacientes().contains("Ana"));
  }

  @Test
  public void listarConsultasVacioDevuelveMensaje() {
    assertEquals("No hay consultas registradas", sala.listarConsultas());
  }

  @Test
  public void mostrarConsultoriosVacioDevuelveMensaje() {
    assertEquals("No hay pacientes en consultorios.", sala.mostrarPacientesEnConsultorios());
  }

  @Test
  public void toStringMuestraLosContadores() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    String s = sala.toString();
    assertTrue(s.contains("Registrados: 1"));
    assertTrue(s.contains("En espera: 1"));
    assertTrue(s.contains("En consultorio: 0"));
    assertTrue(s.contains("Consultas realizadas: 0"));
  }

  @Test
  public void elPacienteMasUrgenteEsAtendidoPrimero() {
    Paciente leve = sala.registrarPaciente("Leve", "L1");
    Paciente critico = sala.registrarPaciente("Critico", "C1");
    sala.agregarPacienteACola(leve, NivelUrgencia.LEVE);
    sala.agregarPacienteACola(critico, NivelUrgencia.CRITICO);
    
    sala.ingresarPaciente(sala.buscarPaciente("C1"));
    assertEquals(EstadoPaciente.EN_CONSULTORIO, critico.getEstadoPaciente());

    sala.darDeAlta(0, "estabilizacion");
    assertTrue(sala.listarConsultas().contains("C1"));
  }
}