package com.lexicon.springws.dao;

import com.lexicon.springws.entity.Author;
import java.util.List;

public interface AuthorDao {
    Author save(Author author);
    Author findById(Long id);
    List<Author> findAll();
    Author update(Author author);
    void delete(Author author);
    
    // Additional query methods
    Author findByEmail(String email);
    List<Author> findByName(String name);
} 