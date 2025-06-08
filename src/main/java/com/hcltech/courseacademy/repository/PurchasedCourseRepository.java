package com.hcltech.courseacademy.repository;

import com.hcltech.courseacademy.entity.PurchasedCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Import Optional

@Repository
public interface PurchasedCourseRepository extends JpaRepository<PurchasedCourse, Long> {

    // Existing methods (if you have them)
    List<PurchasedCourse> findByStudentId(Long studentId);
    List<PurchasedCourse> findByCourseId(Long courseId);

    // New method to check for duplicate purchases
    Optional<PurchasedCourse> findByStudentIdAndCourseId(Long studentId, Long courseId);
}