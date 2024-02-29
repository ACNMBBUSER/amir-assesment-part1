package com.example.author.dao.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
	
	 private long id;
	 private String firstName;
	 private String lastName;
	 private int age;
	 private String nationality;
	 //private Book book;
	 private List<Book> books;


}
