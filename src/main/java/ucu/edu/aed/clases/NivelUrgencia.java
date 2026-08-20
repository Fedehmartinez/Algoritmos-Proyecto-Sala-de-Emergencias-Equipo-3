package ucu.edu.aed.clases;

public enum NivelUrgencia {
    CRITICO(0),
    URGENTE(10),
    MODERADO(60),
    LEVE(120),
    NO_URGENTE(240);

    private final int tiempoMaximoEsperaMinutos;

    NivelUrgencia(int tiempoMaximoEsperaMinutos){
        this.tiempoMaximoEsperaMinutos = tiempoMaximoEsperaMinutos;
    }

    public int getTiempoMaximoEsperaMinutos(){
        return tiempoMaximoEsperaMinutos;
    }
}