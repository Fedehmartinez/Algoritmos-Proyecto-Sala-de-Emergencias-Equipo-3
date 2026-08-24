package ucu.edu.aed.clases;

public class Main {
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
    sala.agregarPacienteACola(sala.buscarPaciente("B2"), NivelUrgencia.CRITICO);
    sala.agregarPacienteACola(sala.buscarPaciente("C3"), NivelUrgencia.MODERADO);
    sala.agregarPacienteACola(sala.buscarPaciente("D4"), NivelUrgencia.URGENTE);
    System.out.println(sala);

    System.out.println("\n--- Ingreso a consultorio por prioridad ---");
    Paciente beto = sala.buscarPaciente("B2");
    sala.ingresarPaciente(beto);
    System.out.println("Ingresa: " + beto.getNombre() + " (" + beto.getUrgencia() + ")");
    Paciente diego = sala.buscarPaciente("D4");
    sala.ingresarPaciente(diego);
    System.out.println("Ingresa: " + diego.getNombre() + " (" + diego.getUrgencia() + ")");
    System.out.println(sala.mostrarPacientesEnConsultorios());
    System.out.println(sala);

    System.out.println("\n--- Dar de alta ---");
    sala.darDeAlta(0, "Estabilizacion y sutura");
    System.out.println("Estado de Beto: " + beto.getEstadoPaciente());
    System.out.println(sala.listarConsultas());
    System.out.println(sala);

    System.out.println("\n--- Caracteristicas de un paciente ---");
    Paciente ana = sala.buscarPaciente("A1");
    ana.agregarCaracteristica("fiebre");
    ana.agregarCaracteristica("dolor de cabeza");
    System.out.println("Caracteristicas de Ana: " + ana.getCaracteristicas());
    ana.eliminarCaracteristica("fiebre");
    System.out.println("Tras quitar fiebre: " + ana.getCaracteristicas());

    System.out.println("\n--- Eliminar paciente ---");
    sala.eliminarPaciente("C3");
    System.out.println("Buscar C3: " + sala.buscarPaciente("C3"));
    System.out.println(sala.listarPacientes());

    System.out.println("\n--- Resumen final ---");
    System.out.println(sala);
  }
}
