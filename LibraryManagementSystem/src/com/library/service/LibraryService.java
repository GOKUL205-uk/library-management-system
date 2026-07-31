package com.library.service;

import com.library.exception.*;
import com.library.model.*;
import com.library.util.FileManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core business logic of the Library Management System.
 * Holds books and members in memory (backed by HashMap for O(1) lookup)
 * and persists all data to disk on every change.
 */
public class LibraryService {

    private static final String BOOKS_FILE = "data/books.csv";
    private static final String MEMBERS_FILE = "data/members.csv";
    private static final String TRANSACTIONS_FILE = "data/transactions.csv";

    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public LibraryService() {
        loadData();
    }

    // ---------- Book operations ----------

    public void addBook(Book book) throws DuplicateEntryException {
        if (books.containsKey(book.getIsbn())) {
            throw new DuplicateEntryException("A book with ISBN " + book.getIsbn() + " already exists.");
        }
        books.put(book.getIsbn(), book);
        saveBooks();
    }

    public Book findBook(String isbn) throws BookNotFoundException {
        Book book = books.get(isbn);
        if (book == null) throw new BookNotFoundException(isbn);
        return book;
    }

    public void removeBook(String isbn) throws BookNotFoundException {
        if (!books.containsKey(isbn)) throw new BookNotFoundException(isbn);
        books.remove(isbn);
        saveBooks();
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Book> searchBooksByTitle(String keyword) {
        String lower = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByAuthor(String keyword) {
        String lower = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Book> getBooksSortedByTitle() {
        return books.values().stream()
                .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // ---------- Member operations ----------

    public void registerMember(Member member) throws DuplicateEntryException {
        if (members.containsKey(member.getMemberId())) {
            throw new DuplicateEntryException("A member with ID " + member.getMemberId() + " already exists.");
        }
        members.put(member.getMemberId(), member);
        saveMembers();
    }

    public Member findMember(String memberId) throws MemberNotFoundException {
        Member member = members.get(memberId);
        if (member == null) throw new MemberNotFoundException(memberId);
        return member;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    // ---------- Issue / Return workflow ----------

    public void issueBook(String isbn, String memberId)
            throws BookNotFoundException, MemberNotFoundException, BookNotAvailableException {
        Book book = findBook(isbn);
        findMember(memberId); // validates member exists
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(book.getTitle());
        }
        book.decrementAvailable();
        transactions.add(new Transaction(isbn, memberId, Transaction.Type.ISSUE, LocalDate.now()));
        saveBooks();
        saveTransactions();
    }

    public void returnBook(String isbn, String memberId) throws BookNotFoundException, MemberNotFoundException {
        Book book = findBook(isbn);
        findMember(memberId);
        book.incrementAvailable();
        transactions.add(new Transaction(isbn, memberId, Transaction.Type.RETURN, LocalDate.now()));
        saveBooks();
        saveTransactions();
    }

    public List<Transaction> getTransactionsForMember(String memberId) {
        return transactions.stream()
                .filter(t -> t.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    /** Returns every book currently issued out (available < total), with borrower info. */
    public List<Transaction> getCurrentlyIssuedBooks() {
        // A book issued but not yet returned: count ISSUE minus RETURN per isbn+member pair
        Map<String, Integer> netIssued = new LinkedHashMap<>();
        Map<String, Transaction> lastTransaction = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            String key = t.getIsbn() + "|" + t.getMemberId();
            int delta = t.getType() == Transaction.Type.ISSUE ? 1 : -1;
            netIssued.merge(key, delta, Integer::sum);
            lastTransaction.put(key, t);
        }
        List<Transaction> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : netIssued.entrySet()) {
            if (entry.getValue() > 0) {
                result.add(lastTransaction.get(entry.getKey()));
            }
        }
        return result;
    }

    // ---------- Persistence ----------

    private void loadData() {
        FileManager.readAll(BOOKS_FILE, Book::fromCsv).forEach(b -> books.put(b.getIsbn(), b));
        FileManager.readAll(MEMBERS_FILE, Member::fromCsv).forEach(m -> members.put(m.getMemberId(), m));
        transactions.addAll(FileManager.readAll(TRANSACTIONS_FILE, Transaction::fromCsv));
    }

    private void saveBooks() {
        FileManager.writeAll(BOOKS_FILE, new ArrayList<>(books.values()), Book::toCsv);
    }

    private void saveMembers() {
        FileManager.writeAll(MEMBERS_FILE, new ArrayList<>(members.values()), Member::toCsv);
    }

    private void saveTransactions() {
        FileManager.writeAll(TRANSACTIONS_FILE, transactions, Transaction::toCsv);
    }
}
