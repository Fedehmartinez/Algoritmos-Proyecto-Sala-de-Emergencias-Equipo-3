package ucu.edu.aed.clases;

import java.time.LocalDateTime;

import ucu.edu.aed.tda.TDALista;

public class Main {

  private static final LocalDateTime T0 = LocalDateTime.of(2026, 9, 9, 8, 0);

  public static void main(String[] args) {
    SalaEmergencia sala = new SalaEmergencia();

    System.out.println("--- Registro de pacientes ---");
    sala.registrarPaciente("Ana Perez", "A1");
    sala.registrarPaciente("Beto Gomez", "B2");
    sala.registrarPaciente("Carla Diaz", "C3");
    sala.registrarPaciente("Diego Ruiz", "D4");
    System.out.println(sala.listarPacientes());
    System.out.println(sala);

    System.out.println("\n--- Buscar paciente por id ---");
    System.out.println("Buscar C3: " + sala.buscarPaciente("C3"));
    System.out.println("Buscar Z9: " + sala.buscarPaciente("Z9"));

    System.out.println("\n--- Asignar urgencia y encolar ---");
    sala.agregarPacienteACola(sala.buscarPaciente("A1"), NivelUrgencia.LEVE);
    sala.agregarPacienteACola(sala.buscarPaciente("B2"), NivelUrgencia.URGENTE);
    sala.agregarPacienteACola(sala.buscarPaciente("C3"), NivelUrgencia.MODERADO);
    sala.agregarPacienteACola(sala.buscarPaciente("D4"), NivelUrgencia.LEVE);
    System.out.println("Proximo a atender: " + sala.proximoAAtender().getNombre());
    System.out.println(sala);

    System.out.println("\n--- Cambiar prioridad de alguien que ya espera ---");
    sala.cambiarPrioridad(sala.buscarPaciente("A1"), NivelUrgencia.CRITICO);
    System.out.println("Ana pasa a CRITICO. Proximo: " + sala.proximoAAtender().getNombre());

    System.out.println("\n--- Se atiende al mas urgente: se abre su episodio ---");
    Episodio episodio = sala.atenderSiguiente("EP1", "dolor toracico", T0);
    System.out.println("Atiende a: " + episodio.getPaciente().getNombre());
    System.out.println("Raiz del episodio: " + episodio.getConsultaInicial().getIdEvento());

    System.out.println("\n--- El episodio se ramifica ---");
    String idPaciente = episodio.getPaciente().getId();
    sala.registrarEvento(idPaciente, "EP1-E0",
        new EventoClinico("E1", idPaciente, TipoEvento.ESTUDIO, "electrocardiograma", T0.plusMinutes(10)));
    sala.registrarEvento(idPaciente, "E1",
        new EventoClinico("E2", idPaciente, TipoEvento.INTERCONSULTA, "cardiologia", T0.plusMinutes(30)));
    sala.registrarEvento(idPaciente, "E2",
        new EventoClinico("E3", idPaciente, TipoEvento.PROCEDIMIENTO, "cateterismo", T0.plusMinutes(60)));
    sala.registrarEvento(idPaciente, "E3",
        new EventoClinico("E4", idPaciente, TipoEvento.COMPLICACION, "sangrado", T0.plusMinutes(90)));
    sala.registrarEvento(idPaciente, "EP1-E0",
        new EventoClinico("E5", idPaciente, TipoEvento.ESTUDIO, "analisis de sangre", T0.plusMinutes(15)));
    System.out.println("Eventos: " + episodio.cantidadEventos()
        + " | Profundidad de derivacion: " + episodio.profundidad());
    System.out.println("Derivaciones de la consulta inicial: " + episodio.derivacionesDe("EP1-E0"));

    System.out.println("\n--- Insumos donde se consumen ---");
    episodio.agregarInsumo("E1", new Insumo("electrodos", 150.0, 4));
    episodio.agregarInsumo("E3", new Insumo("cateter", 8000.0, 1));
    episodio.agregarInsumo("E4", new Insumo("gasas", 25.5, 10));
    System.out.printf("Costo del subarbol de E3: %.2f%n", episodio.costoDe("E3"));
    System.out.printf("Costo del episodio completo: %.2f%n", episodio.costoTotal());

    System.out.println("\n--- Que impide cerrar el episodio ---");
    System.out.println(idsDe(sala.queImpideCerrarEpisodioDe(idPaciente)));
    try {
      sala.cerrarEpisodio(idPaciente, T0.plusMinutes(200));
    } catch (IllegalStateException e) {
      System.out.println("Rechazado: " + e.getMessage());
    }

    System.out.println("\n--- Cierre de abajo hacia arriba ---");
    episodio.cerrarEvento("E4", T0.plusMinutes(120));
    episodio.cerrarEvento("E3", T0.plusMinutes(130));
    episodio.cerrarEvento("E2", T0.plusMinutes(140));
    episodio.cerrarEvento("E1", T0.plusMinutes(150));
    episodio.cerrarEvento("E5", T0.plusMinutes(40));
    sala.cerrarEpisodio(idPaciente, T0.plusMinutes(160));
    System.out.println("Estado del episodio: " + episodio.getEstado());
    System.out.println("Minutos acumulados: " + episodio.duracionAcumuladaMinutos("EP1-E0"));

    System.out.println("\n--- Diagnosticos del episodio ---");
    episodio.buscarEvento("EP1-E0").agregarCodigoDiagnostico(
        new CodigoDiagnostico("I21", "Infarto agudo de miocardio"));
    episodio.buscarEvento("E4").agregarCodigoDiagnostico(
        new CodigoDiagnostico("R58", "Hemorragia"));
    System.out.println(episodio.codigos());

    System.out.println("\n--- Consultas por rango de fechas ---");
    sala.abrirEpisodio("C3", "EP2", "fractura", T0.plusDays(2));
    sala.cerrarEpisodio("C3", T0.plusDays(2).plusHours(1));
    sala.abrirEpisodio("D4", "EP3", "fiebre", T0.plusDays(5));
    sala.cerrarEpisodio("D4", T0.plusDays(5).plusHours(1));

    System.out.println("Toda la sala, dias 0 a 3:");
    for (Episodio e : listar(sala.episodiosEnRango(T0, T0.plusDays(3)))) {
      System.out.println("   " + e);
    }
    System.out.println("Solo de C3, sin cotas:");
    for (Episodio e : listar(sala.episodiosDePacienteEnRango("C3", null, null))) {
      System.out.println("   " + e);
    }

    System.out.println("\n--- Resumen final ---");
    System.out.println(sala);
  }

  private static String idsDe(TDALista<EventoClinico> lista) {
    if (lista.esVacio()) {
      return "Nada lo impide";
    }
    StringBuilder sb = new StringBuilder("Siguen abiertos: ");
    for (int i = 0; i < lista.tamaño(); i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(lista.obtener(i).getIdEvento());
    }
    return sb.toString();
  }

  private static Episodio[] listar(TDALista<Episodio> lista) {
    Episodio[] copia = new Episodio[lista.tamaño()];
    for (int i = 0; i < lista.tamaño(); i++) {
      copia[i] = lista.obtener(i);
    }
    return copia;
  }
}
