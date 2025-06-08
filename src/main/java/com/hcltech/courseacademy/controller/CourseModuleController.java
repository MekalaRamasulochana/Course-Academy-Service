
package com.hcltech.courseacademy.controller;

import com.hcltech.courseacademy.dto.CourseModuleDTO;
import com.hcltech.courseacademy.service.CourseModuleService; // Assuming ModuleService is the interface
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import javax.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/modules")
@Tag(name = "Module Management", description = "APIs for managing course modules")
public class CourseModuleController {

    private static final Logger logger = LoggerFactory.getLogger(CourseModuleController.class);
    private final CourseModuleService moduleService;

    public CourseModuleController(CourseModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping
    @Operation(summary = "Create a new module")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Module created successfully",
                    content = @Content(schema = @Schema(implementation = CourseModuleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or required fields missing"),
            @ApiResponse(responseCode = "404", description = "Course not found for the given ID")
    })
    public ResponseEntity<CourseModuleDTO> createModule(@Valid @RequestBody CourseModuleDTO moduleDTO) {
        logger.info("Received request to create module: {}", moduleDTO.getTitle());
        try {
            CourseModuleDTO createdModule = moduleService.createModule(moduleDTO);
            logger.info("Module created successfully with ID: {}", createdModule.getId());
            return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            logger.error("Error creating module: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Course not found
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during module creation: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during module creation: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a module by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CourseModuleDTO.class))),
            @ApiResponse(responseCode = "404", description = "Module not found")
    })
    public ResponseEntity<CourseModuleDTO> getModuleById(@PathVariable Long id) {
        logger.info("Received request to retrieve module with ID: {}", id);
        try {
            CourseModuleDTO module = moduleService.getModuleById(id);
            logger.info("Retrieved module with ID: {}", id);
            return new ResponseEntity<>(module, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Module not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving module with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Retrieve all modules")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of modules",
            content = @Content(schema = @Schema(implementation = CourseModuleDTO[].class)))
    public ResponseEntity<List<CourseModuleDTO>> getAllModules() {
        logger.info("Received request to retrieve all modules.");
        try {
            List<CourseModuleDTO> modules = moduleService.getAllModules();
            logger.info("Successfully retrieved {} modules.", modules.size());
            return new ResponseEntity<>(modules, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all modules: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing module record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module updated successfully",
                    content = @Content(schema = @Schema(implementation = CourseModuleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Module or Course not found")
    })
    public ResponseEntity<CourseModuleDTO> updateModule(@PathVariable Long id, @Valid @RequestBody CourseModuleDTO moduleDTO) {
        logger.info("Received request to update module with ID: {}", id);
        try {
            CourseModuleDTO updatedModule = moduleService.updateModule(id, moduleDTO);
            logger.info("Module with ID {} updated successfully.", id);
            return new ResponseEntity<>(updatedModule, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Error updating module with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during module update for ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during module update for ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a module record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Module deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Module not found")
    })
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        logger.info("Received request to delete module with ID: {}", id);
        try {
            moduleService.deleteModule(id);
            logger.info("Module with ID {} deleted successfully.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            logger.error("Module not found for deletion with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during module deletion for ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Retrieve all modules for a specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved modules for the course (might be an empty list)",
                    content = @Content(schema = @Schema(implementation = CourseModuleDTO[].class))),
            @ApiResponse(responseCode = "404", description = "Course not found") // If course ID itself is invalid
    })
    public ResponseEntity<List<CourseModuleDTO>> getModulesByCourseId(@PathVariable Long courseId) {
        logger.info("Received request to retrieve modules for course ID: {}", courseId);
        try {
            List<CourseModuleDTO> modules = moduleService.getModulesByCourseId(courseId);
            logger.info("Retrieved {} modules for course ID: {}.", modules.size(), courseId);
            return new ResponseEntity<>(modules, HttpStatus.OK);
        } catch (NoSuchElementException e) { // This implies the course itself was not found by service
            logger.error("Course not found with ID: {}: {}", courseId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving modules for course ID {}: {}", courseId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
