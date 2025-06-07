package com.hcltech.courseacademy.dto;

import java.util.Objects;

// This DTO should be in com.hcltech.CourseAcademyService.DTO package
public class AuthorDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email; // Using lowercase 'email' here for DTO, common practice

    // AllArgsConstructor
    public AuthorDTO(Long id, String firstname, String lastname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    // NoArgsConstructor
    public AuthorDTO() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Builder Pattern (Optional, but good practice for DTOs)
    public static AuthorDTOBuilder builder() {
        return new AuthorDTOBuilder();
    }

    public static class AuthorDTOBuilder {
        private Long id;
        private String firstname;
        private String lastname;
        private String email;

        AuthorDTOBuilder() {}

        public AuthorDTOBuilder id(Long id) { this.id = id; return this; }
        public AuthorDTOBuilder firstname(String firstname) { this.firstname = firstname; return this; }
        public AuthorDTOBuilder lastname(String lastname) { this.lastname = lastname; return this; }
        public AuthorDTOBuilder email(String email) { this.email = email; return this; }

        public AuthorDTO build() {
            return new AuthorDTO(id, firstname, lastname, email);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDTO authorDTO = (AuthorDTO) o;
        return Objects.equals(id, authorDTO.id) && Objects.equals(firstname, authorDTO.firstname) && Objects.equals(lastname, authorDTO.lastname) && Objects.equals(email, authorDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email);
    }

    // toString
    @Override
    public String toString() {
        return "AuthorDTO{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}