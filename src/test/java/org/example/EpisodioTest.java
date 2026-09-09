package org.example;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import ucu.edu.aed.clases.Episodio;
import ucu.edu.aed.clases.EstadoActual;
import ucu.edu.aed.clases.EventoClinico;
import ucu.edu.aed.clases.Insumo;
import ucu.edu.aed.clases.Paciente;
import ucu.edu.aed.clases.TipoEvento;
import ucu.edu.aed.tda.TDALista;

public class EpisodioTest extends TestCase {

    private static final LocalDateTime T0 = LocalDateTime.of(2026, 9, 9, 8, 0);

    private Paciente paciente;
    private Episodio episodio;

    protected void setUp(){
        paciente = new Paciente("P1", "Ana Perez");
        episodio = new Episodio("EP1", paciente, ev("E1", TipoEvento.CONSULTA_INICIAL, 0));
    }

    private EventoClinico ev(String id, TipoEvento tipo, int minuto){
        return new EventoClinico(id, "P1", tipo, "descripcion de " + id, T0.plusMinutes(minuto));
    }

    private void armarArbolCompleto(){
        episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 10));
        episodio.registrarEvento("E2", ev("E3", TipoEvento.INTERCONSULTA, 30));
        episodio.registrarEvento("E3", ev("E4", TipoEvento.PROCEDIMIENTO, 60));
        episodio.registrarEvento("E4", ev("E5", TipoEvento.COMPLICACION, 90));
        episodio.registrarEvento("E1", ev("E6", TipoEvento.ESTUDIO, 10));
    }

    private String ids(TDALista<EventoClinico> lista){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.tamaño(); i++){
            if (i > 0){
                sb.append(",");
            }
            sb.append(lista.obtener(i).getIdEvento());
        }
        return sb.toString();
    }

    public void testEpisodioNuevoTieneSoloLaConsultaInicial(){
        assertEquals("E1", episodio.getConsultaInicial().getIdEvento());
        assertEquals(EstadoActual.ABIERTO, episodio.getEstado());
        assertTrue(episodio.estaAbierto());
        assertNull(episodio.getFechaCierre());
    }

    public void testLaFechaDeAperturaEsLaDeLaConsultaInicial(){
        assertEquals(T0, episodio.getFechaApertura());
    }

    public void testNoSePuedeAbrirSinConsultaInicial(){
        try {
            new Episodio("EP9", paciente, null);
            fail("un episodio no puede existir sin su consulta inicial");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testNoSePuedeAbrirConLaConsultaDeOtroPaciente(){
        EventoClinico ajena = new EventoClinico("Z1", "OTRO", TipoEvento.CONSULTA_INICIAL, "x", T0);
        try {
            new Episodio("EP9", paciente, ajena);
            fail("la consulta inicial tiene que ser del mismo paciente");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testNoSePuedeAbrirSinIdNiPaciente(){
        try {
            new Episodio(" ", paciente, ev("E1", TipoEvento.CONSULTA_INICIAL, 0));
            fail("deberia rechazar un id vacio");
        } catch (IllegalArgumentException esperada){

        }
        try {
            new Episodio("EP9", null, ev("E1", TipoEvento.CONSULTA_INICIAL, 0));
            fail("deberia rechazar un episodio sin paciente");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testRegistrarEventoLoCuelgaDeSuOrigen(){
        assertTrue(episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 10)));
        assertEquals("E2", ids(episodio.derivacionesDe("E1")));
    }

    public void testUnEventoPuedeTenerVariasDerivaciones(){
        episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 10));
        episodio.registrarEvento("E1", ev("E6", TipoEvento.ESTUDIO, 10));
        episodio.registrarEvento("E1", ev("E7", TipoEvento.INTERCONSULTA, 15));
        assertEquals("E2,E6,E7", ids(episodio.derivacionesDe("E1")));
    }

    public void testTodoLoQueDerivoDeUnEvento(){
        armarArbolCompleto();
        assertEquals("E3,E4,E5", ids(episodio.todoLoQueDerivoDe("E2")));
        assertEquals("", ids(episodio.todoLoQueDerivoDe("E6")));
    }

    public void testRegistrarBajoUnPadreInexistenteFalla(){
        try {
            episodio.registrarEvento("NO_EXISTE", ev("E2", TipoEvento.ESTUDIO, 10));
            fail("no deberia poder colgar de un evento que no esta");
        } catch (NoSuchElementException esperada){

        }
    }

    public void testNoSeRegistraDosVecesElMismoEvento(){
        episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 10));
        assertFalse(episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 10)));
    }

    public void testNoSeRegistraUnEventoDeOtroPaciente(){
        EventoClinico ajeno = new EventoClinico("Z1", "OTRO", TipoEvento.ESTUDIO, "x", T0);
        try {
            episodio.registrarEvento("E1", ajeno);
            fail("el evento tiene que ser del mismo paciente");
        } catch (IllegalArgumentException esperada){

        }
    }

    public void testBuscarEvento(){
        armarArbolCompleto();
        assertEquals("E4", episodio.buscarEvento("E4").getIdEvento());
        assertNull(episodio.buscarEvento("NO_EXISTE"));
        assertNull(episodio.buscarEvento(null));
    }

    public void testRegistrarEventoArmandoloEnElEpisodio(){
        assertTrue(episodio.registrarEvento("E1",
                new EventoClinico("E2", "P1", TipoEvento.ESTUDIO, "ecg", T0.plusMinutes(10))));
        EventoClinico creado = episodio.buscarEvento("E2");
        assertNotNull(creado);

        assertEquals("P1", creado.getIdPaciente());
        assertEquals(TipoEvento.ESTUDIO, creado.getTipo());
        assertEquals("ecg", creado.getDescripcion());
    }

    public void testNoSeLePuedenColgarDerivacionesAUnEventoCerrado(){
        episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 10));
        episodio.cerrarEvento("E2", T0.plusMinutes(20));
        try {
            episodio.registrarEvento("E2", ev("E3", TipoEvento.COMPLICACION, 30));
            fail("no deberia aceptar derivaciones de un evento cerrado");
        } catch (IllegalStateException esperada){

        }
        assertTrue(episodio.queImpideCerrar("E2").esVacio());
    }

    public void testUnEventoCerradoNoInvalidaAlResto(){

        episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 10));
        episodio.cerrarEvento("E2", T0.plusMinutes(20));
        assertTrue(episodio.registrarEvento("E1", ev("E3", TipoEvento.ESTUDIO, 30)));
    }

    public void testAgregarInsumoDesdeElEpisodio(){
        armarArbolCompleto();
        episodio.agregarInsumo("E4", new Insumo("cateter", 8000.0, 1));
        assertEquals(8000.0, episodio.costoDe("E4"), 0.0001);
    }

    public void testNoSePuedeAgregarInsumoAUnEventoInexistente(){
        try {
            episodio.agregarInsumo("NO_EXISTE", new Insumo("x", 1.0, 1));
            fail("deberia fallar con un evento que no esta");
        } catch (NoSuchElementException esperada){

        }
    }

    public void testUnEventoCerradoNoAdmiteMasInsumos(){
        episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 10));
        episodio.cerrarEvento("E2", T0.plusMinutes(20));
        try {
            episodio.agregarInsumo("E2", new Insumo("gasas", 10.0, 1));
            fail("un evento cerrado ya rindio sus costos");
        } catch (IllegalStateException esperada){

        }
    }

    public void testEnUnEpisodioReciénAbiertoNadaImpideCerrar(){
        assertTrue(episodio.queImpideCerrarEpisodio().esVacio());
        assertTrue(episodio.puedeCerrarse("E1"));
    }

    public void testQueImpideCerrarDevuelveLosAbiertosEnPostOrden(){
        armarArbolCompleto();
        assertEquals("E5,E4,E3,E2,E6", ids(episodio.queImpideCerrarEpisodio()));
    }

    public void testUnEventoNoSeBloqueaASiMismo(){
        armarArbolCompleto();

        assertTrue(episodio.queImpideCerrar("E5").esVacio());
        assertTrue(episodio.puedeCerrarse("E5"));
    }

    public void testNoSePuedeCerrarConDescendientesAbiertos(){
        armarArbolCompleto();
        try {
            episodio.cerrarEvento("E4", T0.plusMinutes(120));
            fail("E4 no se puede cerrar con E5 abierto");
        } catch (IllegalStateException esperada){
            assertTrue("el mensaje deberia decir que falta E5: " + esperada.getMessage(),
                    esperada.getMessage().contains("E5"));
        }
    }

    public void testCerrarDeAbajoHaciaArribaFunciona(){
        armarArbolCompleto();
        assertTrue(episodio.cerrarEvento("E5", T0.plusMinutes(120)));
        assertTrue(episodio.cerrarEvento("E4", T0.plusMinutes(130)));
        assertTrue(episodio.cerrarEvento("E3", T0.plusMinutes(140)));
        assertTrue(episodio.cerrarEvento("E2", T0.plusMinutes(150)));

        assertEquals("E6", ids(episodio.queImpideCerrarEpisodio()));
        assertTrue(episodio.cerrarEvento("E6", T0.plusMinutes(40)));
        assertTrue(episodio.queImpideCerrarEpisodio().esVacio());
    }

    public void testCerrarUnEventoInexistenteFalla(){
        try {
            episodio.cerrarEvento("NO_EXISTE", T0.plusMinutes(10));
            fail("deberia fallar con un evento que no esta");
        } catch (NoSuchElementException esperada){

        }
    }

    public void testCerrarDosVecesElMismoEventoDevuelveFalse(){
        assertTrue(episodio.cerrarEvento("E1", T0.plusMinutes(10)));
        assertFalse(episodio.cerrarEvento("E1", T0.plusMinutes(20)));
    }

    public void testNoSePuedeCerrarElEpisodioConEventosAbiertos(){
        armarArbolCompleto();
        try {
            episodio.cerrarEpisodio(T0.plusMinutes(200));
            fail("no deberia cerrar con eventos abiertos");
        } catch (IllegalStateException esperada){

        }
        assertTrue(episodio.estaAbierto());
    }

    public void testCerrarElEpisodioCuandoTodoEstaCerrado(){
        armarArbolCompleto();
        episodio.cerrarEvento("E5", T0.plusMinutes(120));
        episodio.cerrarEvento("E4", T0.plusMinutes(130));
        episodio.cerrarEvento("E3", T0.plusMinutes(140));
        episodio.cerrarEvento("E2", T0.plusMinutes(150));
        episodio.cerrarEvento("E6", T0.plusMinutes(40));

        assertTrue(episodio.cerrarEpisodio(T0.plusMinutes(160)));
        assertEquals(EstadoActual.CERRADO, episodio.getEstado());
        assertFalse(episodio.estaAbierto());
        assertEquals(T0.plusMinutes(160), episodio.getFechaCierre());
    }

    public void testCerrarUnEpisodioYaCerradoDevuelveFalse(){
        assertTrue(episodio.cerrarEpisodio(T0.plusMinutes(10)));
        assertFalse(episodio.cerrarEpisodio(T0.plusMinutes(20)));
    }

    public void testNoSeRegistranEventosEnUnEpisodioCerrado(){
        episodio.cerrarEpisodio(T0.plusMinutes(10));
        try {
            episodio.registrarEvento("E1", ev("E2", TipoEvento.ESTUDIO, 20));
            fail("no deberia aceptar eventos despues del cierre");
        } catch (IllegalStateException esperada){

        }
    }

    public void testEventosAbiertos(){
        armarArbolCompleto();
        assertEquals(6, episodio.eventosAbiertos().tamaño());
        episodio.cerrarEvento("E5", T0.plusMinutes(120));
        assertEquals(5, episodio.eventosAbiertos().tamaño());
    }

    public void testEpisodioSinInsumosNoCuestaNada(){
        armarArbolCompleto();
        assertEquals(0.0, episodio.costoTotal(), 0.0001);
    }

    public void testCostoDeUnSubarbolIncluyeAlNodoYSusDescendientes(){
        armarArbolCompleto();
        episodio.buscarEvento("E2").agregarInsumo(new Insumo("electrodos", 150.0, 4));
        episodio.buscarEvento("E4").agregarInsumo(new Insumo("cateter", 8000.0, 1));
        episodio.buscarEvento("E5").agregarInsumo(new Insumo("gasas", 25.5, 10));

        assertEquals(8255.0, episodio.costoDe("E4"), 0.0001);
        assertEquals(8855.0, episodio.costoDe("E2"), 0.0001);
        assertEquals(8855.0, episodio.costoTotal(), 0.0001);
        assertEquals(0.0, episodio.costoDe("E6"), 0.0001);
    }

    public void testCostoDeUnEventoInexistenteFalla(){
        try {
            episodio.costoDe("NO_EXISTE");
            fail("deberia fallar con un evento que no esta");
        } catch (NoSuchElementException esperada){

        }
    }

    public void testLosEventosAbiertosNoSumanTiempo(){
        armarArbolCompleto();
        assertEquals(0L, episodio.duracionAcumuladaMinutos("E2"));
    }

    public void testDuracionAcumuladaSumaElSubarbol(){
        armarArbolCompleto();
        episodio.cerrarEvento("E5", T0.plusMinutes(120));
        episodio.cerrarEvento("E4", T0.plusMinutes(130));
        episodio.cerrarEvento("E3", T0.plusMinutes(140));
        episodio.cerrarEvento("E2", T0.plusMinutes(150));
        assertEquals(350L, episodio.duracionAcumuladaMinutos("E2"));
        assertEquals(100L, episodio.duracionAcumuladaMinutos("E4"));
    }

    public void testCodigosDelEpisodioSeReunenSinRepetir(){
        armarArbolCompleto();
        episodio.buscarEvento("E1").agregarCodigoDiagnostico("I21");
        episodio.buscarEvento("E4").agregarCodigoDiagnostico("I21");
        episodio.buscarEvento("E5").agregarCodigoDiagnostico("R58");

        TDALista<String> codigos = episodio.codigos();
        assertEquals(2, codigos.tamaño());
        assertTrue(codigos.contiene("I21"));
    }

    public void testEpisodioSinDiagnosticosDevuelveListaVacia(){
        assertTrue(episodio.codigos().esVacio());
    }

    public void testOrdenaPorFechaDeApertura(){
        Episodio temprano = new Episodio("A", paciente, ev("EA", TipoEvento.CONSULTA_INICIAL, 0));
        Episodio tardio = new Episodio("B", paciente, ev("EB", TipoEvento.CONSULTA_INICIAL, 60));
        assertTrue(temprano.compareTo(tardio) < 0);
    }

    public void testMismaAperturaDesempataPorId(){
        Episodio a = new Episodio("A", paciente, ev("EA", TipoEvento.CONSULTA_INICIAL, 5));
        Episodio b = new Episodio("B", paciente, ev("EB", TipoEvento.CONSULTA_INICIAL, 5));
        assertEquals(a.getFechaApertura(), b.getFechaApertura());
        assertTrue(a.compareTo(b) < 0);
    }

    public void testPorFechaComoCriterio(){
        assertEquals(0, Episodio.porFecha(T0).compareTo(episodio));
        assertTrue(Episodio.porFecha(T0.plusMinutes(1)).compareTo(episodio) > 0);
    }

    public void testIgualdadEsPorId(){
        Episodio otroConMismoId =
                new Episodio("EP1", paciente, ev("EX", TipoEvento.CONSULTA_INICIAL, 99));
        assertEquals(episodio, otroConMismoId);
        assertEquals(episodio.hashCode(), otroConMismoId.hashCode());
    }
}
