package com.lexicon.springws;

import com.lexicon.springws.entity.Author;
import com.lexicon.springws.entity.Book;
import com.lexicon.springws.entity.AppUser;
import com.lexicon.springws.entity.BookLoan;
import com.lexicon.springws.repository.AuthorRepository;
import com.lexicon.springws.repository.BookRepository;
import com.lexicon.springws.repository.AppUserRepository;
import com.lexicon.springws.repository.BookLoanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class SpringWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWsApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(AuthorRepository authorRepository,
                                BookRepository bookRepository,
                                AppUserRepository appUserRepository,
                                BookLoanRepository bookLoanRepository) {
        return args -> {
            // Create and save an author
            Author author = new Author("Test Author", "test@example.com");
            author = authorRepository.save(author);
            System.out.println("Saved author: " + author.getName());

            // Create and save a book
            Book book = new Book();
            book.setIsbn("1234567890");
            book.setTitle("Test Book");
            book.setMaxLoanDays(14);
            book.setAuthor(author);
            book = bookRepository.save(book);
            System.out.println("Saved book: " + book.getTitle());

            // Create and save a user
            AppUser user = new AppUser();
            user.setUsername("testuser");
            user.setPassword("password");
            user = appUserRepository.save(user);
            System.out.println("Saved user: " + user.getUsername());

            // Create and save a book loan
            BookLoan loan = new BookLoan();
            loan.setBook(book);
            loan.setBorrower(user);
            loan.setLoanDate(LocalDate.now());
            loan.setDueDate(LocalDate.now().plusDays(14));
            loan = bookLoanRepository.save(loan);
            System.out.println("Saved book loan for book: " + loan.getBook().getTitle());

            // Test finding books by author
            System.out.println("\nBooks by author:");
            bookRepository.findByAuthor(author).forEach(b -> 
                System.out.println("- " + b.getTitle()));

            // Test finding active loans
            System.out.println("\nActive loans:");
            bookLoanRepository.findByReturnDateIsNull().forEach(l -> 
                System.out.println("- Book: " + l.getBook().getTitle() + 
                                 ", Borrower: " + l.getBorrower().getUsername()));

            // Test marking a loan as returned
            System.out.println("\nMarking loan as returned...");
            int updated = bookLoanRepository.markAsReturned(loan.getId());
            System.out.println("Updated " + updated + " loan(s)");

            // Verify the loan is marked as returned
            BookLoan returnedLoan = bookLoanRepository.findById(loan.getId()).orElse(null);
            if (returnedLoan != null && returnedLoan.getReturnDate() != null) {
                System.out.println("Loan successfully marked as returned on: " + returnedLoan.getReturnDate());
            }
        };
    }
} 