package com.hcltech.courseacademy.dto;

import java.util.Objects;

public class StudentDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;


    public StudentDTO(Long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public StudentDTO() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static StudentDTOBuilder builder() {
        return new StudentDTOBuilder();
    }

    public static class StudentDTOBuilder {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;

        StudentDTOBuilder() {
        }

        public StudentDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StudentDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public StudentDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentDTO build() {
            return new StudentDTO(id, email, firstName, lastName);
        }

        @Override
        public String toString() {
            return "StudentDTO.StudentDTOBuilder(id=" + this.id + ", email=" + this.email + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ")";
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName);
    }


    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}