package com.lexicon.springws.repository;

import com.lexicon.springws.entity.Author;
import com.lexicon.springws.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByIsbnIgnoreCase(String isbn);
    
    List<Book> findByTitleContains(String title);
    
    List<Book> findByMaxLoanDaysLessThan(int maxLoanDays);
    
    List<Book> findByAuthor(Author author);
} 