package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.PurchasedCourseDTO;
import com.hcltech.courseacademy.entity.Course;
import com.hcltech.courseacademy.entity.PurchasedCourse;
import com.hcltech.courseacademy.entity.Student;
import com.hcltech.courseacademy.mapper.PurchasedCourseMapper; // Import the new mapper
import com.hcltech.courseacademy.repository.CourseRepository;
import com.hcltech.courseacademy.repository.PurchasedCourseRepository;
import com.hcltech.courseacademy.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors; // For stream API

@Service
public class PurchasedCourseServiceImp implements PurchasedCourseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchasedCourseServiceImp.class);
    private final PurchasedCourseRepository purchasedCourseRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public PurchasedCourseServiceImp(PurchasedCourseRepository purchasedCourseRepository,
                                     StudentRepository studentRepository,
                                     CourseRepository courseRepository) {
        this.purchasedCourseRepository = purchasedCourseRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public PurchasedCourseDTO createPurchasedCourse(PurchasedCourseDTO purchasedCourseDTO) {
        logger.info("Attempting to create purchased course for student ID: {} and course ID: {}",
                purchasedCourseDTO.getStudentId(), purchasedCourseDTO.getCourseId());

        // Rectification: studentRepository.findById expects Long
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

        // Optional: Business rule to prevent duplicate purchases
        if (purchasedCourseRepository.findByStudentIdAndCourseId(purchasedCourseDTO.getStudentId(), purchasedCourseDTO.getCourseId()).isPresent()) {
            logger.warn("Attempt to create duplicate purchased course for student ID: {} and course ID: {}",
                    purchasedCourseDTO.getStudentId(), purchasedCourseDTO.getCourseId());
            throw new IllegalArgumentException("Student has already purchased this course.");
        }

        PurchasedCourse purchasedCourse = new PurchasedCourse(
                student,
                course,
                purchasedCourseDTO.getPurchaseDate(), // Use LocalDate directly
                purchasedCourseDTO.getCompleted() != null ? purchasedCourseDTO.getCompleted() : false // Handle Boolean to boolean
        );
        PurchasedCourse savedPurchasedCourse = purchasedCourseRepository.save(purchasedCourse);
        logger.info("Successfully created purchased course with ID: {}", savedPurchasedCourse.getId());

        return PurchasedCourseMapper.toDto(savedPurchasedCourse); // Use mapper
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

        return PurchasedCourseMapper.toDto(purchasedCourse); // Use mapper
    }

    @Override
    public List<PurchasedCourseDTO> getAllPurchasedCourses() {
        logger.info("Attempting to retrieve all purchased courses.");
        List<PurchasedCourseDTO> purchasedCourseDTOs = purchasedCourseRepository.findAll().stream()
                .map(PurchasedCourseMapper::toDto) // Use mapper with stream
                .collect(Collectors.toList());
        logger.info("Successfully retrieved {} purchased courses.", purchasedCourseDTOs.size());
        return purchasedCourseDTOs;
    }

    @Override
    @Transactional
    public PurchasedCourseDTO updatePurchasedCourse(Long id, PurchasedCourseDTO purchasedCourseDTO) {
        logger.info("Attempting to update purchased course with ID: {}, details: {}", id, purchasedCourseDTO); // Log full DTO
        PurchasedCourse existingPurchasedCourse = purchasedCourseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Purchased course not found for update with ID: {}", id);
                    return new NoSuchElementException("Purchased course not found with ID: " + id);
                });

        // Update purchase date and completion status directly from DTO
        if (purchasedCourseDTO.getPurchaseDate() != null) {
            existingPurchasedCourse.setPurchaseDate(purchasedCourseDTO.getPurchaseDate());
        }
        if (purchasedCourseDTO.getCompleted() != null) {
            existingPurchasedCourse.setCompleted(purchasedCourseDTO.getCompleted());
        }


        if (purchasedCourseDTO.getStudentId() != null &&
                !purchasedCourseDTO.getStudentId().equals(existingPurchasedCourse.getStudent().getId())) {
            // Rectification: studentRepository.findById expects Long
            Student newStudent = studentRepository.findById(purchasedCourseDTO.getStudentId())
                    .orElseThrow(() -> {
                        logger.warn("New student not found with ID: {}", purchasedCourseDTO.getStudentId());
                        return new NoSuchElementException("New student not found with ID: " + purchasedCourseDTO.getStudentId());
                    });
            existingPurchasedCourse.setStudent(newStudent);
        }

        if (purchasedCourseDTO.getCourseId() != null &&
                !purchasedCourseDTO.getCourseId().equals(existingPurchasedCourse.getCourse().getId())) {
            Course newCourse = courseRepository.findById(purchasedCourseDTO.getCourseId())
                    .orElseThrow(() -> {
                        logger.warn("New course not found with ID: {}", purchasedCourseDTO.getCourseId());
                        return new NoSuchElementException("New course not found with ID: " + purchasedCourseDTO.getCourseId());
                    });
            existingPurchasedCourse.setCourse(newCourse);
        }

        PurchasedCourse updatedPurchasedCourse = purchasedCourseRepository.save(existingPurchasedCourse);
        logger.info("Successfully updated purchased course with ID: {}", updatedPurchasedCourse.getId());

        return PurchasedCourseMapper.toDto(updatedPurchasedCourse); // Use mapper
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
        List<PurchasedCourseDTO> purchasedCourseDTOs = purchasedCourseRepository.findByStudentId(studentId).stream()
                .map(PurchasedCourseMapper::toDto) // Use mapper with stream
                .collect(Collectors.toList());
        logger.info("Retrieved {} purchased courses for student ID: {}", purchasedCourseDTOs.size(), studentId);
        return purchasedCourseDTOs;
    }

    @Override
    public List<PurchasedCourseDTO> getPurchasedCoursesByCourseId(Long courseId) {
        logger.info("Attempting to retrieve purchased courses for course ID: {}", courseId);
        List<PurchasedCourseDTO> purchasedCourseDTOs = purchasedCourseRepository.findByCourseId(courseId).stream()
                .map(PurchasedCourseMapper::toDto) // Use mapper with stream
                .collect(Collectors.toList());
        logger.info("Retrieved {} purchased courses for course ID: {}", purchasedCourseDTOs.size(), courseId);
        return purchasedCourseDTOs;
    }
}