package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.CourseModuleDTO;

import java.util.List;

public interface CourseModuleService {

    CourseModuleDTO createModule(CourseModuleDTO moduleDTO);
    CourseModuleDTO getModuleById(Long id);
    List<CourseModuleDTO> getAllModules();
    CourseModuleDTO updateModule(Long id, CourseModuleDTO moduleDTO);
    void deleteModule(Long id);
    List<CourseModuleDTO> getModulesByCourseId(Long courseId); // New method to find modules for a specific course
}