// com.invoiceapp.exception.CompanyAlreadyExistsException.java
package com.invoiceapp.exception;

public class CompanyAlreadyExistsException extends RuntimeException {
    public CompanyAlreadyExistsException(String message) {
        super(message);
    }
}
