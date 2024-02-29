package com.example.author.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.author.dao.response.AuthorResponse;
import com.example.author.dao.response.Book;
import com.example.author.entity.Author;
import com.example.author.service.AuthorService;
import com.example.author.exception.ResourceNotFoundException;
import com.example.author.repository.AuthorRepository;
import com.example.author.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class AuthorController {

    @Autowired 
    AuthorService authorService;

    @Autowired 
    AuthorRepository authorRepository;

    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Operation(summary = "Create a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/author")
    public ResponseEntity<?> createAuthor(@RequestBody @Valid Author author, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Author createAuthor = authorService.createAuthor(author);
            Map<String, Object> response = new HashMap<>();
            response.put("author", createAuthor);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Retrive All Author",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/author")
    public ResponseEntity<List<Author>> getAllAuthor() {
        try {
            List<Author> authors = authorService.getAllAuthor();
            return new ResponseEntity<>(authors, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Find an author by their penName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/author/{penName}")
    public ResponseEntity <?> getAuthorByPenName(@PathVariable String penName) {
        Optional<Author> authorData = authorService.getAuthorByPenName(penName);
        if (authorData.isPresent()) {
            return ResponseEntity.ok(authorData.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Author not found"));
        }
    }

    @Operation(summary = "Update a Author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PutMapping("/author/{id}")
    public ResponseEntity <Object> updateAuthor(@RequestBody Author author, @PathVariable long id) {
        try {
            Author updateAuthor = authorService.updateAuthor(author, id);
            return ResponseEntity.ok(updateAuthor);
        } catch (ResourceNotFoundException e) {
            logger.error("Country not found with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Country not found with ID: " + id));
        } catch (Exception e) {
            logger.error("Error occurred while updating country with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while updating country with ID: " + id));
        }
    }

    @Operation(summary = "Delete an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @DeleteMapping("/author/{id}")
    public ResponseEntity <Author> deleteById(@PathVariable long id) {
        try {
             authorService.deleteAuthorById(id);
             return ResponseEntity.noContent().build();
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    //IF One Author had only 1 book
    /*@GetMapping("/author/book/{id}")
    public ResponseEntity<?> fetchAuthorById(@PathVariable long id) {
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()) {
            Book book = restTemplate.getForObject("http://localhost:8081/api/v1/book/id/" + author.get().getBookId(), Book.class);
            AuthorResponse authorResponse = new AuthorResponse(
                author.get().getId(),
                author.get().getFirstName(),
                author.get().getLastName(),
                author.get().getAge(),
                author.get().getNationality(),
                book
            );
            return new ResponseEntity<>(authorResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No Books Found", HttpStatus.NOT_FOUND);
        }
    }*/
    
    //One Author can had multiple book
    @Operation(summary = "Get all book by author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve all books by author",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/author/books/{id}")
    public ResponseEntity<?> fetchAuthorById(@PathVariable long id, HttpServletRequest request) {
        // Get JWT token from request headers
        String jwtToken = request.getHeader("Authorization");
        
        // Set JWT token in the headers of the request to the book microservice
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);

        // Make a request to the book microservice endpoint to fetch books by author ID
        ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(
            "http://localhost:8081/api/v1/books/author/{authorId}",
            HttpMethod.GET,
            new HttpEntity<>(headers), // Pass the headers with JWT token
            new ParameterizedTypeReference<List<Book>>() {},
            id
        );

        // Process the response from the book microservice
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            List<Book> books = responseEntity.getBody();
            Author author = authorRepository.findById(id).orElse(null);
            if (author != null) {
                AuthorResponse authorResponse = new AuthorResponse(
                    author.getId(),
                    author.getFirstName(),
                    author.getLastName(),
                    author.getAge(),
                    author.getNationality(),
                    books
                );
                return new ResponseEntity<>(authorResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Failed to fetch books", responseEntity.getStatusCode());
        }
    }
}