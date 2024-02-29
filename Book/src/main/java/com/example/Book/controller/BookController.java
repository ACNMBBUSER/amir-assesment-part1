package com.example.Book.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Book.entity.Book;
import com.example.Book.service.BookService;
import com.example.Book.Exception.ResourceNotFoundException;
import com.example.Book.Exception.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class BookController {
	
	@Autowired BookService bookService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Operation(summary = "Create a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
	@PostMapping("/book")
	public ResponseEntity<?> createBook(@RequestBody @Valid Book book,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        List<String> errors = new ArrayList<>();
	        bindingResult.getAllErrors().forEach(error -> {
	            errors.add(error.getDefaultMessage());
	        });
	        return ResponseEntity.badRequest().body(errors);
	    }
	    try {
	        Book createdBook = bookService.createBook(book);
	        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
	    } catch(Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    } 		
	}
	
	@Operation(summary = "Get all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Retrive All Books",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
	@GetMapping("/book")
	public ResponseEntity <List<Book>> getAllBook(){
		try {
		  List <Book> books = bookService.getAllBook();	
		  return new ResponseEntity<>(books,HttpStatus.OK);
		}catch(Exception e){
		  return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Operation(summary = "Find a book by their title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
	@GetMapping("/book/{title}")
	public ResponseEntity <?> getBookByTitle(@PathVariable String title){
		  Optional <Book> bookData = bookService.getBookByTitle(title);
		  if (bookData.isPresent()) {
	            return ResponseEntity.ok(bookData.get());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ErrorResponse("Author not found"));
	        }
	}
	
	@Operation(summary = "Find a book by author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
	@GetMapping("/books/author/{authorId}")
	public ResponseEntity<List<Book>> getBooksByAuthorId(@PathVariable long authorId) {
	    try {
	        List<Book> books = bookService.getBooksByAuthorId(authorId);
	        if (books.isEmpty()) {
	            return ResponseEntity.notFound().build();
	        }
	        return ResponseEntity.ok(books);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@Operation(summary = "Update a Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
	@PutMapping("/book/{id}")
	public ResponseEntity<Object> updateBook(@RequestBody Book book,@PathVariable long id) {
		try {
		  Book updateBook = bookService.updateBook(book, id);
		  return ResponseEntity.ok(updateBook);
        } catch (ResourceNotFoundException e) {
            logger.error("Country not found with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ErrorResponse("Book not found with ID: " + id));
        } catch (Exception e) {
            logger.error("Error occurred while updating country with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("An error occurred while updating Book with ID: " + id));
        }
	}
	
	@Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
	@DeleteMapping("/book/{id}")
	public ResponseEntity<Book> deleteBookById(@PathVariable long id) {
		try {
			bookService.deleteBookById(id);
			 return ResponseEntity.noContent().build();
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
		}
	}

	/*@GetMapping("/book/id/{id}")
	public ResponseEntity <?> getBookById(@PathVariable long id){
		  Optional <Book> bookData = bookService.getBookById(id);
		  if (bookData.isPresent()) {
	            return ResponseEntity.ok(bookData.get());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ErrorResponse("Author not found"));
	        }
	}*/
}
