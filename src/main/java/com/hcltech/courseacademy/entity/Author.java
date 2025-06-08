package com.hcltech.courseacademy.entity;

import javax.persistence.*;
import java.util.HashSet; // For courses set initialization
import java.util.Set;

@Entity
@Table(name = "authors") // Ensure your table name matches, default is 'Author'
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming auto-increment ID
    private Long id;

    @Column(name = "firstname", nullable = false) // Explicitly mark as not nullable
    private String firstname;

    @Column(name = "lastname", nullable = false) // Explicitly mark as not nullable
    private String lastname;

    @Column(name = "email", nullable = false, unique = true) // Email should be unique and not null
    private String email;


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();


    public Author() {
    }

    public Author(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }


    public Author(Long id, String firstname, String lastname, String email, Set<Course> courses) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        if (courses != null) {
            this.courses = courses;
        } else {
            this.courses = new HashSet<>();
        }
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
        return email;
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
        this.email = email;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // Helper methods for managing relationship if you chose to do so
    public void addCourse(Course course) {
        this.courses.add(course);
        course.setAuthor(this); // Set the author on the course side
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.setAuthor(null); // Remove the author reference from the course side
    }


    // Optional: Override equals() and hashCode() for proper comparison
    // Especially important if using Author objects in Sets or Maps
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id != null && id.equals(author.id); // Use ID for equality if it's unique
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0; // Use ID for hashCode
    }

    // Optional: Override toString() for better logging/debugging
    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}