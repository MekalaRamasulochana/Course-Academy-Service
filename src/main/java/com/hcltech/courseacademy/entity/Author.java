package com.hcltech.courseacademy.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String Email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    public Author() {
        // No-argument constructor is required by JPA
    }

    // Corrected constructor parameters and assignments
    public Author(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.Email = email; // Correctly assigning the 'email' parameter to 'Email' field
    }

    // --- Getters ---
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
        return Email;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    // --- Setters ---
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
        Email = email;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }


    // Rely on the ID for identity.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id); // Compare only by ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash based only on ID
    }


    //  lazy initialization exceptions.
    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", Email='" + Email + '\'' +
                '}';
    }
}

