package org.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ucu.edu.aed.clases.EstadoPaciente;

public class TestEstadoPaciente {

  @Test
  public void tieneLosCuatroEstadosEsperados() {
    assertEquals(4, EstadoPaciente.values().length);
  }

  @Test
  public void valueOfDevuelveElEstadoCorrecto() {
    assertEquals(EstadoPaciente.REGISTRADO, EstadoPaciente.valueOf("REGISTRADO"));
    assertEquals(EstadoPaciente.EN_ESPERA, EstadoPaciente.valueOf("EN_ESPERA"));
    assertEquals(EstadoPaciente.EN_CONSULTORIO, EstadoPaciente.valueOf("EN_CONSULTORIO"));
    assertEquals(EstadoPaciente.ATENDIDO, EstadoPaciente.valueOf("ATENDIDO"));
  }

  @Test
  public void elOrdenDeDeclaracionEsElEsperado() {
    EstadoPaciente[] v = EstadoPaciente.values();
    assertEquals(EstadoPaciente.REGISTRADO, v[0]);
    assertEquals(EstadoPaciente.EN_ESPERA, v[1]);
    assertEquals(EstadoPaciente.EN_CONSULTORIO, v[2]);
    assertEquals(EstadoPaciente.ATENDIDO, v[3]);
  }
}