package com.hcltech.courseacademy.controller;

import com.hcltech.courseacademy.dto.CourseDTO;
import com.hcltech.courseacademy.service.CourseService;

import org.slf4j.Logger;         // CORRECT: Import SLF4J Logger
import org.slf4j.LoggerFactory;  // CORRECT: Import SLF4J LoggerFactory
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException; // Needed for error handling
// REMOVE THIS LINE: import java.util.logging.Logger; // This caused the ClassCastException

@RestController
@RequestMapping("/api/courses") // Recommended: Add a base request mapping for clarity
public class CourseController {

    // --- FIX IS HERE: Correct Logger initialization for SLF4J ---
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) { // Added @RequestBody
        logger.info("Received request to create course: {}", courseDTO.getTitle()); // Improved log message
        try {
            CourseDTO createdCourse = courseService.createCourse(courseDTO);
            logger.info("Course created successfully with ID: {}", createdCourse.getId());
            return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
        } catch (NoSuchElementException e) { // For cases where related entities (e.g., Author) are not found
            logger.error("Error creating course: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) { // For validation failures or business logic errors
            logger.error("Validation error during course creation: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during course creation: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        logger.info("Received request to get course by ID: {}", id); // Improved log message
        try {
            CourseDTO course = courseService.getCourseById(id);
            logger.info("Successfully retrieved course with ID: {}", id);
            return ResponseEntity.ok(course);
        } catch (NoSuchElementException e) {
            logger.error("Course not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving course with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        logger.info("Received request to get all courses.");
        try {
            List<CourseDTO> courses = courseService.getAllCourses();
            logger.info("Successfully retrieved {} courses.", courses.size());
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all courses: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO courseDTO) {
        logger.info("Received request to update course with ID: {}, details: {}", id, courseDTO.getTitle()); // Improved log message
        try {
            CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
            logger.info("Course with ID {} updated successfully.", id);
            return ResponseEntity.ok(updatedCourse);
        } catch (NoSuchElementException e) {
            logger.error("Course not found for update with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during course update for ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during course update for ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        logger.info("Received request to delete course with ID: {}", id); // Improved log message
        try {
            courseService.deleteCourse(id);
            logger.info("Course with ID {} deleted successfully.", id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            logger.error("Course not found for deletion with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during course deletion for ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}