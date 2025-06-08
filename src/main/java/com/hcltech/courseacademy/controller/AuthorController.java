package com.hcltech.courseacademy.controller;

import com.hcltech.courseacademy.dto.AuthorDTO;
import com.hcltech.courseacademy.dto.CourseDTO;
import com.hcltech.courseacademy.service.AuthorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException; // <--- NEW IMPORT for specific error handling
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        logger.info("Received request to create author with email: {}", authorDTO.getEmail());
        try {
            AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
            logger.info("Author created successfully with ID: {}", createdAuthor.getId());
            return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Validation/Business Logic error during author creation: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) { // <--- NEW CATCH BLOCK
            logger.error("Database constraint violation during author creation: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            logger.error("An unexpected internal error occurred during author creation: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        logger.info("Received request to get all authors.");
        try {
            List<AuthorDTO> authors = authorService.getAllAuthors();
            logger.info("Successfully retrieved {} authors.", authors.size());
            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all authors: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        logger.info("Received request to get author by ID: {}", id);
        try {
            AuthorDTO author = authorService.getAuthorById(id);
            logger.info("Successfully retrieved author with ID: {}", id);
            return ResponseEntity.ok(author);
        } catch (NoSuchElementException e) {
            logger.error("Author not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving author with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        logger.info("Received request to update author with ID: {}", id);
        try {
            AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
            logger.info("Author with ID {} updated successfully.", id);
            return ResponseEntity.ok(updatedAuthor);
        } catch (NoSuchElementException e) {
            logger.error("Author not found for update with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during author update for ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) { // <--- NEW CATCH BLOCK
            logger.error("Database constraint violation during author update: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during author update for ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        logger.info("Received request to delete author with ID: {}", id);
        try {
            authorService.deleteAuthor(id);
            logger.info("Author with ID {} deleted successfully.", id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            logger.error("Author not found for deletion with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during author deletion for ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{authorId}/courses/{courseId}")
    public ResponseEntity<CourseDTO> assignCourseToAuthor(
            @PathVariable Long authorId,
            @PathVariable Long courseId) {
        logger.info("Received request to assign course ID {} to author ID {}.", courseId, authorId);
        try {
            CourseDTO updatedCourse = authorService.assignCourseToAuthor(courseId, authorId);
            logger.info("Course ID {} successfully assigned to author ID {}.", courseId, authorId);
            return ResponseEntity.ok(updatedCourse);
        } catch (NoSuchElementException e) {
            logger.error("Error assigning course: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during course assignment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during course assignment: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/courses/{courseId}/remove-author")
    public ResponseEntity<CourseDTO> removeCourseFromAuthor(@PathVariable Long courseId) {
        logger.info("Received request to remove author from course ID {}.", courseId);
        try {
            CourseDTO updatedCourse = authorService.removeCourseFromAuthor(courseId);
            logger.info("Author successfully removed from course ID {}.", courseId);
            return ResponseEntity.ok(updatedCourse);
        } catch (NoSuchElementException e) {
            logger.error("Error removing author from course: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while removing author from course ID {}: {}", courseId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{authorId}/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesByAuthor(@PathVariable Long authorId) {
        logger.info("Received request to get courses for author ID {}.", authorId);
        try {
            List<CourseDTO> courses = authorService.getCoursesByAuthor(authorId);
            logger.info("Successfully retrieved {} courses for author ID {}.", courses.size(), authorId);
            return ResponseEntity.ok(courses);
        } catch (NoSuchElementException e) {
            logger.error("Author not found when trying to get courses for ID {}: {}", authorId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving courses for author ID {}: {}", authorId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
