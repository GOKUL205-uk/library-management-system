package com.library.model;

import java.util.Objects;

/**
 * Represents a Book in the library catalogue.
 * Demonstrates encapsulation: all fields are private with controlled access.
 */
public class Book {
    private final String isbn;
    private String title;
    private String author;
    private Genre genre;
    private int totalCopies;
    private int availableCopies;

    public Book(String isbn, String title, String author, Genre genre, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    // Constructor used when reloading from file, where available copies is already known
    public Book(String isbn, String title, String author, Genre genre, int totalCopies, int availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Genre getGenre() { return genre; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public void addCopies(int count) {
        this.totalCopies += count;
        this.availableCopies += count;
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public void decrementAvailable() {
        if (availableCopies > 0) availableCopies--;
    }

    public void incrementAvailable() {
        if (availableCopies < totalCopies) availableCopies++;
    }

    /** Serializes this book to a single CSV line for file persistence. */
    public String toCsv() {
        return String.join(",", isbn, escape(title), escape(author), genre.name(),
                String.valueOf(totalCopies), String.valueOf(availableCopies));
    }

    private String escape(String value) {
        return value.replace(",", ";");
    }

    public static Book fromCsv(String line) {
        String[] parts = line.split(",", -1);
        String isbn = parts[0];
        String title = parts[1];
        String author = parts[2];
        Genre genre = Genre.fromString(parts[3]);
        int total = Integer.parseInt(parts[4]);
        int available = Integer.parseInt(parts[5]);
        return new Book(isbn, title, author, genre, total, available);
    }

    @Override
    public String toString() {
        return String.format("%-13s | %-30s | %-20s | %-12s | Total: %-3d | Available: %-3d",
                isbn, title, author, genre, totalCopies, availableCopies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
