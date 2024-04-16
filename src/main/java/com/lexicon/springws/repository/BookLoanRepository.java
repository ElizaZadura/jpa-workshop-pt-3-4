package com.lexicon.springws.repository;

import com.lexicon.springws.entity.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    List<BookLoan> findByBorrowerId(Long borrowerId);
    
    List<BookLoan> findByBookId(Long bookId);
    
    List<BookLoan> findByReturnDateIsNull();
    
    List<BookLoan> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);
    
    List<BookLoan> findByLoanDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Modifying
    @Transactional
    @Query("UPDATE BookLoan bl SET bl.returnDate = CURRENT_DATE WHERE bl.id = :id")
    int markAsReturned(@Param("id") Long id);
} 