package com.example.author.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.author.entity.Author;

public interface AuthorRepository extends JpaRepository <Author,Long>{

	Optional<Author> getAuthorByPenName(String penName);
}
