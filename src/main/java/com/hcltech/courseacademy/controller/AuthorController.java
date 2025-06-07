package com.hcltech.courseacademy.controller;

import com.hcltech.courseacademy.dto.AuthorDTO;
import com.hcltech.courseacademy.dto.CourseDTO;
import com.hcltech.courseacademy.entity.Author;
import com.hcltech.courseacademy.service.AuthorService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController

public class AuthorController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @PostMapping
    public ResponseEntity<Author> createAuthor(@Valid AuthorDTO authorDTO) {
        logger.info("Received request to create author: ");
        Author createdAuthor = authorService.createAuthor(authorDTO);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        logger.info("received request to get all authors.");
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthById(@PathVariable Long id) {
        logger.info("received request to get all authors by id: ");
        Author authors = authorService.getAuthorById(id);
        return ResponseEntity.ok(authors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid AuthorDTO authorDTO) {
        logger.info("received request to update author with ID: ");
        Author updatedAuthor = authorService.updateAuthor(id,authorDTO);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        logger.info("received request to delete author with ID:");
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{authorId}/courses/{courseId}")
    public ResponseEntity<CourseDTO> assignCourseToAuthor(
            @PathVariable Long authorId,
            @PathVariable Long courseId) {
        logger.info("received request to assign course ID {} to author ID {}.");
        CourseDTO updatedCourse = authorService.assignCourseToAuthor(courseId, authorId);
        return ResponseEntity.ok(updatedCourse);
    }


    @DeleteMapping("/courses/{courseId}/remove-author")
    public ResponseEntity<CourseDTO> removeCourseFromAuthor(@PathVariable Long courseId) {
        logger.info("received request to remove author from course ID {}.");
        CourseDTO updatedCourse = authorService.removeCourseFromAuthor(courseId);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping("/{authorId}/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesByAuthor(@PathVariable Long authorId) {
        logger.info("received request to get courses for author ID {}.");
        List<CourseDTO> courses = authorService.getCoursesByAuthor(authorId);
        return ResponseEntity.ok(courses);
    }
}

