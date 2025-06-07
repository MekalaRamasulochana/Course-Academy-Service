package com.hcltech.courseacademy.service;

import com.hcltech.courseacademy.dto.ModuleDTO;

import java.util.List;

public interface ModuleService {
    ModuleDTO createModule(ModuleDTO moduleDTO);

    ModuleDTO getModuleById(Long id);

    List<ModuleDTO> getAllModules();

    ModuleDTO updateModule(Long id, ModuleDTO moduleDTO);

    void deleteModule(Long id);
}
