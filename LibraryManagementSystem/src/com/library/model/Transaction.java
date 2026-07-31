package com.library.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Records a single issue/return event for a book-member pair.
 */
public class Transaction {

    public enum Type { ISSUE, RETURN }

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final String isbn;
    private final String memberId;
    private final Type type;
    private final LocalDate date;

    public Transaction(String isbn, String memberId, Type type, LocalDate date) {
        this.isbn = isbn;
        this.memberId = memberId;
        this.type = type;
        this.date = date;
    }

    public String getIsbn() { return isbn; }
    public String getMemberId() { return memberId; }
    public Type getType() { return type; }
    public LocalDate getDate() { return date; }

    public String toCsv() {
        return String.join(",", isbn, memberId, type.name(), date.format(FORMAT));
    }

    public static Transaction fromCsv(String line) {
        String[] parts = line.split(",", -1);
        return new Transaction(parts[0], parts[1], Type.valueOf(parts[2]), LocalDate.parse(parts[3], FORMAT));
    }

    @Override
    public String toString() {
        return String.format("%-10s | ISBN: %-13s | Member: %-8s | %s", type, isbn, memberId, date);
    }
}
