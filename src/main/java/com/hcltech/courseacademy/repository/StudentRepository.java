package com.hcltech.courseacademy.repository;

import com.hcltech.courseacademy.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Object> findByEmail(String email);

}
