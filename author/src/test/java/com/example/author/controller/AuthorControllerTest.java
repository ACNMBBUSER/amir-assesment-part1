package com.example.author.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.author.entity.Author;
import com.example.author.repository.AuthorRepository;
import com.example.author.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

	@MockBean
	private AuthorService authorService;
	
	@MockBean
	private AuthorRepository authorRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	//COMMON
	Author author = new Author(1, "James","Morrison", "JM", 25, "Germany");
	long id = author.getId();
    String penName = author.getPenName();
    
    //Use for update Test
    Author updateAuthor = new Author(1, "James","Morrison", "JM", 26, "Ukraine");
    String updateNationality = updateAuthor.getNationality();
	
	@Test
	void shouldCreateAuthor() throws Exception {
			
		mockMvc.perform(post("/api/v1/author").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(author)))
				.andExpectAll(status().isCreated())
				.andDo(print());		
	}
	
	@Test
	void shouldReturnListOfAuthor() throws Exception{
		
		List<Author> authors = new ArrayList<>(
			 Arrays.asList(new Author(1, "James","Morrison", "JM", 25, "Germany"),
					 new Author(2, "Garry","Bule", "GB", 29, "England"),
					 new Author(3, "Spade","Wilson", "SW", 24, "Spain")));
		
        when(authorService.getAllAuthor()).thenReturn(authors);
		mockMvc.perform(get("/api/v1/author"))
		.andExpectAll(status().isOk())
		.andExpect(jsonPath("$.size()").value(authors.size()))
		.andDo(print());   
	}
	
	@Test
	void shouldReturnAuthor() throws Exception{
				
		when(authorService.getAuthorByPenName(penName)).thenReturn(Optional.of(author));
		mockMvc.perform(get("/api/v1/author/{penName}",penName))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(id))
		.andExpect(jsonPath("$.penName").value(penName))
		.andDo(print());
	}
	
	/*@Test
	void shouldReturnNotFoundAuthor() throws Exception{
		
		String penName="Ahmad";
		
		when(authorService.getAuthorByPenName(penName)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/v1/author/{penName}",penName))
	    .andExpect(status().isNotFound())
		.andDo(print());
	}*/
		
	@Test
	void shouldUpdateAuthor() throws Exception{
		
		when(authorRepository.findById(id)).thenReturn(Optional.of(author));
		when(authorService.updateAuthor(any(Author.class),any(Long.class)))
		.thenReturn(updateAuthor);
		
		 mockMvc.perform(put("/api/v1/author/{id}", id)
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(mapper.writeValueAsString(updateAuthor)))
			        .andExpect(status().isOk())
			        .andExpect(jsonPath("$.nationality").value(updateNationality))
			        .andDo(print())
			        .andExpect(content().json("{}"));		
	}
	
	@Test
	void shouldDeleteAuthor() throws Exception{
		
		doNothing().when(authorService).deleteAuthorById(id);
		mockMvc.perform(delete("/api/v1/author/{id}",id))
		.andExpect(status().isNoContent())
		.andDo(print());
	}
}
