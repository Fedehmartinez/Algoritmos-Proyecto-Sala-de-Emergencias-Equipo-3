package ucu.edu.aed.clases;

/**
 * Estado de un evento clínico.
 *
 * <p>Se mantiene separado de {@link EstadoEpisodio} aunque hoy tengan los mismos
 * valores: son dos niveles distintos de la jerarquía y conviene poder hablar de cada
 * uno sin ambigüedad. Un evento se cierra cuando terminó y todo lo que se desprendió
 * de él está cerrado; el episodio se cierra cuando se cierra su evento raíz.</p>
 */
public enum EstadoEvento {
    ABIERTO, // todavía en curso, o con descendientes en curso
    CERRADO  // terminado, y todo lo que derivó de él también
}
