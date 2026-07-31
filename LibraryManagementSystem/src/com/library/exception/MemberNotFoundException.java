package com.library.exception;

/** Thrown when a member with the given ID does not exist. */
public class MemberNotFoundException extends LibraryException {
    public MemberNotFoundException(String memberId) {
        super("No member found with ID: " + memberId);
    }
}
