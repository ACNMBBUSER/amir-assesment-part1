package com.example.Book.service;

import java.util.List;
import java.util.Optional;

import com.example.Book.entity.Book;

public interface BookService {

	Book createBook (Book book);
	List<Book> getAllBook();
	List<Book> getBooksByAuthorId(long authorId);
	Optional<Book> getBookByTitle(String title);
	//Optional<Book>getBookById(long id);
	//Optional<Book> getBookByAuthor(String bookAuth);
	Book updateBook (Book book, long id);
	void deleteBookById(long id);
}
