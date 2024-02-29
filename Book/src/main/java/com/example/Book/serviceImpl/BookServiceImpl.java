package com.example.Book.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Book.entity.Book;
import com.example.Book.repository.BookRepository;
import com.example.Book.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired BookRepository bookRepository;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Book createBook(Book book) {

		return bookRepository.save(book);
	}

	@Override
	public List<Book> getAllBook() {

	    logger.info("Asynchronous method started in thread: {}", Thread.currentThread().getName());
		List<Book> books = bookRepository.findAll();
	    logger.info("Asynchronous method completed");
	    
	    return books;
	}

	@Override
	public Optional<Book> getBookByTitle(String title) {

		return bookRepository.getBookByTitle(title);
	}
	
	@Override
	public Book updateBook(Book book, long id) {

		Optional<Book> bookData = bookRepository.findById(id);
		if(bookData.isPresent()) {
			Book bookDB = bookData.get();
			bookDB.setTitle(book.getTitle());
			bookDB.setDescription(book.getDescription());
			bookDB.setGenre(book.getGenre());
			bookDB.setYearPublish(book.getYearPublish());
			return bookRepository.save(bookDB);
		}
		return null;
	}

	@Override
	public void deleteBookById(long id) {

		bookRepository.deleteById(id);
	}

	@Override
	public List<Book> getBooksByAuthorId(long authorId) {
    
		return bookRepository.getBooksByAuthorId(authorId);
    }

	/*@Override
	public Optional<Book> getBookByAuthor(String bookAuth) {

		return bookRepository.getBookByBookAuth(bookAuth);
	}*/
	
	/*@Override
	public Optional<Book> getBookById(long id) {
		return bookRepository.findById(id);
	}*/

}
