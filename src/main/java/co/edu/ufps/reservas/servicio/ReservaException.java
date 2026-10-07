package co.edu.ufps.reservas.servicio;

public class ReservaException extends RuntimeException {
    private final int status;
    public ReservaException(int status, String message) { super(message); this.status = status; }
    public int status() { return status; }
}
