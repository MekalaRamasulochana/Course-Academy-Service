package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.AuthorDTO;
import com.hcltech.courseacademy.dto.CourseDTO;
import com.hcltech.courseacademy.entity.Author;
import com.hcltech.courseacademy.entity.Course;
import com.hcltech.courseacademy.repository.AuthorRepository;
import com.hcltech.courseacademy.repository.CourseRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional // Ensures atomicity of operations
public class AuthorServiceImp implements AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImp.class);

    private final AuthorRepository authorRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AuthorServiceImp(AuthorRepository authorRepository, CourseRepository courseRepository) {
        this.authorRepository = authorRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        logger.info("Service: Creating author with email: {}", authorDTO.getEmail());
        // --- FIX IS HERE: Correct DTO to Entity conversion ---
        Author author = new Author(); // Create an empty Author entity
        author.setFirstname(authorDTO.getFirstname()); // Set fields from DTO
        author.setLastname(authorDTO.getLastname());
        author.setEmail(authorDTO.getEmail());

        Author savedAuthor = authorRepository.save(author);
        logger.info("Service: Author saved to database with ID: {}", savedAuthor.getId());
        return convertToAuthorDTO(savedAuthor);
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        logger.info("Service: Attempting to retrieve all authors.");
        List<Author> authors = authorRepository.findAll();
        logger.info("Service: Retrieved {} authors.", authors.size());
        return authors.stream()
                .map(this::convertToAuthorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO getAuthorById(Long id) {
        logger.info("Service: Attempting to retrieve author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with ID: " + id));
        logger.info("Service: Author found with ID: {}", id);
        return convertToAuthorDTO(author);
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        logger.info("Service: Attempting to update author with ID: {}", id);
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with ID: " + id));

        // Update fields from DTO to existing entity
        existingAuthor.setFirstname(authorDTO.getFirstname());
        existingAuthor.setLastname(authorDTO.getLastname());
        existingAuthor.setEmail(authorDTO.getEmail()); // Ensure email is also updated

        Author updatedAuthor = authorRepository.save(existingAuthor);
        logger.info("Service: Author with ID {} updated successfully.", id);
        return convertToAuthorDTO(updatedAuthor);
    }

    @Override
    public void deleteAuthor(Long id) {
        logger.info("Service: Attempting to delete author with ID: {}", id);
        if (!authorRepository.existsById(id)) {
            throw new NoSuchElementException("Author not found with ID: " + id);
        }
        authorRepository.deleteById(id);
        logger.info("Service: Author with ID {} deleted from database.", id);
    }

    @Override
    public CourseDTO assignCourseToAuthor(Long courseId, Long authorId) {
        logger.info("Service: Attempting to assign course ID {} to author ID {}.", courseId, authorId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found with ID: " + courseId));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author not found with ID: " + authorId));

        // Check if the course is already assigned to this author
        if (course.getAuthor() != null && course.getAuthor().getId().equals(authorId)) {
            logger.warn("Course ID {} is already assigned to author ID {}.", courseId, authorId);
            return convertToCourseDTO(course); // Or throw IllegalArgumentException if re-assignment is not allowed
        }

        course.setAuthor(author);
        Course updatedCourse = courseRepository.save(course);
        logger.info("Service: Course ID {} assigned to author ID {} successfully.", courseId, authorId);
        return convertToCourseDTO(updatedCourse);
    }

    @Override
    public CourseDTO removeCourseFromAuthor(Long courseId) {
        logger.info("Service: Attempting to remove author from course ID {}.", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found with ID: " + courseId));
        if (course.getAuthor() == null) {
            logger.warn("Course ID {} is not assigned to any author. No action needed.", courseId);
            return convertToCourseDTO(course);
        }

        course.setAuthor(null); // Set the author to null
        Course updatedCourse = courseRepository.save(course);
        logger.info("Service: Author removed from course ID {} successfully.", courseId);
        return convertToCourseDTO(updatedCourse);
    }

    @Override
    public List<CourseDTO> getCoursesByAuthor(Long authorId) {
        logger.info("Service: Attempting to retrieve courses for author ID {}.", authorId);
        // First, check if the author exists
        if (!authorRepository.existsById(authorId)) {
            throw new NoSuchElementException("Author not found with ID: " + authorId);
        }

        // Assuming you have a findByAuthorId method in CourseRepository
        List<Course> courses = courseRepository.findByAuthorId(authorId);
        logger.info("Service: Retrieved {} courses for author ID {}.", courses.size(), authorId);
        return courses.stream()
                .map(this::convertToCourseDTO)
                .collect(Collectors.toList());
    }

    // --- Helper methods for DTO-Entity conversion ---
    private AuthorDTO convertToAuthorDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstname(author.getFirstname());
        authorDTO.setLastname(author.getLastname());
        authorDTO.setEmail(author.getEmail());
        return authorDTO;
    }

    private CourseDTO convertToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setPrice(course.getPrice());
        // Set authorId if author is present
        if (course.getAuthor() != null) {
            courseDTO.setAuthorId(course.getAuthor().getId());
        }
        return courseDTO;
    }
}