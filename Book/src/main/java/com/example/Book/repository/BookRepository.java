package com.example.Book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Book.entity.Book;

public interface BookRepository extends JpaRepository <Book,Long> {
	
	Optional<Book> getBookByTitle(String title);
	//Optional<Book> getBookByBookAuth(String bookAuth);
	List<Book> getBooksByAuthorId(long authorId);
}
