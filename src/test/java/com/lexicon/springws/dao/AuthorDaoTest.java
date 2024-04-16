package com.lexicon.springws.dao;

import com.lexicon.springws.entity.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.lexicon.springws.SpringWsApplication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = SpringWsApplication.class)
@ComponentScan(basePackages = {"com.lexicon.springws.dao"})
@ActiveProfiles("test")
public class AuthorDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorDao authorDao;

    @Test
    @Transactional
    public void testSaveAndFindById() {
        // Create and save an author
        Author author = new Author("John Doe", "john.doe@example.com");
        entityManager.persist(author);
        entityManager.flush();

        // Find the author by ID
        Author foundAuthor = authorDao.findById(author.getAuthorId());

        // Assertions
        assertNotNull(foundAuthor);
        assertEquals("John Doe", foundAuthor.getName());
        assertEquals("john.doe@example.com", foundAuthor.getEmail());
    }

    @Test
    @Transactional
    public void testFindAll() {
        // Create and save multiple authors
        Author author1 = new Author("John Doe", "john.doe@example.com");
        Author author2 = new Author("Jane Smith", "jane.smith@example.com");
        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.flush();

        // Find all authors
        List<Author> authors = authorDao.findAll();

        // Assertions
        assertNotNull(authors);
        assertTrue(authors.size() >= 2);
    }

    @Test
    @Transactional
    public void testUpdate() {
        // Create and save an author
        Author author = new Author("John Doe", "john.doe@example.com");
        entityManager.persist(author);
        entityManager.flush();

        // Update the author
        author.setName("John Updated");
        author.setEmail("john.updated@example.com");
        Author updatedAuthor = authorDao.update(author);

        // Assertions
        assertEquals("John Updated", updatedAuthor.getName());
        assertEquals("john.updated@example.com", updatedAuthor.getEmail());
    }

    @Test
    @Transactional
    public void testDelete() {
        // Create and save an author
        Author author = new Author("John Doe", "john.doe@example.com");
        entityManager.persist(author);
        entityManager.flush();

        // Delete the author
        authorDao.delete(author);

        // Try to find the deleted author
        Author foundAuthor = authorDao.findById(author.getAuthorId());

        // Assertion
        assertNull(foundAuthor);
    }

    @Test
    @Transactional
    public void testFindByEmail() {
        // Create and save an author
        Author author = new Author("John Doe", "john.doe@example.com");
        entityManager.persist(author);
        entityManager.flush();

        // Find author by email
        Author foundAuthor = authorDao.findByEmail("john.doe@example.com");

        // Assertions
        assertNotNull(foundAuthor);
        assertEquals("John Doe", foundAuthor.getName());
    }

    @Test
    @Transactional
    public void testFindByName() {
        // Create and save multiple authors
        Author author1 = new Author("John Doe", "john.doe@example.com");
        Author author2 = new Author("John Smith", "john.smith@example.com");
        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.flush();

        // Find authors by name
        List<Author> authors = authorDao.findByName("John");

        // Assertions
        assertNotNull(authors);
        assertTrue(authors.size() >= 2);
    }
} 