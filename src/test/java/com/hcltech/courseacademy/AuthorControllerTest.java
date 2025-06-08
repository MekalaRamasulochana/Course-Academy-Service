package com.hcltech.courseacademy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.courseacademy.controller.AuthorController;
import com.hcltech.courseacademy.dto.AuthorDTO;
import com.hcltech.courseacademy.dto.CourseDTO;
import com.hcltech.courseacademy.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class) // Focuses on testing AuthorController, mocks MVC infra
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc; // Used to perform HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to/from JSON

    @MockBean // Creates a Mockito mock for AuthorService and adds it to the Spring context
    private AuthorService authorService;

    private AuthorDTO testAuthorDTO;

    @BeforeEach
    void setUp() {

        testAuthorDTO = new AuthorDTO(1L, "Mekala Rama", "sudha@gmail.com");
        CourseDTO testCourseDTO = new CourseDTO(101L, "Introduction to Java", "Learn Java basics", null, null);

    }

    // --- POST /api/authors (createAuthor) ---

    @Test
    @DisplayName("should create a new author successfully")
    void createAuthor_Success() throws Exception {
        when(authorService.createAuthor(any(AuthorDTO.class))).thenReturn(testAuthorDTO);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                // MODIFIED: Updated expected name
                .andExpect(jsonPath("$.name").value("Mekala Rama"))
                // MODIFIED: Updated expected email
                .andExpect(jsonPath("$.email").value("sudha@gmail.com"));

        verify(authorService, times(1)).createAuthor(any(AuthorDTO.class));
    }

    @Test
    @DisplayName("should return BAD_REQUEST for invalid author input (missing name)")
    void createAuthor_InvalidInput_MissingName() throws Exception {
        AuthorDTO invalidAuthor = new AuthorDTO(null, "", "invalid@example.com"); // Blank name
        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAuthor)))
                .andExpect(status().isBadRequest()); // @Valid should catch this

        verify(authorService, never()).createAuthor(any(AuthorDTO.class)); // Service should not be called
    }

    @Test
    @DisplayName("should return BAD_REQUEST for invalid email format")
    void createAuthor_InvalidInput_InvalidEmailFormat() throws Exception {
        AuthorDTO invalidAuthor = new AuthorDTO(null, "Mekalaaa Rama", "invalid-email"); // Invalid email
        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAuthor)))
                .andExpect(status().isBadRequest());

        verify(authorService, never()).createAuthor(any(AuthorDTO.class));
    }

    @Test
    @DisplayName("should return BAD_REQUEST when email already exists (DataIntegrityViolation)")
    void createAuthor_EmailAlreadyExists_DataIntegrityViolation() throws Exception {
        when(authorService.createAuthor(any(AuthorDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Email already exists"));

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthorDTO)))
                .andExpect(status().isBadRequest());

        verify(authorService, times(1)).createAuthor(any(AuthorDTO.class));
    }

    @Test
    @DisplayName("should return BAD_REQUEST for business logic validation error")
    void createAuthor_ServiceThrowsIllegalArgumentException() throws Exception {
        when(authorService.createAuthor(any(AuthorDTO.class)))
                .thenThrow(new IllegalArgumentException("Business rule violation: Author cannot be named 'Admin'"));

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthorDTO)))
                .andExpect(status().isBadRequest());

        verify(authorService, times(1)).createAuthor(any(AuthorDTO.class));
    }

    @Test
    @DisplayName("should return INTERNAL_SERVER_ERROR for unexpected service exception during creation")
    void createAuthor_InternalServerError() throws Exception {
        when(authorService.createAuthor(any(AuthorDTO.class)))
                .thenThrow(new RuntimeException("Something unexpected happened"));

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthorDTO)))
                .andExpect(status().isInternalServerError());

        verify(authorService, times(1)).createAuthor(any(AuthorDTO.class));
    }

    // --- GET /api/authors/{id} (getAuthorById) ---

    @Test
    @DisplayName("should retrieve author by ID successfully")
    void getAuthorById_Success() throws Exception {
        when(authorService.getAuthorById(1L)).thenReturn(testAuthorDTO);

        mockMvc.perform(get("/api/authors/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                // MODIFIED: Updated expected name
                .andExpect(jsonPath("$.name").value("Mekala Rama"));

        verify(authorService, times(1)).getAuthorById(1L);
    }

    @Test
    @DisplayName("should return NOT_FOUND when author by ID does not exist")
    void getAuthorById_NotFound() throws Exception {
        when(authorService.getAuthorById(99L)).thenThrow(new NoSuchElementException("Author not found"));

        mockMvc.perform(get("/api/authors/{id}", 99L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).getAuthorById(99L);
    }

    @Test
    @DisplayName("should return INTERNAL_SERVER_ERROR for unexpected service exception during retrieval by ID")
    void getAuthorById_InternalServerError() throws Exception {
        when(authorService.getAuthorById(1L)).thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get("/api/authors/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(authorService, times(1)).getAuthorById(1L);
    }

    // --- PUT /api/authors/{id} (updateAuthor) ---

    @Test
    @DisplayName("should update an existing author successfully")
    void updateAuthor_Success() throws Exception {
        // MODIFIED: Updated expected name and email for the updated DTO
        AuthorDTO updatedAuthorDTO = new AuthorDTO(1L, "Mekala sudha Updated", "sudha.updated@example.com");
        when(authorService.updateAuthor(eq(1L), any(AuthorDTO.class))).thenReturn(updatedAuthorDTO);

        mockMvc.perform(put("/api/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                // MODIFIED: Updated expected name
                .andExpect(jsonPath("$.name").value("Mekala sudha Updated"))
                // MODIFIED: Updated expected email
                .andExpect(jsonPath("$.email").value("sudha.updated@example.com"));

        verify(authorService, times(1)).updateAuthor(eq(1L), any(AuthorDTO.class));
    }

    @Test
    @DisplayName("should return NOT_FOUND when author to update does not exist")
    void updateAuthor_NotFound() throws Exception {
        AuthorDTO nonExistentAuthor = new AuthorDTO(99L, "Non Existent", "no@example.com");
        when(authorService.updateAuthor(eq(99L), any(AuthorDTO.class)))
                .thenThrow(new NoSuchElementException("Author not found for update"));

        mockMvc.perform(put("/api/authors/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentAuthor)))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).updateAuthor(eq(99L), any(AuthorDTO.class));
    }

    @Test
    @DisplayName("should return BAD_REQUEST for invalid update input (invalid email)")
    void updateAuthor_InvalidInput_InvalidEmail() throws Exception {
        AuthorDTO invalidUpdate = new AuthorDTO(1L, "Mekala Rama", "bad-email");
        mockMvc.perform(put("/api/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest());

        verify(authorService, never()).updateAuthor(eq(1L), any(AuthorDTO.class));
    }

    @Test
    @DisplayName("should return BAD_REQUEST for DataIntegrityViolation during update")
    void updateAuthor_DataIntegrityViolation() throws Exception {
        AuthorDTO conflictAuthor = new AuthorDTO(1L, "Mekala Rama", "existing@example.com");
        when(authorService.updateAuthor(eq(1L), any(AuthorDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Email 'existing@example.com' is already in use by another author."));

        mockMvc.perform(put("/api/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflictAuthor)))
                .andExpect(status().isBadRequest());

        verify(authorService, times(1)).updateAuthor(eq(1L), any(AuthorDTO.class));
    }

    @Test
    @DisplayName("should return INTERNAL_SERVER_ERROR for unexpected service exception during update")
    void updateAuthor_InternalServerError() throws Exception {
        AuthorDTO validAuthor = new AuthorDTO(1L, "Mekala Rama", "sudha@gmail.com");
        when(authorService.updateAuthor(eq(1L), any(AuthorDTO.class)))
                .thenThrow(new RuntimeException("Database error during update"));

        mockMvc.perform(put("/api/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAuthor)))
                .andExpect(status().isInternalServerError());

        verify(authorService, times(1)).updateAuthor(eq(1L), any(AuthorDTO.class));
    }

    // --- POST /api/authors/{authorId}/courses/{courseId} (assignCourseToAuthor) ---

    @Test
    @DisplayName("should assign a course to an author successfully")
    void assignCourseToAuthor_Success() throws Exception {
        CourseDTO assignedCourse = new CourseDTO(101L, "Introduction to Java", "Learn Java basics", 1L, "Mekala Rama");
        when(authorService.assignCourseToAuthor(eq(101L), eq(1L))).thenReturn(assignedCourse);

        mockMvc.perform(post("/api/authors/{authorId}/courses/{courseId}", 1L, 101L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.title").value("Introduction to Java"))
                .andExpect(jsonPath("$.authorId").value(1L));

        verify(authorService, times(1)).assignCourseToAuthor(eq(101L), eq(1L));
    }

    @Test
    @DisplayName("should return NOT_FOUND when author not found for assignment")
    void assignCourseToAuthor_AuthorNotFound() throws Exception {
        when(authorService.assignCourseToAuthor(eq(101L), eq(999L)))
                .thenThrow(new NoSuchElementException("Author with ID 999 not found"));

        mockMvc.perform(post("/api/authors/{authorId}/courses/{courseId}", 999L, 101L))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).assignCourseToAuthor(eq(101L), eq(999L));
    }

    @Test
    @DisplayName("should return NOT_FOUND when course not found for assignment")
    void assignCourseToAuthor_CourseNotFound() throws Exception {
        when(authorService.assignCourseToAuthor(eq(999L), eq(1L)))
                .thenThrow(new NoSuchElementException("Course with ID 999 not found"));

        mockMvc.perform(post("/api/authors/{authorId}/courses/{courseId}", 1L, 999L))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).assignCourseToAuthor(eq(999L), eq(1L));
    }

    @Test
    @DisplayName("should return BAD_REQUEST when course is already assigned (IllegalArgumentException)")
    void assignCourseToAuthor_CourseAlreadyAssigned() throws Exception {
        when(authorService.assignCourseToAuthor(eq(101L), eq(1L)))
                .thenThrow(new IllegalArgumentException("Course 101 is already assigned to an author."));

        mockMvc.perform(post("/api/authors/{authorId}/courses/{courseId}", 1L, 101L))
                .andExpect(status().isBadRequest());

        verify(authorService, times(1)).assignCourseToAuthor(eq(101L), eq(1L));
    }

    @Test
    @DisplayName("should return INTERNAL_SERVER_ERROR for unexpected service exception during course assignment")
    void assignCourseToAuthor_InternalServerError() throws Exception {
        when(authorService.assignCourseToAuthor(eq(101L), eq(1L)))
                .thenThrow(new RuntimeException("Unexpected error during assignment"));

        mockMvc.perform(post("/api/authors/{authorId}/courses/{courseId}", 1L, 101L))
                .andExpect(status().isInternalServerError());

        verify(authorService, times(1)).assignCourseToAuthor(eq(101L), eq(1L));
    }

    // --- GET /api/authors (getAllAuthors) ---

    @Test
    @DisplayName("should retrieve all authors successfully")
    void getAllAuthors_Success() throws Exception {
        List<AuthorDTO> authors = List.of(
                testAuthorDTO, // This will now be Mekala Rama
                new AuthorDTO(2L, "Jane Smith", "jane.smith@example.com")
        );
        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/api/authors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                // MODIFIED: Updated expected name
                .andExpect(jsonPath("$[0].name").value("Mekala Rama"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    @DisplayName("should return empty list when no authors exist")
    void getAllAuthors_NoAuthors() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/authors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    @DisplayName("should return INTERNAL_SERVER_ERROR for unexpected service exception during getAllAuthors")
    void getAllAuthors_InternalServerError() throws Exception {
        when(authorService.getAllAuthors()).thenThrow(new RuntimeException("DB down"));

        mockMvc.perform(get("/api/authors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(authorService, times(1)).getAllAuthors();
    }

    // --- DELETE /api/authors/{id} (deleteAuthor) ---

    @Test
    @DisplayName("should delete an author successfully")
    void deleteAuthor_Success() throws Exception {
        doNothing().when(authorService).deleteAuthor(1L);

        mockMvc.perform(delete("/api/authors/{id}", 1L))
                .andExpect(status().isNoContent()); // 204 No Content

        verify(authorService, times(1)).deleteAuthor(1L);
    }

    @Test
    @DisplayName("should return NOT_FOUND when author to delete does not exist")
    void deleteAuthor_NotFound() throws Exception {
        doThrow(new NoSuchElementException("Author not found for deletion")).when(authorService).deleteAuthor(99L);

        mockMvc.perform(delete("/api/authors/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).deleteAuthor(99L);
    }

    @Test
    @DisplayName("should return INTERNAL_SERVER_ERROR for unexpected service exception during deletion")
    void deleteAuthor_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Failed to delete from DB")).when(authorService).deleteAuthor(1L);

        mockMvc.perform(delete("/api/authors/{id}", 1L))
                .andExpect(status().isInternalServerError());

        verify(authorService, times(1)).deleteAuthor(1L);
    }

    // --- DELETE /api/authors/courses/{courseId}/remove-author (removeCourseFromAuthor) ---

    @Test
    @DisplayName("should remove author from course successfully")
    void removeCourseFromAuthor_Success() throws Exception {
        CourseDTO updatedCourse = new CourseDTO(101L, "Introduction to Java", "Learn Java basics", null, null);
        when(authorService.removeCourseFromAuthor(101L)).thenReturn(updatedCourse);

        mockMvc.perform(delete("/api/authors/courses/{courseId}/remove-author", 101L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.authorId").doesNotExist()); // No authorId expected

        verify(authorService, times(1)).removeCourseFromAuthor(101L);
    }

    @Test
    @DisplayName("should return NOT_FOUND when course to remove author from does not exist")
    void removeCourseFromAuthor_CourseNotFound() throws Exception {
        when(authorService.removeCourseFromAuthor(999L))
                .thenThrow(new NoSuchElementException("Course with ID 999 not found for author removal"));

        mockMvc.perform(delete("/api/authors/courses/{courseId}/remove-author", 999L))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).removeCourseFromAuthor(999L);
    }

    @Test
    @DisplayName("should return INTERNAL_SERVER_ERROR for unexpected service exception during removeCourseFromAuthor")
    void removeCourseFromAuthor_InternalServerError() throws Exception {
        when(authorService.removeCourseFromAuthor(101L))
                .thenThrow(new RuntimeException("DB error during course author removal"));

        mockMvc.perform(delete("/api/authors/courses/{courseId}/remove-author", 101L))
                .andExpect(status().isInternalServerError());

        verify(authorService, times(1)).removeCourseFromAuthor(101L);
    }

    // --- GET /api/authors/{authorId}/courses (getCoursesByAuthor) ---

    @Test
    @DisplayName("should retrieve courses by author ID successfully")
    void getCoursesByAuthor_Success() throws Exception {
        List<CourseDTO> courses = List.of(
                new CourseDTO(101L, "Java Basics", "Desc 1", 1L, "Mekala Rama"), // Changed author name here
                new CourseDTO(102L, "Spring Framework", "Desc 2", 1L, "Mekala Rama") // Changed author name here
        );
        when(authorService.getCoursesByAuthor(1L)).thenReturn(courses);

        mockMvc.perform(get("/api/authors/{authorId}/courses", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Java Basics"))
                .andExpect(jsonPath("$[1].title").value("Spring Framework"));

        verify(authorService, times(1)).getCoursesByAuthor(1L);
    }

    @Test
    @DisplayName("should return empty list when author has no courses")
    void getCoursesByAuthor_NoCourses() throws Exception {
        when(authorService.getCoursesByAuthor(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/authors/{authorId}/courses", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(authorService, times(1)).getCoursesByAuthor(1L);
    }

    @Test
    @DisplayName("should return NOT_FOUND when author does not exist for getting courses")
    void getCoursesByAuthor_AuthorNotFound() throws Exception {
        when(authorService.getCoursesByAuthor(999L))
                .thenThrow(new NoSuchElementException("Author with ID 999 not found for courses"));

        mockMvc.perform(get("/api/authors/{authorId}/courses", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).getCoursesByAuthor(999L);
    }

    @Test
    @DisplayName("should return INTERNAL_SERVER_ERROR for unexpected service exception during getCoursesByAuthor")
    void getCoursesByAuthor_InternalServerError() throws Exception {
        when(authorService.getCoursesByAuthor(1L))
                .thenThrow(new RuntimeException("Failed to fetch courses from DB"));

        mockMvc.perform(get("/api/authors/{authorId}/courses", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(authorService, times(1)).getCoursesByAuthor(1L);
    }
}