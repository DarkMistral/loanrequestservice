package eu.attempto.loanrequest.exceptions;

public class InvalidAmountException extends IllegalArgumentException {

    public InvalidAmountException() {
        super();
    };

    public InvalidAmountException(String message) {
        super(message);
    }
}
