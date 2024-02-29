package com.example.author.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	 private long id;
	 private String isbn;
	 //private long authId;
	 private String title;
	 private String description;
	 private String genre;
	 private int yearPublish;
}
