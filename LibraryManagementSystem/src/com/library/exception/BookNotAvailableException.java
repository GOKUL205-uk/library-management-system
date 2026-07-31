package com.library.exception;

/** Thrown when a book exists but has no available copies to issue. */
public class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String title) {
        super("Book is currently not available (all copies issued): " + title);
    }
}
