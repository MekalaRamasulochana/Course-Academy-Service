package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.AuthorDTO;
import com.hcltech.courseacademy.dto.CourseDTO;
import com.hcltech.courseacademy.entity.Author;
import com.hcltech.courseacademy.entity.Course; // Ensure Course entity is imported
import com.hcltech.courseacademy.repository.AuthorRepository;
import com.hcltech.courseacademy.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);
    private final AuthorRepository authorRepository;
    private final CourseRepository courseRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, CourseRepository courseRepository) {
        this.authorRepository = authorRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public Author createAuthor(AuthorDTO authorDTO) {
        logger.info("Attempting to create author: {} {}", authorDTO.getFirstname(), authorDTO.getLastname());
        // Convert DTO to Entity using new fields
        Author author = new Author(authorDTO.getFirstname(), authorDTO.getLastname(), authorDTO.getEmail());
        Author savedAuthor = authorRepository.save(author);
        logger.info("Successfully created author with ID: {}", savedAuthor.getId());
        return savedAuthor;
    }

    @Override
    public List<Author> getAllAuthors() {
        logger.info("Attempting to retrieve all authors.");
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        logger.info("Attempting to retrieve author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Author not found with ID: {}", id);
                    return new NoSuchElementException("Author not found with ID: " + id);
                });
        logger.info("Successfully retrieved author with ID: {}", id);
        return author;
    }

    @Override
    @Transactional
    public Author updateAuthor(Long id, AuthorDTO authorDTO) {
        logger.info("Attempting to update author with ID: {}", id);
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Author not found for update with ID: {}", id);
                    return new NoSuchElementException("Author not found with ID: " + id);
                });

        // Update fields from DTO
        existingAuthor.setFirstname(authorDTO.getFirstname());
        existingAuthor.setLastname(authorDTO.getLastname());
        existingAuthor.setEmail(authorDTO.getEmail());

        Author updatedAuthor = authorRepository.save(existingAuthor);
        logger.info("Successfully updated author with ID: {}", updatedAuthor.getId());
        return updatedAuthor;
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        logger.info("Attempting to delete author with ID: {}", id);
        if (!authorRepository.existsById(id)) {
            logger.warn("Author not found for deletion with ID: {}", id);
            throw new NoSuchElementException("Author not found with ID: " + id);
        }
        authorRepository.deleteById(id);
        logger.info("Successfully deleted author with ID: {}", id);
    }

    @Override
    @Transactional
    public CourseDTO assignCourseToAuthor(Long courseId, Long authorId) {
        logger.info("Attempting to assign course ID {} to author ID {}.", courseId, authorId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    logger.warn("Course not found with ID: {}", courseId);
                    return new NoSuchElementException("Course not found with ID: " + courseId);
                });
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    logger.warn("Author not found with ID: {}", authorId);
                    return new NoSuchElementException("Author not found with ID: " + authorId);
                });

        course.setAuthor(author);
        Course updatedCourse = courseRepository.save(course);
        logger.info("Successfully assigned course ID {} to author ID {}.", courseId, authorId);
        return new CourseDTO(updatedCourse.getId(), updatedCourse.getTitle(), updatedCourse.getDescription(), updatedCourse.getAuthor().getId());
    }

    @Override
    @Transactional
    public CourseDTO removeCourseFromAuthor(Long courseId) {
        logger.info("Attempting to remove author from course ID {}.", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    logger.warn("Course not found for removal from author with ID: {}", courseId);
                    return new NoSuchElementException("Course not found with ID: " + courseId);
                });

        if (course.getAuthor() == null) {
            logger.warn("Course ID {} does not have an assigned author to remove.", courseId);
            throw new IllegalStateException("Course " + courseId + " does not have an author assigned.");
        }

        course.setAuthor(null);
        Course updatedCourse = courseRepository.save(course);
        logger.info("Successfully removed author from course ID {}.", courseId);
        return new CourseDTO(updatedCourse.getId(), updatedCourse.getTitle(), updatedCourse.getDescription(), null);
    }

    @Override
    public List<CourseDTO> getCoursesByAuthor(Long authorId) {
        logger.info("Attempting to retrieve courses for author ID {}.", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    logger.warn("Author not found when fetching courses for ID: {}", authorId);
                    return new NoSuchElementException("Author not found with ID: " + authorId);
                });

        // Ensure Course entity has getAuthor() and CourseDTO can map from it
        // This assumes 'courses' collection is fetched (can be lazy, then accessed in a transactional context)
        List<Course> courses = author.getCourses().stream().collect(Collectors.toList()); // Convert Set to List for stream()

        if (courses == null) {
            return List.of();
        }

        return courses.stream()
                .map(course -> new CourseDTO(course.getId(), course.getTitle(), course.getDescription(), course.getAuthor() != null ? course.getAuthor().getId() : null))
                .collect(Collectors.toList());
    }
}