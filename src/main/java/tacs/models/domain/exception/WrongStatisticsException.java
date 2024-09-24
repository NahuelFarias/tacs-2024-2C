package tacs.models.domain.exception;

public class WrongStatisticsException extends RuntimeException{
    String errorCode;
    public WrongStatisticsException(String message) {
        super(message);
        this.errorCode = message;
    }

    @Override
    public String getMessage() {
        return errorCode;
    }
}
