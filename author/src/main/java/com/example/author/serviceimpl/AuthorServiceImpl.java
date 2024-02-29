package com.example.author.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.author.entity.Author;
import com.example.author.repository.AuthorRepository;
import com.example.author.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

	@Autowired AuthorRepository authorRepository;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Author createAuthor(Author author) {
 
		return authorRepository.save(author);
	}

	@Async
	@Override
	public List<Author> getAllAuthor() {

	    logger.info("Asynchronous method started in thread: {}", Thread.currentThread().getName());
		List<Author> authors = authorRepository.findAll();
	    logger.info("Asynchronous method completed");

	    return authors;
	}

	@Override
	public Optional<Author> getAuthorByPenName(String penName) {

		return authorRepository.getAuthorByPenName(penName);
	}
	
	@Override
	public Author updateAuthor(Author author, long id) {

		Optional<Author> authorData = authorRepository.findById(id);
		if(authorData.isPresent()) {
			Author authorDB = authorData.get();
			authorDB.setFirstName(author.getFirstName());
			authorDB.setLastName(author.getLastName());
			authorDB.setPenName(author.getPenName());
			authorDB.setNationality(author.getNationality());
			return authorRepository.save(authorDB);
		}
		return null;
	}

	@Override
	public void deleteAuthorById(long id) {
		// TODO Auto-generated method stub
		authorRepository.deleteById(id);
	}

}

