package com.library.exception;

/**
 * Base checked exception for all library-domain errors.
 * Letting BookNotFoundException, MemberNotFoundException, etc. extend this
 * allows callers to catch every domain error with a single catch block
 * while still being able to catch specific subtypes when needed.
 */
public class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}
