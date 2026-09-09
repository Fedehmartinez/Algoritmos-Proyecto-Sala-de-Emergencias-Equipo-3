package ucu.edu.aed.clases;

/**
 * Nivel de un nodo dentro del catálogo institucional de diagnósticos.
 *
 * <p>El catálogo es un árbol de tres niveles: los capítulos cuelgan de la raíz, los
 * grupos de los capítulos y los códigos de los grupos. Sólo los códigos son
 * diagnosticables; capítulos y grupos existen para poder consultar por un nivel
 * entero sin enumerar a mano lo que contiene.</p>
 */
public enum NivelCatalogo {
    CAPITULO, // nivel 1: la clasificación más gruesa
    GRUPO,    // nivel 2: subdivisión de un capítulo
    CODIGO    // nivel 3: el diagnóstico concreto, siempre una hoja
}
