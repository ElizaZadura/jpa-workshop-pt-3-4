package com.lexicon.springws.repository;

import com.lexicon.springws.entity.Author;
import com.lexicon.springws.entity.Book;
import com.lexicon.springws.entity.BookLoan;
import com.lexicon.springws.entity.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookLoanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @Transactional
    public void testFindByBorrowerId() {
        // Create and save an author
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        
        // Create and save a book
        Book book = new Book();
        book.setIsbn("ISBN123461");
        book.setTitle("Test Book");
        book.setMaxLoanDays(14);
        book.setAuthor(author);
        entityManager.persist(book);

        // Create and save a user
        AppUser user = new AppUser();
        user.setUsername("testuser1");
        user.setPassword("password");
        entityManager.persist(user);

        // Create and save a book loan
        BookLoan loan = new BookLoan();
        loan.setBook(book);
        loan.setBorrower(user);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        entityManager.persist(loan);
        entityManager.flush();

        // Find loans by borrower ID
        List<BookLoan> loans = bookLoanRepository.findByBorrowerId(user.getId());
        assertNotNull(loans);
        assertEquals(1, loans.size());
        assertEquals(book.getId(), loans.get(0).getBook().getId());
    }

    @Test
    @Transactional
    public void testFindByBookId() {
        // Create and save an author
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        
        // Create and save a book
        Book book = new Book();
        book.setIsbn("ISBN123462");
        book.setTitle("Test Book");
        book.setMaxLoanDays(14);
        book.setAuthor(author);
        entityManager.persist(book);

        // Create and save a user
        AppUser user = new AppUser();
        user.setUsername("testuser2");
        user.setPassword("password");
        entityManager.persist(user);

        // Create and save a book loan
        BookLoan loan = new BookLoan();
        loan.setBook(book);
        loan.setBorrower(user);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        entityManager.persist(loan);
        entityManager.flush();

        // Find loans by book ID
        List<BookLoan> loans = bookLoanRepository.findByBookId(book.getId());
        assertNotNull(loans);
        assertEquals(1, loans.size());
        assertEquals(user.getId(), loans.get(0).getBorrower().getId());
    }

    @Test
    @Transactional
    public void testFindByReturnDateIsNull() {
        // Create and save an author
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        
        // Create and save a book
        Book book = new Book();
        book.setIsbn("ISBN123463");
        book.setTitle("Test Book");
        book.setMaxLoanDays(14);
        book.setAuthor(author);
        entityManager.persist(book);

        // Create and save a user
        AppUser user = new AppUser();
        user.setUsername("testuser3");
        user.setPassword("password");
        entityManager.persist(user);

        // Create and save an active book loan
        BookLoan activeLoan = new BookLoan();
        activeLoan.setBook(book);
        activeLoan.setBorrower(user);
        activeLoan.setLoanDate(LocalDate.now());
        activeLoan.setDueDate(LocalDate.now().plusDays(14));
        entityManager.persist(activeLoan);

        // Create and save a returned book loan
        BookLoan returnedLoan = new BookLoan();
        returnedLoan.setBook(book);
        returnedLoan.setBorrower(user);
        returnedLoan.setLoanDate(LocalDate.now().minusDays(30));
        returnedLoan.setDueDate(LocalDate.now().minusDays(16));
        returnedLoan.setReturnDate(LocalDate.now().minusDays(15));
        entityManager.persist(returnedLoan);
        entityManager.flush();

        // Find active loans
        List<BookLoan> activeLoans = bookLoanRepository.findByReturnDateIsNull();
        assertNotNull(activeLoans);
        assertEquals(1, activeLoans.size());
        assertEquals(activeLoan.getId(), activeLoans.get(0).getId());
    }

    @Test
    @Transactional
    public void testFindByDueDateBeforeAndReturnDateIsNull() {
        // Create and save an author
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        
        // Create and save a book
        Book book = new Book();
        book.setIsbn("ISBN123464");
        book.setTitle("Test Book");
        book.setMaxLoanDays(14);
        book.setAuthor(author);
        entityManager.persist(book);

        // Create and save a user
        AppUser user = new AppUser();
        user.setUsername("testuser4");
        user.setPassword("password");
        entityManager.persist(user);

        // Create and save an overdue book loan
        BookLoan overdueLoan = new BookLoan();
        overdueLoan.setBook(book);
        overdueLoan.setBorrower(user);
        overdueLoan.setLoanDate(LocalDate.now().minusDays(30));
        overdueLoan.setDueDate(LocalDate.now().minusDays(16));
        entityManager.persist(overdueLoan);

        // Create and save a current book loan
        BookLoan currentLoan = new BookLoan();
        currentLoan.setBook(book);
        currentLoan.setBorrower(user);
        currentLoan.setLoanDate(LocalDate.now());
        currentLoan.setDueDate(LocalDate.now().plusDays(14));
        entityManager.persist(currentLoan);
        entityManager.flush();

        // Find overdue loans
        List<BookLoan> overdueLoans = bookLoanRepository.findByDueDateBeforeAndReturnDateIsNull(LocalDate.now());
        assertNotNull(overdueLoans);
        assertEquals(1, overdueLoans.size());
        assertEquals(overdueLoan.getId(), overdueLoans.get(0).getId());
    }

    @Test
    @Transactional
    public void testFindByLoanDateBetween() {
        // Create and save an author
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        
        // Create and save a book
        Book book = new Book();
        book.setIsbn("ISBN123465");
        book.setTitle("Test Book");
        book.setMaxLoanDays(14);
        book.setAuthor(author);
        entityManager.persist(book);

        // Create and save a user
        AppUser user = new AppUser();
        user.setUsername("testuser5");
        user.setPassword("password");
        entityManager.persist(user);

        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        // Create and save a book loan within the date range
        BookLoan loan = new BookLoan();
        loan.setBook(book);
        loan.setBorrower(user);
        loan.setLoanDate(startDate.plusDays(15));
        loan.setDueDate(startDate.plusDays(29));
        entityManager.persist(loan);
        entityManager.flush();

        // Find loans between dates
        List<BookLoan> loans = bookLoanRepository.findByLoanDateBetween(startDate, endDate);
        assertNotNull(loans);
        assertEquals(1, loans.size());
        assertEquals(loan.getId(), loans.get(0).getId());
    }

    @Test
    @Transactional
    public void testMarkAsReturned() {
        // Create and save an author
        Author author = new Author("Test Author", "test@example.com");
        entityManager.persist(author);
        
        // Create and save a book
        Book book = new Book();
        book.setIsbn("ISBN123466");
        book.setTitle("Test Book");
        book.setMaxLoanDays(14);
        book.setAuthor(author);
        entityManager.persist(book);

        // Create and save a user
        AppUser user = new AppUser();
        user.setUsername("testuser6");
        user.setPassword("password");
        entityManager.persist(user);

        // Create and save a book loan
        BookLoan loan = new BookLoan();
        loan.setBook(book);
        loan.setBorrower(user);
        loan.setLoanDate(LocalDate.now().minusDays(7));
        loan.setDueDate(LocalDate.now().plusDays(7));
        entityManager.persist(loan);
        entityManager.flush();

        // Mark the loan as returned
        loan.setReturnDate(LocalDate.now());
        entityManager.persist(loan);
        entityManager.flush();

        // Verify the loan is marked as returned
        BookLoan foundLoan = entityManager.find(BookLoan.class, loan.getId());
        assertNotNull(foundLoan.getReturnDate());
    }
} 