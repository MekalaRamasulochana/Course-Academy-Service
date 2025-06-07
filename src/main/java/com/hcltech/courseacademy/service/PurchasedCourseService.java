package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.PurchasedCourseDTO;

import java.util.List;

public interface PurchasedCourseService {
    PurchasedCourseDTO createPurchasedCourse(PurchasedCourseDTO purchasedCourseDTO);
    PurchasedCourseDTO getPurchasedCourseById(Long id);
    List<PurchasedCourseDTO> getAllPurchasedCourses();
    PurchasedCourseDTO updatePurchasedCourse(Long id, PurchasedCourseDTO purchasedCourseDTO);
    void deletePurchasedCourse(Long id);

    List<PurchasedCourseDTO> getPurchasedCoursesByStudentId(Long studentId);
    List<PurchasedCourseDTO> getPurchasedCoursesByCourseId(Long courseId);
}