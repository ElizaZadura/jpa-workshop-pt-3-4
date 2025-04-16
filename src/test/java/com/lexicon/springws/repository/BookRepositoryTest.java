package com.lexicon.springws.repository;

import com.lexicon.springws.entity.Author;
import com.lexicon.springws.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.lexicon.springws.SpringWsApplication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = SpringWsApplication.class)
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Transactional
    public void testFindByIsbnIgnoreCase() {
        // Create and save an author first
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        entityManager.flush();
        
        // Create and save books with different ISBN cases
        Book book1 = new Book("ISBN-TEST-001", "Test Book 1", 14);
        book1.setAuthor(author);
        entityManager.persist(book1);

        Book book2 = new Book("ISBN-TEST-002", "Test Book 2", 14);
        book2.setAuthor(author);
        entityManager.persist(book2);
        entityManager.flush();

        // Test case-insensitive search with different cases
        Book foundBook1 = bookRepository.findByIsbnIgnoreCase("isbn-test-001");
        assertNotNull(foundBook1, "Should find book with lowercase ISBN");
        assertEquals("Test Book 1", foundBook1.getTitle());

        Book foundBook2 = bookRepository.findByIsbnIgnoreCase("ISBN-TEST-001");
        assertNotNull(foundBook2, "Should find book with uppercase ISBN");
        assertEquals("Test Book 1", foundBook2.getTitle());

        // Test non-existent ISBN
        Book notFoundBook = bookRepository.findByIsbnIgnoreCase("NON-EXISTENT-ISBN");
        assertNull(notFoundBook, "Should return null for non-existent ISBN");
    }

    @Test
    @Transactional
    public void testFindByTitleContains() {
        // Create and save an author first
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        entityManager.flush();
        
        // Create and save multiple books
        Book book1 = new Book("ISBN-TEST-003", "Spring in Action", 14);
        book1.setAuthor(author);
        Book book2 = new Book("ISBN-TEST-004", "Spring Boot", 14);
        book2.setAuthor(author);
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.flush();

        // Find by title containing "Spring"
        List<Book> books = bookRepository.findByTitleContains("Spring");
        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    @Transactional
    public void testFindByMaxLoanDaysLessThan() {
        // Create and save an author first
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        entityManager.flush();
        
        // Create and save books with different max loan days
        Book book1 = new Book("ISBN-TEST-005", "Book 1", 7);
        book1.setAuthor(author);
        Book book2 = new Book("ISBN-TEST-006", "Book 2", 14);
        book2.setAuthor(author);
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.flush();

        // Find books with max loan days less than 10
        List<Book> books = bookRepository.findByMaxLoanDaysLessThan(10);
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
    }
} 