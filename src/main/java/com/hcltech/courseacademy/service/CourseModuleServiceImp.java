package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.CourseModuleDTO; // Updated DTO import
import com.hcltech.courseacademy.entity.Course;
import com.hcltech.courseacademy.entity.CourseModule; // Updated Entity import
import com.hcltech.courseacademy.repository.CourseRepository;
import com.hcltech.courseacademy.repository.CourseModuleRepository; // Updated Repository import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CourseModuleServiceImp implements CourseModuleService { // Updated class name and interface

    private static final Logger logger = LoggerFactory.getLogger(CourseModuleServiceImp.class); // Updated logger name
    private final CourseModuleRepository courseModuleRepository; // Updated repository field name
    private final CourseRepository courseRepository;

    public CourseModuleServiceImp(CourseModuleRepository courseModuleRepository, CourseRepository courseRepository) { // Updated constructor parameter
        this.courseModuleRepository = courseModuleRepository; // Updated field assignment
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public CourseModuleDTO createModule(CourseModuleDTO courseModuleDTO) { // Updated DTO type
        logger.info("Attempting to create module with title: {} for course ID: {}",
                courseModuleDTO.getTitle(), courseModuleDTO.getCourseId());

        Course course = courseRepository.findById(courseModuleDTO.getCourseId())
                .orElseThrow(() -> {
                    logger.warn("Course not found with ID: {} for module creation.", String.valueOf(courseModuleDTO.getCourseId()));
                    return new NoSuchElementException("Course not found with ID: " + courseModuleDTO.getCourseId());
                });

        CourseModule courseModule = new CourseModule( // Updated entity type
                courseModuleDTO.getTitle(),
                courseModuleDTO.getContent(),
                course
        );

        CourseModule savedModule = courseModuleRepository.save(courseModule); // Updated repository call and entity type
        logger.info("Successfully created module with ID: {}", savedModule.getId());

        return new CourseModuleDTO( // Updated DTO type
                savedModule.getId(),
                savedModule.getTitle(),
                savedModule.getContent(),
                savedModule.getCourse().getId()
        );
    }

    @Override
    public CourseModuleDTO getModuleById(Long id) { // Updated DTO type
        logger.info("Attempting to retrieve module with ID: {}", id);
        CourseModule courseModule = courseModuleRepository.findById(id) // Updated repository call and entity type
                .orElseThrow(() -> {
                    logger.warn("Module not found with ID: {}", id);
                    return new NoSuchElementException("Module not found with ID: " + id);
                });
        logger.info("Successfully retrieved module with ID: {}", id);

        return new CourseModuleDTO( // Updated DTO type
                courseModule.getId(),
                courseModule.getTitle(),
                courseModule.getContent(),
                courseModule.getCourse().getId()
        );
    }

    @Override
    public List<CourseModuleDTO> getAllModules() { // Updated DTO type
        logger.info("Attempting to retrieve all modules.");
        List<CourseModule> courseModules = courseModuleRepository.findAll(); // Updated entity type

        List<CourseModuleDTO> courseModuleDTOs = new ArrayList<>(); // Updated DTO type
        for (CourseModule courseModule : courseModules) { // Updated entity type
            courseModuleDTOs.add(new CourseModuleDTO( // Updated DTO type
                    courseModule.getId(),
                    courseModule.getTitle(),
                    courseModule.getContent(),
                    courseModule.getCourse().getId()
            ));
        }
        logger.info("Retrieved {} modules.", courseModules.size());
        return courseModuleDTOs;
    }

    @Override
    @Transactional
    public CourseModuleDTO updateModule(Long id, CourseModuleDTO courseModuleDTO) { // Updated DTO type
        logger.info("Attempting to update module with ID: {}", id);
        CourseModule existingModule = courseModuleRepository.findById(id) // Updated repository call and entity type
                .orElseThrow(() -> {
                    logger.warn("Module not found for update with ID: {}", id);
                    return new NoSuchElementException("Module not found with ID: " + id);
                });

        existingModule.setTitle(courseModuleDTO.getTitle());
        existingModule.setContent(courseModuleDTO.getContent());

        if (courseModuleDTO.getCourseId() != null &&
                !courseModuleDTO.getCourseId().equals(existingModule.getCourse().getId())) {
            Course newCourse = courseRepository.findById(courseModuleDTO.getCourseId())
                    .orElseThrow(() -> {
                        logger.warn("New course not found with ID: {}", String.valueOf(courseModuleDTO.getCourseId()));
                        return new NoSuchElementException("New course not found with ID: " + courseModuleDTO.getCourseId());
                    });
            existingModule.setCourse(newCourse);
        }

        CourseModule updatedModule = courseModuleRepository.save(existingModule); // Updated repository call and entity type
        logger.info("Successfully updated module with ID: {}", updatedModule.getId());

        return new CourseModuleDTO( // Updated DTO type
                updatedModule.getId(),
                updatedModule.getTitle(),
                updatedModule.getContent(),
                updatedModule.getCourse().getId()
        );
    }

    @Override
    @Transactional
    public void deleteModule(Long id) {
        logger.info("Attempting to delete module with ID: {}", id);
        if (!courseModuleRepository.existsById(id)) { // Updated repository call
            logger.warn("Module not found for deletion with ID: {}", id);
            throw new NoSuchElementException("Module not found with ID: " + id);
        }
        courseModuleRepository.deleteById(id); // Updated repository call
        logger.info("Successfully deleted module with ID: {}", id);
    }

    @Override
    public List<CourseModuleDTO> getModulesByCourseId(Long courseId) { // Updated DTO type
        logger.info("Attempting to retrieve modules for course ID: {}", courseId);
        List<CourseModule> courseModules = courseModuleRepository.findByCourseId(courseId); // Updated repository call and entity type

        List<CourseModuleDTO> courseModuleDTOs = new ArrayList<>(); // Updated DTO type
        for (CourseModule courseModule : courseModules) { // Updated entity type
            courseModuleDTOs.add(new CourseModuleDTO( // Updated DTO type
                    courseModule.getId(),
                    courseModule.getTitle(),
                    courseModule.getContent(),
                    courseModule.getCourse().getId()
            ));
        }
        logger.info("Retrieved {} modules for course ID: {}.", courseModules.size(), courseId);
        return courseModuleDTOs;
    }
}