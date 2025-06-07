package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.AuthorDTO;
import com.hcltech.courseacademy.dto.CourseDTO;
import com.hcltech.courseacademy.entity.Author;
import jakarta.validation.Valid;

import java.util.List;

public interface AuthorService {
    Author createAuthor(@Valid AuthorDTO author);
    List<Author>getAllAuthors();
    Author getAuthorById(Long id);
    Author updateAuthor(Long id , @Valid AuthorDTO author);
    void deleteAuthor(Long id);

    CourseDTO assignCourseToAuthor(Long courseId, Long authorId);

    CourseDTO removeCourseFromAuthor(Long courseId);

    List<CourseDTO> getCoursesByAuthor(Long authorId);
}
