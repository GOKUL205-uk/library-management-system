package com.library.model;

/**
 * Enum representing the genre/category of a book.
 * Using an enum instead of a raw String prevents typos and invalid categories.
 */
public enum Genre {
    FICTION,
    NON_FICTION,
    SCIENCE,
    TECHNOLOGY,
    HISTORY,
    BIOGRAPHY,
    FANTASY,
    MYSTERY,
    OTHER;

    /**
     * Safely converts a String to a Genre, defaulting to OTHER if not recognized.
     */
    public static Genre fromString(String value) {
        try {
            return Genre.valueOf(value.trim().toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException | NullPointerException e) {
            return OTHER;
        }
    }
}
