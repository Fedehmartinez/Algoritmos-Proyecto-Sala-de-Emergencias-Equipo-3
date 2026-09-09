package org.example;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import ucu.edu.aed.clases.Episodio;
import ucu.edu.aed.clases.EstadoPaciente;
import ucu.edu.aed.clases.EventoClinico;
import ucu.edu.aed.clases.Insumo;
import ucu.edu.aed.clases.NivelCatalogo;
import ucu.edu.aed.clases.NivelUrgencia;
import ucu.edu.aed.clases.NodoCatalogo;
import ucu.edu.aed.clases.Paciente;
import ucu.edu.aed.clases.SalaEmergencia;
import ucu.edu.aed.clases.TipoEvento;
import ucu.edu.aed.tda.TDALista;

public class TestSalaEmergencia {

  private static final LocalDateTime T0 = LocalDateTime.of(2026, 9, 9, 8, 0);

  private SalaEmergencia sala;

  @Before
  public void setUp() {
    sala = new SalaEmergencia();
  }

  private EventoClinico evento(String id, String idPaciente, int minuto) {
    return new EventoClinico(id, idPaciente, TipoEvento.ESTUDIO, "x", T0.plusMinutes(minuto));
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
  public void losPacientesQuedanOrdenadosPorIdEnElArbol() {
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
  public void eliminarPacienteTambienLoSacaDeLaEspera() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    sala.eliminarPaciente("A1");
    assertEquals(0, sala.cantidadEnEspera());
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
  public void agregarPacienteAColaLoPoneEnEspera() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    assertEquals(EstadoPaciente.EN_ESPERA, p.getEstadoPaciente());
    assertEquals(NivelUrgencia.URGENTE, p.getUrgencia());
    assertEquals(1, sala.cantidadEnEspera());
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
  public void elProximoAAtenderEsElMasUrgente() {
    Paciente leve = sala.registrarPaciente("Leve", "L1");
    Paciente critico = sala.registrarPaciente("Critico", "C1");
    sala.agregarPacienteACola(leve, NivelUrgencia.LEVE);
    sala.agregarPacienteACola(critico, NivelUrgencia.CRITICO);
    assertEquals("C1", sala.proximoAAtender().getId());
  }

  @Test
  public void sinNadieEsperandoNoHayProximo() {
    assertNull(sala.proximoAAtender());
  }

  @Test
  public void cambiarPrioridadActualizaLaUrgencia() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.LEVE);
    sala.cambiarPrioridad(p, NivelUrgencia.CRITICO);
    assertEquals(NivelUrgencia.CRITICO, p.getUrgencia());
  }

  @Test
  public void cambiarPrioridadReordenaLaColaDeEspera() {
    Paciente leve = sala.registrarPaciente("Leve", "L1");
    Paciente moderado = sala.registrarPaciente("Moderado", "M1");
    sala.agregarPacienteACola(leve, NivelUrgencia.LEVE);
    sala.agregarPacienteACola(moderado, NivelUrgencia.MODERADO);

    assertEquals("M1", sala.proximoAAtender().getId());

    sala.cambiarPrioridad(leve, NivelUrgencia.CRITICO);

    assertEquals("L1", sala.proximoAAtender().getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cambiarPrioridadConPacienteNuloLanzaExcepcion() {
    sala.cambiarPrioridad(null, NivelUrgencia.CRITICO);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cambiarPrioridadConUrgenciaNulaLanzaExcepcion() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.LEVE);
    sala.cambiarPrioridad(p, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cambiarPrioridadDePacienteNoRegistradoLanzaExcepcion() {
    Paciente fantasma = new Paciente("X9", "Fantasma");
    sala.cambiarPrioridad(fantasma, NivelUrgencia.CRITICO);
  }

  @Test(expected = IllegalStateException.class)
  public void cambiarPrioridadDePacienteQueNoEstaEsperandoLanzaExcepcion() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.cambiarPrioridad(p, NivelUrgencia.CRITICO);
  }

  @Test(expected = IllegalStateException.class)
  public void cambiarPrioridadDeAlguienYaEnAtencionLanzaExcepcion() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.cambiarPrioridad(p, NivelUrgencia.CRITICO);
  }

  @Test
  public void abrirEpisodioPoneAlPacienteEnAtencion() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);

    Episodio episodio = sala.abrirEpisodio("A1", "EP1", "dolor toracico", T0);

    assertEquals(EstadoPaciente.EN_ATENCION, p.getEstadoPaciente());
    assertEquals("EP1", episodio.getIdEpisodio());
    assertTrue(p.tieneEpisodioAbierto());
    assertEquals(1, sala.cantidadEpisodios());
  }

  @Test
  public void abrirEpisodioLoSacaDeLaColaDeEspera() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    assertEquals(0, sala.cantidadEnEspera());
  }

  @Test
  public void laConsultaInicialEsLaRaizDelEpisodio() {
    sala.registrarPaciente("Ana", "A1");
    Episodio episodio = sala.abrirEpisodio("A1", "EP1", "dolor toracico", T0);
    assertEquals("EP1-E0", episodio.getConsultaInicial().getIdEvento());
    assertEquals(TipoEvento.CONSULTA_INICIAL, episodio.getConsultaInicial().getTipo());
    assertEquals("dolor toracico", episodio.getConsultaInicial().getDescripcion());
  }

  @Test(expected = NoSuchElementException.class)
  public void abrirEpisodioAUnNoRegistradoLanzaExcepcion() {
    sala.abrirEpisodio("NOPE", "EP1", "x", T0);
  }

  @Test(expected = IllegalStateException.class)
  public void unPacienteNoPuedeTenerDosEpisodiosAbiertos() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "x", T0);
    sala.abrirEpisodio("A1", "EP2", "y", T0.plusMinutes(10));
  }

  @Test
  public void sePuedenAbrirTodosLosEpisodiosSimultaneosQueHagaFalta() {
    for (int i = 1; i <= 20; i++) {
      sala.registrarPaciente("P" + i, "ID" + i);
      sala.abrirEpisodio("ID" + i, "EP" + i, "x", T0.plusMinutes(i));
    }
    assertEquals(20, sala.cantidadEpisodios());
  }

  @Test
  public void atenderSiguienteTomaAlMasUrgente() {
    Paciente leve = sala.registrarPaciente("Leve", "L1");
    Paciente critico = sala.registrarPaciente("Critico", "C1");
    sala.agregarPacienteACola(leve, NivelUrgencia.LEVE);
    sala.agregarPacienteACola(critico, NivelUrgencia.CRITICO);

    Episodio episodio = sala.atenderSiguiente("EP1", "urgencia", T0);

    assertEquals("C1", episodio.getPaciente().getId());
    assertEquals(EstadoPaciente.EN_ATENCION, critico.getEstadoPaciente());
    assertEquals(EstadoPaciente.EN_ESPERA, leve.getEstadoPaciente());
  }

  @Test(expected = NoSuchElementException.class)
  public void atenderSiguienteSinNadieEsperandoLanzaExcepcion() {
    sala.atenderSiguiente("EP1", "x", T0);
  }

  @Test
  public void registrarEventoLoCuelgaDelEpisodioEnCurso() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    assertTrue(sala.registrarEvento("A1", "EP1-E0", evento("A2", "A1", 10)));
  }

  @Test(expected = IllegalStateException.class)
  public void registrarEventoSinEpisodioAbiertoLanzaExcepcion() {
    sala.registrarPaciente("Ana", "A1");
    sala.registrarEvento("A1", "X", evento("A2", "A1", 10));
  }

  @Test
  public void registrarEventoArmandoloEnLaSala() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    assertTrue(sala.registrarEvento("A1", "EP1-E0",
        new EventoClinico("E1", "A1", TipoEvento.ESTUDIO, "electrocardiograma", T0.plusMinutes(10))));
  }

  @Test(expected = IllegalStateException.class)
  public void noSePuedenColgarEventosDeUnEventoYaCerrado() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("E1", "A1", 10));
    sala.cerrarEvento("A1", "E1", T0.plusMinutes(20));
    sala.registrarEvento("A1", "E1", evento("E2", "A1", 30));
  }

  @Test
  public void agregarInsumoYConsultarCostos() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("E1", "A1", 10));

    sala.agregarInsumo("A1", "E1", new Insumo("cateter", 8000.0, 1));
    sala.agregarInsumo("A1", "EP1-E0", new Insumo("gasas", 25.5, 10));

    assertEquals(8000.0, sala.costoDe("A1", "E1"), 0.0001);
    assertEquals(8255.0, sala.costoDelEpisodioEnCurso("A1"), 0.0001);
  }

  @Test(expected = IllegalStateException.class)
  public void unEventoCerradoNoAdmiteMasInsumos() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("E1", "A1", 10));
    sala.cerrarEvento("A1", "E1", T0.plusMinutes(20));
    sala.agregarInsumo("A1", "E1", new Insumo("gasas", 10.0, 1));
  }

  @Test
  public void consultarTiemposDelEpisodioYDeUnaParte() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("E1", "A1", 10));
    sala.registrarEvento("A1", "E1", evento("E2", "A1", 20));

    assertEquals(0L, sala.episodioEnCursoDe("A1").duracionAcumuladaMinutos("E1"));

    sala.cerrarEvento("A1", "E2", T0.plusMinutes(50));
    sala.cerrarEvento("A1", "E1", T0.plusMinutes(60));

    assertEquals(30L, sala.episodioEnCursoDe("A1").duracionAcumuladaMinutos("E2"));
    assertEquals(80L, sala.episodioEnCursoDe("A1").duracionAcumuladaMinutos("E1"));
  }

  @Test
  public void cerrarEventoDesdeLaSala() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("E1", "A1", 10));

    assertTrue(sala.cerrarEvento("A1", "E1", T0.plusMinutes(20)));
    assertTrue(sala.queImpideCerrarEpisodioDe("A1").esVacio());
  }

  @Test(expected = IllegalStateException.class)
  public void cerrarUnEventoConDerivacionesAbiertasLanzaExcepcion() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("E1", "A1", 10));
    sala.registrarEvento("A1", "E1", evento("E2", "A1", 20));
    sala.cerrarEvento("A1", "E1", T0.plusMinutes(30));
  }

  @Test
  public void queImpideCerrarUnEventoPuntual() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("E1", "A1", 10));
    sala.registrarEvento("A1", "E1", evento("E2", "A1", 20));

    TDALista<EventoClinico> bloqueantes = sala.queImpideCerrarEvento("A1", "E1");
    assertEquals(1, bloqueantes.tamaño());
    assertEquals("E2", bloqueantes.obtener(0).getIdEvento());
  }

  @Test
  public void cicloCompletoDeUnaAtencion() {
    sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(sala.buscarPaciente("A1"), NivelUrgencia.CRITICO);

    sala.atenderSiguiente("EP1", "dolor toracico", T0);
    sala.registrarEvento("A1", "EP1-E0",
        new EventoClinico("E1", "A1", TipoEvento.ESTUDIO, "ecg", T0.plusMinutes(10)));
    sala.registrarEvento("A1", "E1",
        new EventoClinico("E2", "A1", TipoEvento.PROCEDIMIENTO, "cateter", T0.plusMinutes(30)));
    sala.agregarInsumo("A1", "E2", new Insumo("cateter", 8000.0, 1));

    sala.cerrarEvento("A1", "E2", T0.plusMinutes(60));
    sala.cerrarEvento("A1", "E1", T0.plusMinutes(70));
    Episodio cerrado = sala.cerrarEpisodio("A1", T0.plusMinutes(80));

    assertFalse(cerrado.estaAbierto());
    assertEquals(8000.0, cerrado.costoTotal(), 0.0001);
    assertEquals(EstadoPaciente.ATENDIDO, sala.buscarPaciente("A1").getEstadoPaciente());
    assertEquals(0, sala.cantidadEnEspera());
  }

  @Test
  public void queImpideCerrarListaLosEventosAbiertos() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("A2", "A1", 10));

    TDALista<EventoClinico> bloqueantes = sala.queImpideCerrarEpisodioDe("A1");
    assertEquals(1, bloqueantes.tamaño());
    assertEquals("A2", bloqueantes.obtener(0).getIdEvento());
  }

  @Test(expected = IllegalStateException.class)
  public void cerrarEpisodioConEventosAbiertosLanzaExcepcion() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.registrarEvento("A1", "EP1-E0", evento("A2", "A1", 10));
    sala.cerrarEpisodio("A1", T0.plusMinutes(60));
  }

  @Test
  public void cerrarEpisodioDejaAlPacienteAtendido() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);

    Episodio cerrado = sala.cerrarEpisodio("A1", T0.plusMinutes(60));

    assertEquals(EstadoPaciente.ATENDIDO, p.getEstadoPaciente());
    assertFalse(p.tieneEpisodioAbierto());
    assertFalse(cerrado.estaAbierto());
    assertEquals(T0.plusMinutes(60), cerrado.getFechaCierre());
  }

  @Test
  public void elEpisodioCerradoSigueEnLaHistoriaDelPaciente() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.cerrarEpisodio("A1", T0.plusMinutes(60));

    assertEquals(1, p.cantidadEpisodios());
    assertEquals(1, sala.cantidadEpisodios());
    assertNotNull(p.buscarEpisodio("EP1"));
  }

  @Test
  public void despuesDeCerrarSePuedeAbrirOtroEpisodio() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.cerrarEpisodio("A1", T0.plusMinutes(60));
    sala.abrirEpisodio("A1", "EP2", "otra cosa", T0.plusDays(1));

    assertEquals(2, sala.buscarPaciente("A1").cantidadEpisodios());
  }

  @Test(expected = IllegalStateException.class)
  public void episodioEnCursoSinEpisodioAbiertoLanzaExcepcion() {
    sala.registrarPaciente("Ana", "A1");
    sala.episodioEnCursoDe("A1");
  }

  @Test
  public void episodiosEnRangoDevuelveSoloLosDelPeriodo() {
    sala.registrarPaciente("Ana", "A1");
    for (int dia = 0; dia < 10; dia++) {
      sala.abrirEpisodio("A1", "EP" + dia, "x", T0.plusDays(dia));
      sala.cerrarEpisodio("A1", T0.plusDays(dia).plusHours(1));
    }
    TDALista<Episodio> enRango = sala.episodiosEnRango(T0.plusDays(3), T0.plusDays(5));
    assertEquals(3, enRango.tamaño());
    assertEquals("EP3", enRango.obtener(0).getIdEpisodio());
    assertEquals("EP5", enRango.obtener(2).getIdEpisodio());
  }

  @Test
  public void episodiosEnRangoIncluyeLosExtremos() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "x", T0);
    sala.cerrarEpisodio("A1", T0.plusHours(1));
    assertEquals(1, sala.episodiosEnRango(T0, T0).tamaño());
  }

  @Test
  public void episodiosEnRangoVacioDevuelveListaVacia() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "x", T0);
    assertTrue(sala.episodiosEnRango(T0.plusDays(100), T0.plusDays(200)).esVacio());
  }

  @Test
  public void episodiosEnRangoSinCotasDevuelveTodos() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "x", T0);
    assertEquals(1, sala.episodiosEnRango(null, null).tamaño());
  }

  @Test
  public void episodiosDeUnPacienteNoTraenLosDeOtro() {
    sala.registrarPaciente("Ana", "A1");
    sala.registrarPaciente("Beto", "B1");
    sala.abrirEpisodio("A1", "EPA", "x", T0);
    sala.abrirEpisodio("B1", "EPB", "y", T0.plusMinutes(1));

    TDALista<Episodio> deAna = sala.episodiosEnRango("A1", null, null);
    assertEquals(1, deAna.tamaño());
    assertEquals("EPA", deAna.obtener(0).getIdEpisodio());

    assertEquals(2, sala.episodiosEnRango(null, null).tamaño());
  }

  @Test(expected = NoSuchElementException.class)
  public void episodiosDeUnPacienteInexistenteLanzaExcepcion() {
    sala.episodiosEnRango("NOPE", null, null);
  }

  @Test
  public void listarEpisodiosVacioDevuelveMensaje() {
    assertEquals("No hay episodios registrados", sala.listarEpisodios());
  }

  @Test
  public void toStringMuestraLosContadores() {
    Paciente p = sala.registrarPaciente("Ana", "A1");
    sala.agregarPacienteACola(p, NivelUrgencia.URGENTE);
    String s = sala.toString();
    assertTrue(s.contains("Registrados: 1"));
    assertTrue(s.contains("En espera: 1"));
    assertTrue(s.contains("Episodios: 0"));
    assertTrue(s.contains("Eventos: 0"));
    assertTrue(s.contains("Codigos en catalogo: 0"));
  }

  @Test
  public void eventosEnRangoDevuelveLosDeTodosLosEpisodiosYPacientes() {
    sala.registrarPaciente("Ana", "A1");
    sala.registrarPaciente("Beto", "B1");
    sala.abrirEpisodio("A1", "EPA", "dolor", T0);
    sala.abrirEpisodio("B1", "EPB", "fiebre", T0.plusMinutes(5));
    sala.registrarEvento("A1", "EPA-E0", evento("EA1", "A1", 10));

    TDALista<EventoClinico> enRango = sala.eventosEnRango(T0, T0.plusMinutes(10));
    assertEquals(3, enRango.tamaño());
  }

  @Test
  public void eventosEnRangoRespetaLosExtremos() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EPA", "dolor", T0);
    sala.registrarEvento("A1", "EPA-E0", evento("EA1", "A1", 30));

    assertEquals(1, sala.eventosEnRango(T0, T0).tamaño());
    assertEquals(2, sala.eventosEnRango(null, null).tamaño());
    assertTrue(sala.eventosEnRango(T0.plusMinutes(100), T0.plusMinutes(200)).esVacio());
  }

  @Test
  public void cantidadEventosCuentaTodosLosEventosDeLaSala() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EPA", "dolor", T0);
    sala.registrarEvento("A1", "EPA-E0", evento("EA1", "A1", 10));
    sala.registrarEvento("A1", "EA1",
        new EventoClinico("EA2", "A1", TipoEvento.PROCEDIMIENTO, "cateter", T0.plusMinutes(20)));

    assertEquals(3, sala.cantidadEventos());
  }

  private void armarCatalogo() {
    sala.agregarCapituloDiagnostico("I", "Enfermedades del sistema circulatorio");
    sala.agregarGrupoDiagnostico("I", "I20-I25", "Cardiopatias isquemicas");
    sala.agregarCodigoAlCatalogo("I20-I25", "I21", "Infarto agudo de miocardio");
  }

  @Test
  public void agregarAlCatalogoLoDejaBuscable() {
    armarCatalogo();
    NodoCatalogo codigo = sala.buscarEnCatalogo("I21");
    assertNotNull(codigo);
    assertEquals("Infarto agudo de miocardio", codigo.getNombre());
    assertEquals(NivelCatalogo.CODIGO, codigo.getNivel());
  }

  @Test
  public void codigosDelCatalogoPorCapituloTraeTodosSusCodigos() {
    armarCatalogo();
    sala.agregarCodigoAlCatalogo("I20-I25", "I25", "Cardiopatia isquemica cronica");
    TDALista<NodoCatalogo> codigos = sala.codigosDelCatalogo("I");
    assertEquals(2, codigos.tamaño());
  }

  @Test
  public void toStringCuentaLosCodigosDelCatalogo() {
    armarCatalogo();
    assertTrue(sala.toString().contains("Codigos en catalogo: 3"));
  }

  @Test
  public void diagnosticarAsociaElCodigoDelCatalogoAlEvento() {
    armarCatalogo();
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor toracico", T0);

    sala.diagnosticar("A1", "EP1-E0", "I21");

    TDALista<String> codigos = sala.episodioEnCursoDe("A1").codigos();
    assertEquals(1, codigos.tamaño());
    assertEquals("I21", codigos.obtener(0));
  }

  @Test(expected = NoSuchElementException.class)
  public void diagnosticarConCodigoInexistenteLanzaExcepcion() {
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.diagnosticar("A1", "EP1-E0", "NOPE");
  }

  @Test(expected = IllegalArgumentException.class)
  public void diagnosticarConUnCapituloEnVezDeUnCodigoLanzaExcepcion() {
    armarCatalogo();
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.diagnosticar("A1", "EP1-E0", "I");
  }

  @Test(expected = IllegalArgumentException.class)
  public void diagnosticarConUnGrupoEnVezDeUnCodigoLanzaExcepcion() {
    armarCatalogo();
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.diagnosticar("A1", "EP1-E0", "I20-I25");
  }

  @Test(expected = NoSuchElementException.class)
  public void diagnosticarUnEventoInexistenteLanzaExcepcion() {
    armarCatalogo();
    sala.registrarPaciente("Ana", "A1");
    sala.abrirEpisodio("A1", "EP1", "dolor", T0);
    sala.diagnosticar("A1", "NOPE", "I21");
  }

  @Test(expected = IllegalStateException.class)
  public void diagnosticarSinEpisodioAbiertoLanzaExcepcion() {
    armarCatalogo();
    sala.registrarPaciente("Ana", "A1");
    sala.diagnosticar("A1", "EP1-E0", "I21");
  }
}
