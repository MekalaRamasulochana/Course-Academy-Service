package com.hcltech.courseacademy.repository;

import com.hcltech.courseacademy.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,String> {

    Optional<Object> findByEmail(String email);

}
