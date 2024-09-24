package tacs.models.domain.exception;

public class SoldOutTicketsException extends RuntimeException{
    String errorCode = "Sold out tickets";
    @Override
    public String getMessage() {
        return "Error code: " + errorCode;
    }
}
