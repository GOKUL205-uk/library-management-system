package com.library.model;

import java.util.Objects;

/**
 * Represents a library member who can borrow books.
 */
public class Member {
    private final String memberId;
    private String name;
    private String email;
    private String phone;

    public Member(String memberId, String name, String email, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    public String toCsv() {
        return String.join(",", memberId, escape(name), escape(email), phone);
    }

    private String escape(String value) {
        return value.replace(",", ";");
    }

    public static Member fromCsv(String line) {
        String[] parts = line.split(",", -1);
        return new Member(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-25s | %-25s | %-12s", memberId, name, email, phone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return memberId.equals(member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}
