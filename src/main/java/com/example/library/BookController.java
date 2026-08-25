package com.example.library;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // Add book
    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        book.setBookId(idCounter.getAndIncrement());
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    // Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    // Search book by id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return books.stream()
                .filter(b -> b.getBookId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update availability
    @PutMapping("/{id}/availability")
    public ResponseEntity<Book> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        for (Book book : books) {
            if (book.getBookId().equals(id)) {
                book.setAvailable(available);
                return ResponseEntity.ok(book);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean removed = books.removeIf(b -> b.getBookId().equals(id));
        if (removed) {
            return ResponseEntity.ok("Book deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
