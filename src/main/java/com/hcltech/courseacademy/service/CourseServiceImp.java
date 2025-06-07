package com.hcltech.courseacademy.service;
import com.hcltech.courseacademy.dto.CourseDTO;
import com.hcltech.courseacademy.entity.Author;
import com.hcltech.courseacademy.entity.Course;
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
public class CourseServiceImp implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImp.class);
    private final CourseRepository courseRepository;
    private final AuthorRepository authorRepository;

    public CourseServiceImp(CourseRepository courseRepository, AuthorRepository authorRepository) {
        this.courseRepository = courseRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        logger.info("Attempting to create course: {}", courseDTO.getTitle());

        Author author = null;
        if (courseDTO.getAuthorId() != null) {
            author = authorRepository.findById(courseDTO.getAuthorId())
                    .orElseThrow(() -> {
                        logger.warn("Author not found with ID: {}", courseDTO.getAuthorId());
                        return new NoSuchElementException("Author not found with ID: " + courseDTO.getAuthorId());
                    });
        }

        // Convert DTO to Entity
        Course course = new Course(
                courseDTO.getTitle(),
                courseDTO.getDescription(),
                courseDTO.getPrice(),
                author
        );
        Course savedCourse = courseRepository.save(course);
        logger.info("Successfully created course with ID: {}", savedCourse.getId());

        // Convert Entity back to DTO
        return new CourseDTO(
                savedCourse.getId(),
                savedCourse.getTitle(),
                savedCourse.getDescription(),
                savedCourse.getPrice(),
                savedCourse.getAuthor() != null ? savedCourse.getAuthor().getId() : null
        );
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        logger.info("Attempting to retrieve course with ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Course not found with ID: {}", id);
                    return new NoSuchElementException("Course not found with ID: " + id);
                });
        logger.info("Successfully retrieved course with ID: {}", id);


        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPrice(),
                course.getAuthor() != null ? course.getAuthor().getId() : null
        );
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        logger.info("Attempting to retrieve all courses.");
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getPrice(),
                        course.getAuthor() != null ? course.getAuthor().getId() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        logger.info("Attempting to update course with ID: {}", id);
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Course not found for update with ID: {}", id);
                    return new NoSuchElementException("Course not found with ID: " + id);
                });


        existingCourse.setTitle(courseDTO.getTitle());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setPrice(courseDTO.getPrice());


        if (courseDTO.getAuthorId() != null &&
                (existingCourse.getAuthor() == null || !existingCourse.getAuthor().getId().equals(courseDTO.getAuthorId()))) {
            Author newAuthor = authorRepository.findById(courseDTO.getAuthorId())
                    .orElseThrow(() -> {
                        logger.warn("New author not found with ID: {}", courseDTO.getAuthorId());
                        return new NoSuchElementException("New author not found with ID: " + courseDTO.getAuthorId());
                    });
            existingCourse.setAuthor(newAuthor);
        } else if (courseDTO.getAuthorId() == null && existingCourse.getAuthor() != null) {

            existingCourse.setAuthor(null);
        }


        Course updatedCourse = courseRepository.save(existingCourse);
        logger.info("Successfully updated course with ID: {}", updatedCourse.getId());


        return new CourseDTO(
                updatedCourse.getId(),
                updatedCourse.getTitle(),
                updatedCourse.getDescription(),
                updatedCourse.getPrice(),
                updatedCourse.getAuthor() != null ? updatedCourse.getAuthor().getId() : null
        );
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        logger.info("Attempting to delete course with ID: {}", id);
        if (!courseRepository.existsById(id)) {
            logger.warn("Course not found for deletion with ID: {}", id);
            throw new NoSuchElementException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
        logger.info("Successfully deleted course with ID: {}", id);
    }
}




