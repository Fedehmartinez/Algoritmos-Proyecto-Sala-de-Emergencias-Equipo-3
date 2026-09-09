package org.example;

import java.util.NoSuchElementException;

import junit.framework.TestCase;
import ucu.edu.aed.clases.CatalogoDiagnosticos;
import ucu.edu.aed.clases.NivelCatalogo;
import ucu.edu.aed.clases.NodoCatalogo;
import ucu.edu.aed.tda.TDALista;

public class CatalogoDiagnosticosTest extends TestCase {

    private CatalogoDiagnosticos catalogo;

    protected void setUp(){
        catalogo = new CatalogoDiagnosticos();
        catalogo.agregarCapitulo("I", "Enfermedades del sistema circulatorio");
        catalogo.agregarGrupo("I", "I20-I25", "Cardiopatias isquemicas");
        catalogo.agregarCodigo("I20-I25", "I21", "Infarto agudo de miocardio");
        catalogo.agregarCodigo("I20-I25", "I25", "Cardiopatia isquemica cronica");
        catalogo.agregarGrupo("I", "I60-I69", "Enfermedades cerebrovasculares");
        catalogo.agregarCodigo("I60-I69", "I63", "Infarto cerebral");
    }

    public void testCatalogoNuevoEstaVacio(){
        CatalogoDiagnosticos vacio = new CatalogoDiagnosticos();
        assertTrue(vacio.esVacio());
        assertEquals(0, vacio.cantidadNodos());
    }

    public void testCatalogoConDatosNoEstaVacio(){
        assertFalse(catalogo.esVacio());
        assertEquals(6, catalogo.cantidadNodos());
    }

    public void testAgregarCapituloLoDejaBuscable(){
        NodoCatalogo capitulo = catalogo.buscarNodo("I");
        assertNotNull(capitulo);
        assertEquals("Enfermedades del sistema circulatorio", capitulo.getNombre());
        assertEquals(NivelCatalogo.CAPITULO, capitulo.getNivel());
    }

    public void testAgregarCapituloDuplicadoNoLoAgrega(){
        assertFalse(catalogo.agregarCapitulo("I", "Otro nombre"));
        assertEquals(6, catalogo.cantidadNodos());
    }

    public void testAgregarGrupoBajoCapituloInexistenteLanzaExcepcion(){
        try {
            catalogo.agregarGrupo("NOPE", "G1", "grupo");
            fail("Debia lanzar excepcion");
        } catch (NoSuchElementException e) {

        }
    }

    public void testAgregarGrupoBajoUnCodigoQueNoEsCapituloLanzaExcepcion(){
        try {
            catalogo.agregarGrupo("I21", "G1", "grupo");
            fail("Debia lanzar excepcion");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testAgregarCodigoBajoGrupoInexistenteLanzaExcepcion(){
        try {
            catalogo.agregarCodigo("NOPE", "I99", "codigo");
            fail("Debia lanzar excepcion");
        } catch (NoSuchElementException e) {

        }
    }

    public void testAgregarCodigoBajoUnCapituloQueNoEsGrupoLanzaExcepcion(){
        try {
            catalogo.agregarCodigo("I", "I99", "codigo");
            fail("Debia lanzar excepcion");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testCodigoDuplicadoEnOtraRamaNoSeAgrega(){

        assertFalse(catalogo.agregarCodigo("I60-I69", "I21", "otro nombre"));
        assertEquals(6, catalogo.cantidadNodos());
    }

    public void testBuscarNodoInexistenteDevuelveNull(){
        assertNull(catalogo.buscarNodo("NOPE"));
    }

    public void testBuscarConCodigoNuloLanzaExcepcion(){
        try {
            catalogo.buscarNodo(null);
            fail("Debia lanzar excepcion");
        } catch (IllegalArgumentException e) {

        }
    }

    public void testUnCodigoEsDiagnosticable(){
        assertTrue(catalogo.esDiagnosticable("I21"));
    }

    public void testUnCapituloNoEsDiagnosticable(){
        assertFalse(catalogo.esDiagnosticable("I"));
    }

    public void testUnGrupoNoEsDiagnosticable(){
        assertFalse(catalogo.esDiagnosticable("I20-I25"));
    }

    public void testUnCodigoInexistenteNoEsDiagnosticable(){
        assertFalse(catalogo.esDiagnosticable("NOPE"));
    }

    public void testCodigosBajoUnCapituloTraeTodosLosDeSusGrupos(){
        TDALista<NodoCatalogo> codigos = catalogo.codigosBajo("I");
        assertEquals(3, codigos.tamaño());
        assertTrue(codigos.contiene(new NodoCatalogo("I21", "x", NivelCatalogo.CODIGO)));
        assertTrue(codigos.contiene(new NodoCatalogo("I25", "x", NivelCatalogo.CODIGO)));
        assertTrue(codigos.contiene(new NodoCatalogo("I63", "x", NivelCatalogo.CODIGO)));
    }

    public void testCodigosBajoUnGrupoTraeSoloLosSuyos(){
        TDALista<NodoCatalogo> codigos = catalogo.codigosBajo("I20-I25");
        assertEquals(2, codigos.tamaño());
        assertTrue(codigos.contiene(new NodoCatalogo("I21", "x", NivelCatalogo.CODIGO)));
        assertTrue(codigos.contiene(new NodoCatalogo("I25", "x", NivelCatalogo.CODIGO)));
    }

    public void testCodigosBajoUnCodigoLoTraeAElSolo(){
        TDALista<NodoCatalogo> codigos = catalogo.codigosBajo("I21");
        assertEquals(1, codigos.tamaño());
        assertEquals("I21", codigos.obtener(0).getCodigo());
    }

    public void testCodigosBajoUnNodoInexistenteLanzaExcepcion(){
        try {
            catalogo.codigosBajo("NOPE");
            fail("Debia lanzar excepcion");
        } catch (NoSuchElementException e) {

        }
    }
}
