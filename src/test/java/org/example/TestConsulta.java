package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import ucu.edu.aed.clases.Consulta;
import ucu.edu.aed.clases.NivelUrgencia;

public class TestConsulta {

  @Test
  public void guardaLosDatosPasadosAlConstructor() {
    Consulta c = new Consulta("A1", NivelUrgencia.CRITICO, "sutura");
    assertEquals("A1", c.getIdPaciente());
    assertEquals(NivelUrgencia.CRITICO, c.getUrgencia());
    assertEquals("sutura", c.getProcedimiento());
  }

  @Test
  public void fechaSeAsignaAutomaticamenteYNoEsNula() {
    LocalDateTime antes = LocalDateTime.now();
    Consulta c = new Consulta("A1", NivelUrgencia.LEVE, "control");
    LocalDateTime despues = LocalDateTime.now();
    assertNotNull(c.getFecha());
    assertTrue(!c.getFecha().isBefore(antes));
    assertTrue(!c.getFecha().isAfter(despues));
  }

  @Test
  public void toStringIncluyeIdUrgenciaYProcedimiento() {
    Consulta c = new Consulta("A1", NivelUrgencia.URGENTE, "radiografia");
    String s = c.toString();
    assertTrue(s.contains("A1"));
    assertTrue(s.contains("URGENTE"));
    assertTrue(s.contains("radiografia"));
  }
}