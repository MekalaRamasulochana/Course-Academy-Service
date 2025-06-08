package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.StudentDTO;
import com.hcltech.courseacademy.entity.Student;
import com.hcltech.courseacademy.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StudentServiceImp implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImp.class);
    private final StudentRepository studentRepository;

    public StudentServiceImp(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        logger.info("Attempting to create student with email: {}", studentDTO.getEmail());

        if (studentRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
            logger.warn("Student with email {} already exists.", studentDTO.getEmail());
            throw new IllegalStateException("Student with email " + studentDTO.getEmail() + " already exists.");
        }


        Student student = new Student(
                studentDTO.getEmail(),
                studentDTO.getFirstName(),
                studentDTO.getLastName()
        );
        Student savedStudent = studentRepository.save(student);
        logger.info("Successfully created student with ID: {}", savedStudent.getId());

        return new StudentDTO(
                savedStudent.getId(),
                savedStudent.getEmail(),
                savedStudent.getFirstName(),
                savedStudent.getLastName()
        );
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        logger.info("Attempting to retrieve student with ID: {}", id);
        Student student = studentRepository.findById((id))
                .orElseThrow(() -> {
                    logger.warn("Student not found with ID: {}", id);
                    return new NoSuchElementException("Student not found with ID: " + id);
                });
        logger.info("Successfully retrieved student with ID: {}", id);


        return new StudentDTO(
                student.getId(),
                student.getEmail(),
                student.getFirstName(),
                student.getLastName()
        );
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        logger.info("Attempting to retrieve all students.");
        List<Student> students = studentRepository.findAll();

        List<StudentDTO> studentDTOs = new ArrayList<>();
        if (students.isEmpty()) {
            return studentDTOs; // Return empty list immediately
        }

        for (Student student : students) {
            studentDTOs.add(new StudentDTO(
                    student.getId(),
                    student.getEmail(),
                    student.getFirstName(),
                    student.getLastName()
            ));
        }
        return studentDTOs;
    }

    @Override
    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        logger.info("Attempting to update student with ID: {}", id);
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Student not found for update with ID: {}", id);
                    return new NoSuchElementException("Student not found with ID: " + id);
                });


        if (!existingStudent.getEmail().equals(studentDTO.getEmail()) &&
                studentRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
            logger.warn("Attempt to update student ID {} with email {} which already exists for another student.", id, studentDTO.getEmail());
            throw new IllegalStateException("Email " + studentDTO.getEmail() + " is already taken by another student.");
        }


        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setFirstName(studentDTO.getFirstName());
        existingStudent.setLastName(studentDTO.getLastName());

        Student updatedStudent = studentRepository.save(existingStudent);
        logger.info("Successfully updated student with ID: {}", updatedStudent.getId());


        return new StudentDTO(
                updatedStudent.getId(),
                updatedStudent.getEmail(),
                updatedStudent.getFirstName(),
                updatedStudent.getLastName()
        );
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        logger.info("Attempting to delete student with ID: {}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Student not found for deletion with ID: {}", id);
            throw new NoSuchElementException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
        logger.info("Successfully deleted student with ID: {}", id);
    }

    @Override
    public StudentDTO getStudentByEmail(String email) {
        logger.info("Attempting to retrieve student by email: {}", email);
        Student student = (Student) studentRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Student not found with email: {}", email);
                    return new NoSuchElementException("Student not found with email: " + email);
                });
        logger.info("Successfully retrieved student with ID: {} by email.", student.getId());

        return new StudentDTO(
                student.getId(),
                student.getEmail(),
                student.getFirstName(),
                student.getLastName()
        );
    }
}