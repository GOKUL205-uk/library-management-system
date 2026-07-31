package com.library.exception;

/** Thrown when a book with the given ISBN does not exist in the catalogue. */
public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String isbn) {
        super("No book found with ISBN: " + isbn);
    }
}
