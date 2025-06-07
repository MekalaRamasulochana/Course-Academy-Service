package com.hcltech.courseacademy.controller;

import com.hcltech.courseacademy.dto.ModuleDTO;
import com.hcltech.courseacademy.service.ModuleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping
    public ResponseEntity<ModuleDTO> createModule(@Valid @RequestBody ModuleDTO moduleDTO) {
        logger.info("Received request to create module: {}", moduleDTO.getTitle());
        ModuleDTO createdModule = moduleService.createModule(moduleDTO);
        return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleDTO> getModuleById(@PathVariable Long id) {
        logger.info("Received request to get module by ID: {}", id);
        ModuleDTO module = moduleService.getModuleById(id);
        return ResponseEntity.ok(module);
    }

    @GetMapping
    public ResponseEntity<List<ModuleDTO>> getAllModules() {
        logger.info("Received request to get all modules.");
        List<ModuleDTO> modules = moduleService.getAllModules();
        return ResponseEntity.ok(modules);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleDTO> updateModule(@PathVariable Long id, @Valid @RequestBody ModuleDTO moduleDTO) {
        logger.info("Received request to update module with ID: {}, details: {}", id, moduleDTO.getTitle());
        ModuleDTO updatedModule = moduleService.updateModule(id, moduleDTO);
        return ResponseEntity.ok(updatedModule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        logger.info("Received request to delete module with ID: {}", id);
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
}