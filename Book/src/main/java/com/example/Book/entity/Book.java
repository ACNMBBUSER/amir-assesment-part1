package com.example.Book.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="book")
public class Book {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Size(min = 5, message = "ISBN should have at least 5 characters")
    private String isbn;

    @Column(nullable = false, length = 100, unique = true)
    @NotBlank(message = "Title cannot be blank")
    @Length(min = 5, max = 100, message = "Title must be between 5-100 characters")
    private String title;

    @Column(nullable = false, length = 100, unique = true)
    @NotBlank(message = "Description cannot be blank")
    @Length(min = 10, max = 100, message = "Description must be between 10-100 characters")
    private String description;

    private long authorId;
    
    @Column(nullable = false, length = 100, unique = true)
    @NotBlank(message = "Genre cannot be blank")
    @Length(min = 5, max = 10, message = "Genre must be between 5-10 characters")
    private String genre;

    @Min(value = 1900, message = "Book publish must be greater or equal to 1900")
    @Max(value = 9999, message = "Book publish must be less than or equal to 2024")
    private int yearPublish;
}