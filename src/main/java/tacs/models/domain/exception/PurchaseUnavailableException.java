package tacs.models.domain.exception;

public class PurchaseUnavailableException extends RuntimeException{

    String errorCode = "Purchase Unavailable";
    @Override
    public String getMessage() {
        // Construir un mensaje dinámico
        return "Error code: " + errorCode;
    }
}
