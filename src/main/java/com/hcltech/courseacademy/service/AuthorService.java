package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.AuthorDTO;
import com.hcltech.courseacademy.dto.CourseDTO;

import java.util.List;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    List<AuthorDTO> getAllAuthors();
    AuthorDTO getAuthorById(Long id);
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);
    void deleteAuthor(Long id);
    CourseDTO assignCourseToAuthor(Long courseId, Long authorId);
    CourseDTO removeCourseFromAuthor(Long courseId);
    List<CourseDTO> getCoursesByAuthor(Long authorId);
}