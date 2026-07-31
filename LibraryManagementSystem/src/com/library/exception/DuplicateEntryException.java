package com.library.exception;

/** Thrown when attempting to register a book or member ID that already exists. */
public class DuplicateEntryException extends LibraryException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}
