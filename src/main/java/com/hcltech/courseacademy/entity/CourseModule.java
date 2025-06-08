package com.hcltech.courseacademy.entity; // Corrected package name

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "modules")
public class CourseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Ensure Course entity is in the correct package too

    public CourseModule() {
    }

    public CourseModule(String title, String content, Course course) {
        this.title = title;
        this.content = content;
        this.course = course;
    }

    public CourseModule(Long id, String title, String content, Course course) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Course getCourse() {
        return course;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseModule module = (CourseModule) o;
        return Objects.equals(id, module.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", courseId=" + (course != null ? course.getId() : "null") +
                '}';
    }
}