package org.example;

import java.time.LocalDateTime;

import junit.framework.TestCase;

import ucu.edu.aed.clases.EstadoActual;
import ucu.edu.aed.clases.EventoClinico;
import ucu.edu.aed.clases.Insumo;
import ucu.edu.aed.clases.TipoEvento;
import ucu.edu.aed.implementaciones.AVLImpl;

public class EventoClinicoTest extends TestCase {

    private static final LocalDateTime T0 = LocalDateTime.of(2026, 9, 9, 8, 0);

    private EventoClinico evento;

    protected void setUp(){
        evento = new EventoClinico("E1", "P1", TipoEvento.ESTUDIO, "radiografia", T0);
    }

    private EventoClinico crear(String id, int minuto){
        return new EventoClinico(id, "P1", TipoEvento.ESTUDIO, "x", T0.plusMinutes(minuto));
    }

    public void testEventoNuevoQuedaAbierto(){
        assertEquals(EstadoActual.ABIERTO, evento.getEstado());
        assertTrue(evento.estaAbierto());
        assertNull(evento.getFechaCierre());
    }

    public void testGuardaLoQueSeLePaso(){
        assertEquals("E1", evento.getIdEvento());
        assertEquals("P1", evento.getIdPaciente());
        assertEquals(TipoEvento.ESTUDIO, evento.getTipo());
        assertEquals("radiografia", evento.getDescripcion());
        assertEquals(T0, evento.getFecha());
    }

    public void testDescripcionNulaQuedaVacia(){
        EventoClinico sinDescripcion =
                new EventoClinico("E2", "P1", TipoEvento.ESTUDIO, null, T0);
        assertEquals("", sinDescripcion.getDescripcion());
    }

    public void testIdVacioNoSePermite(){
        try {
            new EventoClinico("  ", "P1", TipoEvento.ESTUDIO, "x", T0);
            fail("deberia rechazar un id vacio");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testEventoSinPacienteNoSePermite(){
        try {
            new EventoClinico("E1", null, TipoEvento.ESTUDIO, "x", T0);
            fail("deberia rechazar un evento sin paciente");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testEventoSinTipoNiFechaNoSePermite(){
        try {
            new EventoClinico("E1", "P1", null, "x", T0);
            fail("deberia rechazar un evento sin tipo");
        } catch (IllegalArgumentException esperada){

        }
        try {
            new EventoClinico("E1", "P1", TipoEvento.ESTUDIO, "x", null);
            fail("deberia rechazar un evento sin fecha");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testEventoNuevoNoTieneCosto(){
        assertEquals(0.0, evento.costoInsumos(), 0.0001);
        assertTrue(evento.getInsumos().esVacio());
    }

    public void testCostoEsLaSumaDeLosInsumos(){
        evento.agregarInsumo(new Insumo("gasas", 25.5, 10));
        evento.agregarInsumo(new Insumo("suero", 100.0, 2));
        assertEquals(455.0, evento.costoInsumos(), 0.0001);
        assertEquals(2, evento.getInsumos().tamaño());
    }

    public void testInsumoNuloNoSePermite(){
        try {
            evento.agregarInsumo(null);
            fail("deberia rechazar un insumo nulo");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testElMismoInsumoDosVecesSumaDosVeces(){

        evento.agregarInsumo(new Insumo("gasas", 10.0, 1));
        evento.agregarInsumo(new Insumo("gasas", 10.0, 1));
        assertEquals(20.0, evento.costoInsumos(), 0.0001);
    }

    public void testAgregarCodigoDeDiagnostico(){
        evento.agregarCodigoDiagnostico("I21");
        assertEquals(1, evento.getCodigosDiagnostico().tamaño());
    }

    public void testElMismoCodigoNoSeAgregaDosVeces(){
        evento.agregarCodigoDiagnostico("I21");
        evento.agregarCodigoDiagnostico("I21");
        assertEquals(1, evento.getCodigosDiagnostico().tamaño());
    }

    public void testCodigoNuloNoSePermite(){
        try {
            evento.agregarCodigoDiagnostico(null);
            fail("deberia rechazar un codigo nulo");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testEventoAbiertoNoTieneDuracion(){
        assertEquals(-1L, evento.duracionMinutos());
    }

    public void testCerrarDejaEstadoFechaYDuracion(){
        assertTrue(evento.cerrar(T0.plusMinutes(45)));
        assertEquals(EstadoActual.CERRADO, evento.getEstado());
        assertFalse(evento.estaAbierto());
        assertEquals(T0.plusMinutes(45), evento.getFechaCierre());
        assertEquals(45L, evento.duracionMinutos());
    }

    public void testCerrarDosVecesDevuelveFalse(){
        assertTrue(evento.cerrar(T0.plusMinutes(10)));
        assertFalse(evento.cerrar(T0.plusMinutes(20)));

        assertEquals(T0.plusMinutes(10), evento.getFechaCierre());
    }

    public void testNoSePuedeCerrarAntesDeAbrir(){
        try {
            evento.cerrar(T0.minusMinutes(1));
            fail("el cierre no puede ser anterior a la apertura");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testCerrarEnElMismoInstanteDaDuracionCero(){
        assertTrue(evento.cerrar(T0));
        assertEquals(0L, evento.duracionMinutos());
    }

    public void testOrdenaPorFecha(){
        EventoClinico temprano = crear("E1", 0);
        EventoClinico tardio = crear("E2", 60);
        assertTrue(temprano.compareTo(tardio) < 0);
        assertTrue(tardio.compareTo(temprano) > 0);
    }

    public void testMismaFechaDesempataPorId(){
        EventoClinico a = crear("A", 5);
        EventoClinico b = crear("B", 5);
        assertEquals(a.getFecha(), b.getFecha());
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    public void testCompareToCeroSoloConsigoMismo(){
        EventoClinico a = crear("A", 5);
        assertEquals(0, a.compareTo(crear("A", 5)));
    }

    public void testDosEventosSimultaneosConvivenEnElAvl(){
        AVLImpl<EventoClinico> global = new AVLImpl<>();
        assertTrue(global.insertar(crear("X1", 5)));
        assertTrue(global.insertar(crear("X2", 5)));
        assertEquals(2, global.cantidadNodos());
    }

    public void testPorIdEncuentraPorIgualdad(){
        assertEquals(0, EventoClinico.porId("E1").compareTo(evento));
        assertTrue(EventoClinico.porId("E9").compareTo(evento) != 0);
    }

    public void testPorFechaIgnoraElId(){

        assertEquals(0, EventoClinico.porFecha(T0).compareTo(evento));
        assertTrue(EventoClinico.porFecha(T0.minusMinutes(1)).compareTo(evento) < 0);
        assertTrue(EventoClinico.porFecha(T0.plusMinutes(1)).compareTo(evento) > 0);
    }

    public void testCriteriosNulosNoSePermiten(){
        try {
            EventoClinico.porId(null);
            fail("deberia rechazar un id nulo");
        } catch (IllegalArgumentException esperada){

        }
        try {
            EventoClinico.porFecha(null);
            fail("deberia rechazar una fecha nula");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testIgualdadEsPorId(){
        assertEquals(evento, new EventoClinico("E1", "P9", TipoEvento.COMPLICACION, "otra", T0.plusDays(1)));
        assertFalse(evento.equals(crear("E2", 0)));
        assertEquals(evento.hashCode(), "E1".hashCode());
    }
}
