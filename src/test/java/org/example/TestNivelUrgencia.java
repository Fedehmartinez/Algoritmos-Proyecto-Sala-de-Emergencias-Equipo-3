package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ucu.edu.aed.clases.NivelUrgencia;

public class TestNivelUrgencia {

  @Test
  public void tiempoMaximoEsperaDeCadaNivelEsElEsperado() {
    assertEquals(0, NivelUrgencia.CRITICO.getTiempoMaximoEsperaMinutos());
    assertEquals(10, NivelUrgencia.URGENTE.getTiempoMaximoEsperaMinutos());
    assertEquals(60, NivelUrgencia.MODERADO.getTiempoMaximoEsperaMinutos());
    assertEquals(120, NivelUrgencia.LEVE.getTiempoMaximoEsperaMinutos());
    assertEquals(240, NivelUrgencia.NO_URGENTE.getTiempoMaximoEsperaMinutos());
  }

  @Test
  public void hayExactamenteCincoNiveles() {
    assertEquals(5, NivelUrgencia.values().length);
  }

  @Test
  public void valueOfDevuelveElNivelCorrecto() {
    assertEquals(NivelUrgencia.CRITICO, NivelUrgencia.valueOf("CRITICO"));
    assertEquals(NivelUrgencia.NO_URGENTE, NivelUrgencia.valueOf("NO_URGENTE"));
  }

  @Test
  public void masGraveTieneMenorTiempoDeEspera() {
    assertTrue(NivelUrgencia.CRITICO.getTiempoMaximoEsperaMinutos()< NivelUrgencia.URGENTE.getTiempoMaximoEsperaMinutos());
    assertTrue(NivelUrgencia.URGENTE.getTiempoMaximoEsperaMinutos()< NivelUrgencia.MODERADO.getTiempoMaximoEsperaMinutos());
    assertTrue(NivelUrgencia.MODERADO.getTiempoMaximoEsperaMinutos()< NivelUrgencia.LEVE.getTiempoMaximoEsperaMinutos());
    assertTrue(NivelUrgencia.LEVE.getTiempoMaximoEsperaMinutos()< NivelUrgencia.NO_URGENTE.getTiempoMaximoEsperaMinutos());
  }
  
}