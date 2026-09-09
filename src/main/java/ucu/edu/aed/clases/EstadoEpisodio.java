package ucu.edu.aed.clases;

/**
 * Estado de un episodio de atención.
 *
 * <p>Un episodio queda CERRADO únicamente cuando su evento raíz quedó cerrado, lo que
 * por la regla de cierre implica que todo el árbol del episodio está cerrado.</p>
 */
public enum EstadoEpisodio {
    ABIERTO, // el paciente está siendo atendido
    CERRADO  // la atención terminó por completo
}
