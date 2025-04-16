package com.lexicon.springws.dao;

import com.lexicon.springws.entity.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AuthorDaoImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Author save(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public Author findById(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Author update(Author author) {
        return entityManager.merge(author);
    }

    @Override
    @Transactional
    public void delete(Author author) {
        entityManager.remove(author);
    }

    @Override
    public Author findByEmail(String email) {
        TypedQuery<Author> query = entityManager.createQuery(
            "SELECT a FROM Author a WHERE a.email = :email", Author.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Author> findByName(String name) {
        TypedQuery<Author> query = entityManager.createQuery(
            "SELECT a FROM Author a WHERE a.name LIKE :name", Author.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
} 