package eu.attempto.loanrequest.exceptions;

import java.util.NoSuchElementException;

public class NoEntryForCustomerException extends NoSuchElementException {

    public NoEntryForCustomerException() {
        super();
    }

    public NoEntryForCustomerException(String message) {
        super(message);
    }
}
