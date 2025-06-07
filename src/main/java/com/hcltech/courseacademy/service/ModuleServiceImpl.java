package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.ModuleDTO;
import com.hcltech.courseacademy.entity.Course;
import com.hcltech.courseacademy.entity.Module;
import com.hcltech.courseacademy.repository.CourseRepository;
import com.hcltech.courseacademy.repository.ModuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl implements ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository; // Needed to link modules to courses


    public ModuleServiceImpl(ModuleRepository moduleRepository, CourseRepository courseRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public ModuleDTO createModule(ModuleDTO moduleDTO) {
        logger.info("Attempting to create module: {}", moduleDTO.getTitle());


        Course course = courseRepository.findById(moduleDTO.getCourseId())
                .orElseThrow(() -> {
                    logger.warn("Course not found with ID: {} for module creation.", moduleDTO.getCourseId());
                    return new NoSuchElementException("Course not found with ID: " + moduleDTO.getCourseId());
                });

        com.hcltech.courseacademy.entity.Module module = new com.hcltech.courseacademy.entity.Module( // Ensure 'Module' here refers to your custom entity
                moduleDTO.getTitle(),
                moduleDTO.getContent(),
                course
        );
        S save = moduleRepository.save(module);// And here
        logger.info("Successfully created module with ID: {}", savedModule.getId());

        return new ModuleDTO(
                savedModule.getId(),
                savedModule.getTitle(),
                savedModule.getContent(),
                savedModule.getCourse() != null ? savedModule.getCourse().getId() : null
        );
    }

    @Override
    public ModuleDTO getModuleById(Long id) {
        logger.info("Attempting to retrieve module with ID: {}", id);
        com.hcltech.courseacademy.entity.Module module = moduleRepository.findById(id) // And here
                .orElseThrow(() -> {
                    logger.warn("Module not found with ID: {}", id);
                    return new NoSuchElementException("Module not found with ID: " + id);
                });
        logger.info("Successfully retrieved module with ID: {}", id);


        return new ModuleDTO(
                module.getId(),
                module.getTitle(),
                module.getContent(),
                module.getCourse() != null ? module.getCourse().getId() : null
        );
    }

    @Override
    public List<ModuleDTO> getAllModules() {
        logger.info("Attempting to retrieve all modules.");
        List<com.hcltech.courseacademy.entity.Module> modules = moduleRepository.findAll(); // And here
        // Convert List of Entities to List of DTOs using stream
        return modules.stream()
                .map(module -> new ModuleDTO(
                        module.getId(),
                        module.getTitle(),
                        module.getContent(),
                        module.getCourse() != null ? module.getCourse().getId() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ModuleDTO updateModule(Long id, ModuleDTO moduleDTO) {
        logger.info("Attempting to update module with ID: {}", id);
        com.hcltech.courseacademy.entity.Module existingModule = moduleRepository.findById(id) // And here
                .orElseThrow(() -> {
                    logger.warn("Module not found for update with ID: {}", id);
                    return new NoSuchElementException("Module not found with ID: " + id);
                });


        existingModule.setTitle(moduleDTO.getTitle());
        existingModule.setContent(moduleDTO.getContent());

        if (moduleDTO.getCourseId() != null &&
                (existingModule.getCourse() == null || !existingModule.getCourse().getId().equals(moduleDTO.getCourseId()))) {
            Course newCourse = courseRepository.findById(moduleDTO.getCourseId())
                    .orElseThrow(() -> {
                        logger.warn("New course not found with ID: {}", moduleDTO.getCourseId());
                        return new NoSuchElementException("New course not found with ID: " + moduleDTO.getCourseId());
                    });
            existingModule.setCourse(newCourse);
        } else if (moduleDTO.getCourseId() == null && existingModule.getCourse() != null) {
            // If courseId is set to null in DTO, remove the association
            existingModule.setCourse(null);
        }

        Module updatedModule = moduleRepository.save(existingModule); // And here
        logger.info("Successfully updated module with ID: {}", updatedModule.getId());


        return new ModuleDTO(
                updatedModule.getId(),
                updatedModule.getTitle(),
                updatedModule.getContent(),
                updatedModule.getCourse() != null ? updatedModule.getCourse().getId() : null
        );
    }

    @Override
    @Transactional
    public void deleteModule(Long id) {
        logger.info("Attempting to delete module with ID: {}", id);
        if (!moduleRepository.existsById(id)) {
            logger.warn("Module not found for deletion with ID: {}", id);
            throw new NoSuchElementException("Module not found with ID: " + id);
        }
        moduleRepository.deleteById(id);
        logger.info("Successfully deleted module with ID: {}", id);
    }
}

