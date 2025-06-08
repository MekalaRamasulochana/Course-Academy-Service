// Assuming this is your DTO. If not, please provide its current content.
package com.hcltech.courseacademy.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate; // Use LocalDate for consistency

public class PurchasedCourseDTO {
    private Long id;
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;
    @NotNull(message = "Purchase date cannot be null")
    private LocalDate purchaseDate; // Changed to LocalDate
    private Boolean completed; // Use Boolean wrapper for DTO, allows null if not always present

    public PurchasedCourseDTO() {}

    public PurchasedCourseDTO(Long id, Long studentId, Long courseId, LocalDate purchaseDate, Boolean completed) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.purchaseDate = purchaseDate;
        this.completed = completed;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public LocalDate getPurchaseDate() { // Changed to LocalDate
        return purchaseDate;
    }

    public Boolean getCompleted() { // Changed to Boolean
        return completed;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setPurchaseDate(LocalDate purchaseDate) { // Changed to LocalDate
        this.purchaseDate = purchaseDate;
    }

    public void setCompleted(Boolean completed) { // Changed to Boolean
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "PurchasedCourseDTO{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", purchaseDate=" + purchaseDate +
                ", completed=" + completed +
                '}';
    }
}