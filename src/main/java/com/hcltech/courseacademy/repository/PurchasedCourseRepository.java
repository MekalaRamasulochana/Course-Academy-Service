package com.hcltech.courseacademy.repository;

import com.hcltech.courseacademy.entity.PurchasedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchasedCourseRepository extends JpaRepository<PurchasedCourse, Long> {

    List<PurchasedCourse> findByStudentId(Long studentId);

    List<PurchasedCourse> findByCourseId(Long courseId);

}
