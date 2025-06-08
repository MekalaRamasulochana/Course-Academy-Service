package com.hcltech.courseacademy.controller;

import com.hcltech.courseacademy.dto.PurchasedCourseDTO;
import com.hcltech.courseacademy.service.PurchasedCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content; // For response content
import io.swagger.v3.oas.annotations.media.Schema; // For response schema

@RestController
@RequestMapping("/api/purchased-courses")
@Tag(name = "Purchased Courses Management", description = "APIs for managing purchased courses") // Updated @Api to @Tag
public class PurchasedCourseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchasedCourseController.class);
    private final PurchasedCourseService purchasedCourseService;

    public PurchasedCourseController(PurchasedCourseService purchasedCourseService) {
        this.purchasedCourseService = purchasedCourseService;
    }

    @PostMapping
    @Operation(summary = "Create a new purchased course record") // Updated @ApiOperation to @Operation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchased course created successfully",
                    content = @Content(schema = @Schema(implementation = PurchasedCourseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or required fields missing"),
            @ApiResponse(responseCode = "404", description = "Student or Course not found for the given IDs")
    })
    public ResponseEntity<PurchasedCourseDTO> createPurchasedCourse(@Valid @RequestBody PurchasedCourseDTO purchasedCourseDTO) {
        logger.info("Received request to create purchased course: {}", purchasedCourseDTO);
        try {
            PurchasedCourseDTO createdCourse = purchasedCourseService.createPurchasedCourse(purchasedCourseDTO);
            logger.info("Purchased course created successfully with ID: {}", createdCourse.getId());
            return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            logger.error("Error creating purchased course: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during purchased course creation: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during purchased course creation: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a purchased course by its ID") // Updated @ApiOperation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchased course retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PurchasedCourseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Purchased course not found")
    })
    public ResponseEntity<PurchasedCourseDTO> getPurchasedCourseById(@PathVariable Long id) {
        logger.info("Received request to retrieve purchased course with ID: {}", id);
        try {
            PurchasedCourseDTO purchasedCourse = purchasedCourseService.getPurchasedCourseById(id);
            logger.info("Retrieved purchased course with ID: {}", id);
            return new ResponseEntity<>(purchasedCourse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Purchased course not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving purchased course with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Retrieve all purchased course records") // Updated @ApiOperation
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of purchased courses",
            content = @Content(schema = @Schema(implementation = PurchasedCourseDTO[].class))) // For array of DTOs
    public ResponseEntity<List<PurchasedCourseDTO>> getAllPurchasedCourses() {
        logger.info("Received request to retrieve all purchased courses.");
        try {
            List<PurchasedCourseDTO> purchasedCourses = purchasedCourseService.getAllPurchasedCourses();
            logger.info("Successfully retrieved {} purchased courses.", purchasedCourses.size());
            return new ResponseEntity<>(purchasedCourses, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all purchased courses: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing purchased course record") // Updated @ApiOperation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchased course updated successfully",
                    content = @Content(schema = @Schema(implementation = PurchasedCourseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Purchased course, Student, or Course not found")
    })
    public ResponseEntity<PurchasedCourseDTO> updatePurchasedCourse(@PathVariable Long id, @Valid @RequestBody PurchasedCourseDTO purchasedCourseDTO) {
        logger.info("Received request to update purchased course with ID: {}", id);
        try {
            PurchasedCourseDTO updatedCourse = purchasedCourseService.updatePurchasedCourse(id, purchasedCourseDTO);
            logger.info("Purchased course with ID {} updated successfully.", id);
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Error updating purchased course with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during purchased course update for ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during purchased course update for ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a purchased course record by its ID") // Updated @ApiOperation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Purchased course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Purchased course not found")
    })
    public ResponseEntity<Void> deletePurchasedCourse(@PathVariable Long id) {
        logger.info("Received request to delete purchased course with ID: {}", id);
        try {
            purchasedCourseService.deletePurchasedCourse(id);
            logger.info("Purchased course with ID {} deleted successfully.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            logger.error("Purchased course not found for deletion with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during purchased course deletion for ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Retrieve all purchased courses for a specific student") // Updated @ApiOperation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved purchased courses for the student (might be an empty list)",
                    content = @Content(schema = @Schema(implementation = PurchasedCourseDTO[].class))),
            @ApiResponse(responseCode = "404", description = "Student not found") // If student ID itself is invalid
    })
    public ResponseEntity<List<PurchasedCourseDTO>> getPurchasedCoursesByStudentId(@PathVariable Long studentId) {
        logger.info("Received request to retrieve purchased courses for student ID: {}", studentId);
        try {
            List<PurchasedCourseDTO> purchasedCourses = purchasedCourseService.getPurchasedCoursesByStudentId(studentId);
            logger.info("Retrieved {} purchased courses for student ID: {}", purchasedCourses.size(), studentId);
            return new ResponseEntity<>(purchasedCourses, HttpStatus.OK);
        } catch (NoSuchElementException e) { // This implies the student itself was not found by service
            logger.error("Student not found with ID: {}: {}", studentId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving purchased courses for student ID {}: {}", studentId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Retrieve all purchased courses for a specific course") // Updated @ApiOperation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved purchased courses for the course (might be an empty list)",
                    content = @Content(schema = @Schema(implementation = PurchasedCourseDTO[].class))),
            @ApiResponse(responseCode = "404", description = "Course not found") // If course ID itself is invalid
    })
    public ResponseEntity<List<PurchasedCourseDTO>> getPurchasedCoursesByCourseId(@PathVariable Long courseId) {
        logger.info("Received request to retrieve purchased courses for course ID: {}", courseId);
        try {
            List<PurchasedCourseDTO> purchasedCourses = purchasedCourseService.getPurchasedCoursesByCourseId(courseId);
            logger.info("Retrieved {} purchased courses for course ID: {}", purchasedCourses.size(), courseId);
            return new ResponseEntity<>(purchasedCourses, HttpStatus.OK);
        } catch (NoSuchElementException e) { // This implies the course itself was not found by service
            logger.error("Course not found with ID: {}: {}", courseId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving purchased courses for course ID {}: {}", courseId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}