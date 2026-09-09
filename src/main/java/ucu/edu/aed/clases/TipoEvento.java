package ucu.edu.aed.clases;

/**
 * Tipo de un evento clínico dentro de un episodio de atención.
 *
 * <p>El tipo no impone la posición del evento en el árbol del episodio: quién derivó
 * de quién lo decide la jerarquía, no el tipo. Un ESTUDIO puede colgar de la consulta
 * inicial o de una interconsulta, y una COMPLICACION puede colgar de cualquiera.</p>
 */
public enum TipoEvento {
    CONSULTA_INICIAL, // abre el episodio, es la raíz del árbol
    ESTUDIO,          // análisis o imagen pedidos por otro evento
    INTERCONSULTA,    // derivación a otra especialidad
    PROCEDIMIENTO,    // intervención concreta sobre el paciente
    COMPLICACION      // problema surgido de otro evento, puede abrir nuevas ramas
}
