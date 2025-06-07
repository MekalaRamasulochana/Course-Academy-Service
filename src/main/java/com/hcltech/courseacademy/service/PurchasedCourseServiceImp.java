package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.PurchasedCourseDTO;
import com.hcltech.courseacademy.entity.Course;
import com.hcltech.courseacademy.entity.PurchasedCourse;
import com.hcltech.courseacademy.entity.Student;
import com.hcltech.courseacademy.repository.CourseRepository;
import com.hcltech.courseacademy.repository.PurchasedCourseRepository;
import com.hcltech.courseacademy.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PurchasedCourseServiceImp implements PurchasedCourseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchasedCourseServiceImp.class);
    private final PurchasedCourseRepository purchasedCourseRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public PurchasedCourseServiceImp(PurchasedCourseRepository purchasedCourseRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.purchasedCourseRepository = purchasedCourseRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public PurchasedCourseDTO createPurchasedCourse(PurchasedCourseDTO purchasedCourseDTO) {
        logger.info("Attempting to create purchased course for student ID: {} and course ID: {}",
                purchasedCourseDTO.getStudentId(), purchasedCourseDTO.getCourseId());

        Student student = studentRepository.findById(purchasedCourseDTO.getStudentId())
                .orElseThrow(() -> {
                    logger.warn("Student not found with ID: {} for purchased course creation.", purchasedCourseDTO.getStudentId());
                    return new NoSuchElementException("Student not found with ID: " + purchasedCourseDTO.getStudentId());
                });


        Course course = courseRepository.findById(purchasedCourseDTO.getCourseId())
                .orElseThrow(() -> {
                    logger.warn("Course not found with ID: {} for purchased course creation.", purchasedCourseDTO.getCourseId());
                    return new NoSuchElementException("Course not found with ID: " + purchasedCourseDTO.getCourseId());
                });


        PurchasedCourse purchasedCourse = new PurchasedCourse(
                student,
                course,
                purchasedCourseDTO.getPurchaseDate(),
                purchasedCourseDTO.isCompleted()
        );
        PurchasedCourse savedPurchasedCourse = purchasedCourseRepository.save(purchasedCourse);
        logger.info("Successfully created purchased course with ID: {}", savedPurchasedCourse.getId());


        return new PurchasedCourseDTO(
                savedPurchasedCourse.getId(),
                savedPurchasedCourse.getStudent().getId(),
                savedPurchasedCourse.getCourse().getId(),
                savedPurchasedCourse.getPurchaseDate(),
                savedPurchasedCourse.isCompleted()
        );
    }

    @Override
    public PurchasedCourseDTO getPurchasedCourseById(Long id) {
        logger.info("Attempting to retrieve purchased course with ID: {}", id);
        PurchasedCourse purchasedCourse = purchasedCourseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Purchased course not found with ID: {}", id);
                    return new NoSuchElementException("Purchased course not found with ID: " + id);
                });
        logger.info("Successfully retrieved purchased course with ID: {}", id);

        return new PurchasedCourseDTO(
                purchasedCourse.getId(),
                purchasedCourse.getStudent().getId(),
                purchasedCourse.getCourse().getId(),
                purchasedCourse.getPurchaseDate(),
                purchasedCourse.isCompleted()
        );
    }

    @Override
    public List<PurchasedCourseDTO> getAllPurchasedCourses() {
        logger.info("Attempting to retrieve all purchased courses.");
        List<PurchasedCourse> purchasedCourses = purchasedCourseRepository.findAll();


        List<PurchasedCourseDTO> purchasedCourseDTOs = new ArrayList<>();
        if (purchasedCourses.isEmpty()) {
            return purchasedCourseDTOs; // Return empty list immediately
        }

        for (PurchasedCourse pc : purchasedCourses) {
            PurchasedCourseDTO dto = new PurchasedCourseDTO(
                    pc.getId(),
                    pc.getStudent() != null ? pc.getStudent().getId() : null,
                    pc.getCourse() != null ? pc.getCourse().getId() : null,
                    pc.getPurchaseDate(),
                    pc.isCompleted()
            );
            purchasedCourseDTOs.add(dto);
        }
        return purchasedCourseDTOs;
    }

    @Override
    @Transactional
    public PurchasedCourseDTO updatePurchasedCourse(Long id, PurchasedCourseDTO purchasedCourseDTO) {
        logger.info("Attempting to update purchased course with ID: {}", id);
        PurchasedCourse existingPurchasedCourse = purchasedCourseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Purchased course not found for update with ID: {}", id);
                    return new NoSuchElementException("Purchased course not found with ID: " + id);
                });


        existingPurchasedCourse.setPurchaseDate(purchasedCourseDTO.getPurchaseDate());
        existingPurchasedCourse.setCompleted(purchasedCourseDTO.isCompleted());

        if (purchasedCourseDTO.getStudentId() != null &&
                (existingPurchasedCourse.getStudent() == null || !existingPurchasedCourse.getStudent().getId().equals(purchasedCourseDTO.getStudentId()))) {
            Student newStudent = studentRepository.findById(purchasedCourseDTO.getStudentId())
                    .orElseThrow(() -> {
                        logger.warn("New student not found with ID: {}", purchasedCourseDTO.getStudentId());
                        return new NoSuchElementException("New student not found with ID: " + purchasedCourseDTO.getStudentId());
                    });
            existingPurchasedCourse.setStudent(newStudent);
        } else if (purchasedCourseDTO.getStudentId() == null && existingPurchasedCourse.getStudent() != null) {
            existingPurchasedCourse.setStudent(null);
        }


        if (purchasedCourseDTO.getCourseId() != null &&
                (existingPurchasedCourse.getCourse() == null || !existingPurchasedCourse.getCourse().getId().equals(purchasedCourseDTO.getCourseId()))) {
            Course newCourse = courseRepository.findById(purchasedCourseDTO.getCourseId())
                    .orElseThrow(() -> {
                        logger.warn("New course not found with ID: {}", purchasedCourseDTO.getCourseId());
                        return new NoSuchElementException("New course not found with ID: " + purchasedCourseDTO.getCourseId());
                    });
            existingPurchasedCourse.setCourse(newCourse);
        } else if (purchasedCourseDTO.getCourseId() == null && existingPurchasedCourse.getCourse() != null) {
            existingPurchasedCourse.setCourse(null);
        }


        PurchasedCourse updatedPurchasedCourse = purchasedCourseRepository.save(existingPurchasedCourse);
        logger.info("Successfully updated purchased course with ID: {}", updatedPurchasedCourse.getId());

        return new PurchasedCourseDTO(
                updatedPurchasedCourse.getId(),
                updatedPurchasedCourse.getStudent() != null ? updatedPurchasedCourse.getStudent().getId() : null,
                updatedPurchasedCourse.getCourse() != null ? updatedPurchasedCourse.getCourse().getId() : null,
                updatedPurchasedCourse.getPurchaseDate(),
                updatedPurchasedCourse.isCompleted()
        );
    }

    @Override
    @Transactional
    public void deletePurchasedCourse(Long id) {
        logger.info("Attempting to delete purchased course with ID: {}", id);
        if (!purchasedCourseRepository.existsById(id)) {
            logger.warn("Purchased course not found for deletion with ID: {}", id);
            throw new NoSuchElementException("Purchased course not found with ID: " + id);
        }
        purchasedCourseRepository.deleteById(id);
        logger.info("Successfully deleted purchased course with ID: {}", id);
    }

    @Override
    public List<PurchasedCourseDTO> getPurchasedCoursesByStudentId(Long studentId) {
        logger.info("Attempting to retrieve purchased courses for student ID: {}", studentId);
        List<PurchasedCourse> purchasedCourses = purchasedCourseRepository.findByStudentId(studentId);

        List<PurchasedCourseDTO> purchasedCourseDTOs = new ArrayList<>();
        for (PurchasedCourse pc : purchasedCourses) {
            purchasedCourseDTOs.add(new PurchasedCourseDTO(
                    pc.getId(),
                    pc.getStudent() != null ? pc.getStudent().getId() : null,
                    pc.getCourse() != null ? pc.getCourse().getId() : null,
                    pc.getPurchaseDate(),
                    pc.isCompleted()
            ));
        }
        return purchasedCourseDTOs;
    }

    @Override
    public List<PurchasedCourseDTO> getPurchasedCoursesByCourseId(Long courseId) {
        logger.info("Attempting to retrieve purchased courses for course ID: {}", courseId);
        List<PurchasedCourse> purchasedCourses = purchasedCourseRepository.findByCourseId(courseId);

        List<PurchasedCourseDTO> purchasedCourseDTOs = new ArrayList<>();
        for (PurchasedCourse pc : purchasedCourses) {
            purchasedCourseDTOs.add(new PurchasedCourseDTO(
                    pc.getId(),
                    pc.getStudent() != null ? pc.getStudent().getId() : null,
                    pc.getCourse() != null ? pc.getCourse().getId() : null,
                    pc.getPurchaseDate(),
                    pc.isCompleted()
            ));
        }
        return purchasedCourseDTOs;
    }
}
