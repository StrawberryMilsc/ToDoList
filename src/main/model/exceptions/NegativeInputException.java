package model.exceptions;

public class NegativeInputException extends IllegalArgumentException {

    public NegativeInputException(String msg) {
        super(msg);
    }

    public NegativeInputException() {
        super();
    }
}
