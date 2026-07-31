package com.library;

import com.library.exception.*;
import com.library.model.*;
import com.library.service.LibraryService;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point: a menu-driven console interface for the Library Management System.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final LibraryService library = new LibraryService();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addBook();
                    case "2" -> viewAllBooks();
                    case "3" -> searchBooks();
                    case "4" -> registerMember();
                    case "5" -> viewAllMembers();
                    case "6" -> issueBook();
                    case "7" -> returnBook();
                    case "8" -> viewCurrentlyIssued();
                    case "9" -> viewMemberHistory();
                    case "0" -> {
                        running = false;
                        System.out.println("Goodbye! All data has been saved to the data/ folder.");
                    }
                    default -> System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (LibraryException e) {
                // Unified catch for our custom checked exceptions (see LibraryException note below)
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("---------------------------------------------");
        System.out.println("1. Add Book              6. Issue Book");
        System.out.println("2. View All Books        7. Return Book");
        System.out.println("3. Search Books          8. View Currently Issued Books");
        System.out.println("4. Register Member       9. View Member Borrow History");
        System.out.println("5. View All Members      0. Exit");
        System.out.println("---------------------------------------------");
        System.out.print("Enter choice: ");
    }

    private static void addBook() throws DuplicateEntryException {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Genre (FICTION, NON_FICTION, SCIENCE, TECHNOLOGY, HISTORY, BIOGRAPHY, FANTASY, MYSTERY, OTHER): ");
        Genre genre = Genre.fromString(scanner.nextLine().trim());
        System.out.print("Number of copies: ");
        int copies = readInt();

        library.addBook(new Book(isbn, title, author, genre, copies));
        System.out.println("Book added successfully.");
    }

    private static void viewAllBooks() {
        List<Book> books = library.getBooksSortedByTitle();
        if (books.isEmpty()) {
            System.out.println("No books in the catalogue yet.");
            return;
        }
        books.forEach(System.out::println);
    }

    private static void searchBooks() {
        System.out.print("Search by (1) Title or (2) Author: ");
        String option = scanner.nextLine().trim();
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().trim();
        List<Book> results = option.equals("2")
                ? library.searchBooksByAuthor(keyword)
                : library.searchBooksByTitle(keyword);

        if (results.isEmpty()) {
            System.out.println("No matching books found.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private static void registerMember() throws DuplicateEntryException {
        System.out.print("Member ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        library.registerMember(new Member(id, name, email, phone));
        System.out.println("Member registered successfully.");
    }

    private static void viewAllMembers() {
        List<Member> members = library.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members registered yet.");
            return;
        }
        members.forEach(System.out::println);
    }

    private static void issueBook() throws BookNotFoundException, MemberNotFoundException, BookNotAvailableException {
        System.out.print("Book ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();

        library.issueBook(isbn, memberId);
        System.out.println("Book issued successfully.");
    }

    private static void returnBook() throws BookNotFoundException, MemberNotFoundException {
        System.out.print("Book ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();

        library.returnBook(isbn, memberId);
        System.out.println("Book returned successfully.");
    }

    private static void viewCurrentlyIssued() {
        List<Transaction> issued = library.getCurrentlyIssuedBooks();
        if (issued.isEmpty()) {
            System.out.println("No books are currently issued.");
            return;
        }
        issued.forEach(System.out::println);
    }

    private static void viewMemberHistory() throws MemberNotFoundException {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine().trim();
        library.findMember(memberId); // throws if not found
        List<Transaction> history = library.getTransactionsForMember(memberId);
        if (history.isEmpty()) {
            System.out.println("No transaction history for this member.");
            return;
        }
        history.forEach(System.out::println);
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
