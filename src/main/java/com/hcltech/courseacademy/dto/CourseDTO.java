package com.hcltech.courseacademy.dto;

import java.util.Objects;

public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private Long authorId;


    public CourseDTO(Long id, String title, String description, Integer price, Long authorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.authorId = authorId;
    }


    public CourseDTO() {
    }


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getAuthorId() {
        return authorId;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }


    public static CourseDTOBuilder builder() {
        return new CourseDTOBuilder();
    }

    public static class CourseDTOBuilder {
        private Long id;
        private String title;
        private String description;
        private Integer price;
        private Long authorId;

        CourseDTOBuilder() {
        }

        public CourseDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CourseDTOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CourseDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CourseDTOBuilder price(Integer price) {
            this.price = price;
            return this;
        }

        public CourseDTOBuilder authorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        public CourseDTO build() {
            return new CourseDTO(id, title, description, price, authorId);
        }

        @Override
        public String toString() {
            return "CourseDTO.CourseDTOBuilder(id=" + this.id + ", title=" + this.title + ", description=" + this.description + ", price=" + this.price + ", authorId=" + this.authorId + ")";
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDTO courseDTO = (CourseDTO) o;
        return Objects.equals(id, courseDTO.id) && Objects.equals(title, courseDTO.title) && Objects.equals(description, courseDTO.description) && Objects.equals(price, courseDTO.price) && Objects.equals(authorId, courseDTO.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price, authorId);
    }


    @Override
    public String toString() {
        return "CourseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", authorId=" + authorId +
                '}';
    }
}