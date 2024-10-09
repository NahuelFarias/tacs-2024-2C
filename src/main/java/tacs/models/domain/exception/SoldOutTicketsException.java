package tacs.models.domain.exception;

public class SoldOutTicketsException extends RuntimeException {
    private String errorCause;

    public SoldOutTicketsException(String errorCause) {
        super(errorCause);
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
