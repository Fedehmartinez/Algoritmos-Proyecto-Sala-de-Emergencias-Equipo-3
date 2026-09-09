package ucu.edu.aed.clases;

import java.time.LocalDateTime;

public class Main {

    private static final LocalDateTime T0 = LocalDateTime.of(2026, 9, 9, 8, 0);

    public static void main(String[] args) {
        SalaEmergencia sala = new SalaEmergencia();

        sala.registrarPaciente("Ana Perez", "A1");
        sala.registrarPaciente("Beto Gomez", "B2");
        sala.agregarPacienteACola(sala.buscarPaciente("A1"), NivelUrgencia.LEVE);
        sala.agregarPacienteACola(sala.buscarPaciente("B2"), NivelUrgencia.MODERADO);
        System.out.println(sala.listarPacientes());

        sala.cambiarPrioridad(sala.buscarPaciente("A1"), NivelUrgencia.CRITICO);
        System.out.println("Proximo a atender: " + sala.proximoAAtender().getNombre());

        Episodio episodio = sala.atenderSiguiente("EP1", "dolor toracico", T0);
        String idPaciente = episodio.getPaciente().getId();
        sala.registrarEvento(idPaciente, "EP1-E0",
                new EventoClinico("E1", idPaciente, TipoEvento.ESTUDIO, "electrocardiograma", T0.plusMinutes(10)));
        sala.registrarEvento(idPaciente, "E1",
                new EventoClinico("E2", idPaciente, TipoEvento.PROCEDIMIENTO, "cateterismo", T0.plusMinutes(30)));

        sala.agregarInsumo(idPaciente, "E2", new Insumo("cateter", 8000.0, 1));
        System.out.printf("Costo del episodio: %.2f%n", episodio.costoTotal());

        sala.agregarCapituloDiagnostico("I", "Enfermedades del sistema circulatorio");
        sala.agregarGrupoDiagnostico("I", "I20-I25", "Cardiopatias isquemicas");
        sala.agregarCodigoAlCatalogo("I20-I25", "I21", "Infarto agudo de miocardio");
        sala.diagnosticar(idPaciente, "E2", "I21");
        System.out.println("Diagnosticos: " + episodio.codigos());

        sala.cerrarEvento(idPaciente, "E2", T0.plusMinutes(60));
        sala.cerrarEvento(idPaciente, "E1", T0.plusMinutes(70));
        sala.cerrarEpisodio(idPaciente, T0.plusMinutes(80));
        System.out.println("Episodio cerrado: " + episodio.getEstado());

        System.out.println("Episodios entre T0 y T0+1h: "
                + sala.episodiosEnRango(T0, T0.plusHours(1)).tamaño());

        System.out.println(sala);
    }
}
