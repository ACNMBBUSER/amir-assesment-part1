package com.example.author.service;

import java.util.List;
import java.util.Optional;

import com.example.author.entity.Author;

public interface AuthorService {

	Author createAuthor(Author author);
	List<Author> getAllAuthor();
	Optional<Author> getAuthorByPenName(String penName);
	Author updateAuthor(Author author, long id);
	void deleteAuthorById(long id);
}
