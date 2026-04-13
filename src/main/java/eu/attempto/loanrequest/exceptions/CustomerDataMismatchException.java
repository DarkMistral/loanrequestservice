package eu.attempto.loanrequest.exceptions;

public class CustomerDataMismatchException extends IllegalArgumentException {
    public CustomerDataMismatchException() {
        super();
    }

    public CustomerDataMismatchException(String message) {
        super(message);
    }
}
