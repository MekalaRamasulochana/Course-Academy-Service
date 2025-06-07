package com.hcltech.courseacademy.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "purchased_courses")
public class PurchasedCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Column(nullable = false)
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    public PurchasedCourse() {
    }


    public PurchasedCourse(Student student, Course course, LocalDateTime purchaseDate, boolean completed) {
        this.student = student;
        this.course = course;
        this.purchaseDate = purchaseDate;
        this.completed = completed;
    }


    public PurchasedCourse(Long id, Student student, Course course, LocalDateTime purchaseDate, boolean completed) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.purchaseDate = purchaseDate;
        this.completed = completed;
    }


    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
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

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchasedCourse that = (PurchasedCourse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PurchasedCourse{" +
                "id=" + id +
                ", studentId=" + (student != null ? student.getId() : "null") +
                ", courseId=" + (course != null ? course.getId() : "null") +
                ", purchaseDate=" + purchaseDate +
                ", completed=" + completed +
                '}';
    }
}