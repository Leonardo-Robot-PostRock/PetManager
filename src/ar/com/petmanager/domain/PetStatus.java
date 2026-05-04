package ar.com.petmanager.domain;

/**
 * Estado de una mascota en el sistema.
 */
public enum PetStatus {
    ACTIVA("Activa"),
    PERDIDA("Perdida"),
    FALLECIDA("Fallecida");

    private final String label;

    PetStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}