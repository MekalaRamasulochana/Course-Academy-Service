package com.hcltech.courseacademy.dto; // Consistent DTO package

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class CourseModuleDTO {
    // Setters
    // Getters
    private Long id;
    private String title;
    private String content;
    private Long courseId; // Foreign key to Course

    // All-argument constructor
    public CourseModuleDTO(Long id, String title, String content, Long courseId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.courseId = courseId;
    }

    // No-argument constructor
    public CourseModuleDTO() {
    }

    // Builder Pattern (optional, but good practice for DTO creation)
    public static ModuleDTOBuilder builder() {
        return new ModuleDTOBuilder();
    }

    public static class ModuleDTOBuilder {
        private Long id;
        private String title;
        private String content;
        private Long courseId;

        ModuleDTOBuilder() {
        }

        public ModuleDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ModuleDTOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ModuleDTOBuilder content(String content) {
            this.content = content;
            return this;
        }

        public ModuleDTOBuilder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public CourseModuleDTO build() {
            return new CourseModuleDTO(id, title, content, courseId);
        }

        @Override
        public String toString() {
            return "ModuleDTO.ModuleDTOBuilder(id=" + this.id + ", title=" + this.title + ", content=" + this.content + ", courseId=" + this.courseId + ")";
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseModuleDTO moduleDTO = (CourseModuleDTO) o;
        return Objects.equals(id, moduleDTO.id) && Objects.equals(title, moduleDTO.title) && Objects.equals(content, moduleDTO.content) && Objects.equals(courseId, moduleDTO.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, courseId);
    }

    // toString
    @Override
    public String toString() {
        return "ModuleDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", courseId=" + courseId +
                '}';
    }
}