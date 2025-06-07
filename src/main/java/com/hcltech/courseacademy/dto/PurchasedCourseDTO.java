package com.hcltech.courseacademy.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class PurchasedCourseDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDateTime purchaseDate;
    private boolean completed;


    public PurchasedCourseDTO(Long id, Long studentId, Long courseId, LocalDateTime purchaseDate, boolean completed) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.purchaseDate = purchaseDate;
        this.completed = completed;
    }


    public PurchasedCourseDTO() {
    }


    public Long getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public boolean isCompleted() {
        return completed;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    public static PurchasedCourseDTOBuilder builder() {
        return new PurchasedCourseDTOBuilder();
    }

    public static class PurchasedCourseDTOBuilder {
        private Long id;
        private Long studentId;
        private Long courseId;
        private LocalDateTime purchaseDate;
        private boolean completed;

        PurchasedCourseDTOBuilder() {
        }

        public PurchasedCourseDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PurchasedCourseDTOBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public PurchasedCourseDTOBuilder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public PurchasedCourseDTOBuilder purchaseDate(LocalDateTime purchaseDate) {
            this.purchaseDate = purchaseDate;
            return this;
        }

        public PurchasedCourseDTOBuilder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        public PurchasedCourseDTO build() {
            return new PurchasedCourseDTO(id, studentId, courseId, purchaseDate, completed);
        }

        @Override
        public String toString() {
            return "PurchasedCourseDTO.PurchasedCourseDTOBuilder(id=" + this.id + ", studentId=" + this.studentId + ", courseId=" + this.courseId + ", purchaseDate=" + this.purchaseDate + ", completed=" + this.completed + ")";
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchasedCourseDTO that = (PurchasedCourseDTO) o;
        return completed == that.completed && Objects.equals(id, that.id) && Objects.equals(studentId, that.studentId) && Objects.equals(courseId, that.courseId) && Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, courseId, purchaseDate, completed);
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