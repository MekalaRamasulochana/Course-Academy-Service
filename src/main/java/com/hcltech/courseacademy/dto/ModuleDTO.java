package com.hcltech.courseacademy.dto;

import java.util.Objects;

public class ModuleDTO {
    private Long id;
    private String title;
    private String content;
    private Long courseId; // Foreign key to Course

    public ModuleDTO(Long id, String title, String content, Long courseId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.courseId = courseId;
    }


    public ModuleDTO() {
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

    public Long getCourseId() {
        return courseId;
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

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }


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

        public ModuleDTO build() {
            return new ModuleDTO(id, title, content, courseId);
        }

        @Override
        public String toString() {
            return "ModuleDTO.ModuleDTOBuilder(id=" + this.id + ", title=" + this.title + ", content=" + this.content + ", courseId=" + this.courseId + ")";
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleDTO moduleDTO = (ModuleDTO) o;
        return Objects.equals(id, moduleDTO.id) && Objects.equals(title, moduleDTO.title) && Objects.equals(content, moduleDTO.content) && Objects.equals(courseId, moduleDTO.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, courseId);
    }


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