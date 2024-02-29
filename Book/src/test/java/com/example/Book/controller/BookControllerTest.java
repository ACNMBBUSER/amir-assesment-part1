package com.example.Book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.Book.entity.Book;
import com.example.Book.repository.BookRepository;
import com.example.Book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@WebMvcTest(BookController.class)
public class BookControllerTest {
	
	@MockBean
	private BookService bookService;
	
	@MockBean
	private BookRepository bookRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	//Common
	Book book = new Book(1, "sa300", "Harry Potter", "Book About Wizard and Mages", 1, "Fiction", 2007);
	long id = book.getId();
	String title = book.getTitle();
	
	//Update
	Book updateBook = new Book(1, "sa300", "Harry Pottah", "Book About Wizard and Mages",1, "Action", 2007);
	String updateTitle = updateBook.getTitle();
	
	@Test
	void shouldCreateBook() throws Exception{
		
		mockMvc.perform(post("/api/v1/book").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(book)))
		.andExpectAll(status().isCreated())
		.andDo(print());
	}

	@Test
	void shouldReturnListOfBook() throws Exception{
		
		List<Book> books = new ArrayList<>(
				Arrays.asList(new Book(1, "sa300", "Harry Potter", "Book About Wizard and Mages",1, "Fiction", 2007),
						new Book(2, "IB200", "Naruto", "Book About Ninja",1, "Fiction", 2007),
						new Book(3, "Z500", "One Piece", "Book About Pirate",1, "Adventure", 2007)));
		
		when(bookService.getAllBook()).thenReturn(books);
		mockMvc.perform(get("/api/v1/book"))
		.andExpectAll(status().isOk())
		.andExpect(jsonPath("$.size()").value(books.size()))
		.andDo(print());
	}
	
	@Test
	void shouldReturnBook() throws Exception{
		
		when(bookService.getBookByTitle(title)).thenReturn(Optional.of(book));
		mockMvc.perform(get("/api/v1/book/{title}",title))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(id))
		.andExpect(jsonPath("$.title").value(title))
		.andDo(print());
	}
	
	@Test
	void shouldUpdateBook() throws Exception{
		
		when(bookRepository.findById(id)).thenReturn(Optional.of(book));
		when(bookService.updateBook(any(Book.class),any(Long.class)))
		.thenReturn(updateBook);
		
		 mockMvc.perform(put("/api/v1/book/{id}", id)
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(mapper.writeValueAsString(updateBook)))
			        .andExpect(status().isOk())
			        .andExpect(jsonPath("$.title").value(updateTitle))
			        .andDo(print())
			        .andExpect(content().json("{}"));
	}
	
	@Test
	void shouldDeleteBook() throws Exception{
		
		doNothing().when(bookService).deleteBookById(id);
		mockMvc.perform(delete("/api/v1/book/{id}",id))
		.andExpect(status().isNoContent())
		.andDo(print());		
	}
}
