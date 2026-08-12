# Library Management System (Java)

A console-based Library Management System built with **Core Java** — no frameworks,
no database server, just clean OOP design, collections, exception handling, and file I/O.
Designed to be a complete, working project you can put on your resume and confidently
walk through in an interview.

## Features

- **Book catalogue** — add, remove, search (by title/author), list sorted alphabetically
- **Member management** — register and list library members
- **Issue / Return workflow** — tracks available copies, prevents issuing unavailable books
- **Transaction history** — every issue/return is logged with a date; view a member's full history
- **Currently issued report** — see every book that's out and who has it
- **Persistent storage** — all data is saved to CSV files in `data/` and reloaded on startup,
  so nothing is lost between runs (no database required)
- **Custom exception hierarchy** — `LibraryException` as a base class with
  `BookNotFoundException`, `MemberNotFoundException`, `BookNotAvailableException`,
  and `DuplicateEntryException` for precise, meaningful error handling

## What it demonstrates

| Concept | Where |
|---|---|
| OOP (encapsulation, inheritance) | `model/` package, `LibraryException` hierarchy |
| Collections Framework (`HashMap`, `ArrayList`) | `LibraryService` for O(1) lookups |
| Java Streams & Lambdas | Searching/sorting/filtering in `LibraryService` |
| Custom checked exceptions | `exception/` package |
| File I/O (`java.nio.file`) | `util/FileManager` |
| Enums | `Genre`, `Transaction.Type` |
| Clean layered architecture | `model` / `service` / `util` separation |

## Project structure

```
LibraryManagementSystem/
├── src/
│   └── com/library/
│       ├── Main.java                  # Console menu / entry point
│       ├── model/
│       │   ├── Book.java
│       │   ├── Member.java
│       │   ├── Transaction.java
│       │   └── Genre.java
│       ├── exception/
│       │   ├── LibraryException.java
│       │   ├── BookNotFoundException.java
│       │   ├── BookNotAvailableException.java
│       │   ├── MemberNotFoundException.java
│       │   └── DuplicateEntryException.java
│       ├── service/
│       │   └── LibraryService.java    # Core business logic
│       └── util/
│           └── FileManager.java       # Generic CSV read/write helper
├── data/                              # Created automatically at runtime
└── README.md
```

## How to run

Requires JDK 17+ (uses switch expressions and `var`-friendly syntax).

```bash
# From the project root
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin @sources.txt

java -cp bin com.library.Main
```

On exit (or after any change), your data is saved to `data/books.csv`,
`data/members.csv`, and `data/transactions.csv`. Delete the `data/` folder
at any time to start fresh.

## Sample workflow

1. Choose `1` to add a book (ISBN, title, author, genre, copies)
2. Choose `4` to register a member
3. Choose `6` to issue that book to that member
4. Choose `8` to see it listed as currently issued
5. Choose `7` to return it
