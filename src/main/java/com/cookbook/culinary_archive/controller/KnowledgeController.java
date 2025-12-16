package com.cookbook.culinary_archive.controller;

import com.cookbook.culinary_archive.config.AppProperties;
import com.cookbook.culinary_archive.dto.request.KnowledgeRequestDTO;
import com.cookbook.culinary_archive.dto.response.KnowledgeResponseDTO;
import com.cookbook.culinary_archive.model.enums.KnowledgeType;
import com.cookbook.culinary_archive.service.KnowledgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeService knowledgeService;
    private final AppProperties appProperties;

    @GetMapping
    public ResponseEntity<Page<KnowledgeResponseDTO>> getAllKnowledge(
            @RequestParam(defaultValue = "RECIPE") KnowledgeType type,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortDirection
    ) {
        AppProperties.Pagination pagination = appProperties.getPagination();
        
        int pageNum = (page != null) ? page : pagination.getDefaultPage();
        int pageSize = (size != null) ? Math.min(size, pagination.getMaxSize()) : pagination.getDefaultSize();
        String sortDir = (sortDirection != null) ? sortDirection : pagination.getDefaultSortDirection();

        Page<KnowledgeResponseDTO> result = knowledgeService.getAllKnowledge(type, pageNum, pageSize, sortDir);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<KnowledgeResponseDTO>> searchByTitle(
            @RequestParam String title,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortDirection
    ) {
        AppProperties.Pagination pagination = appProperties.getPagination();

        int pageNum = (page != null) ? page : pagination.getDefaultPage();
        int pageSize = (size != null) ? Math.min(size, pagination.getMaxSize()) : pagination.getDefaultSize();
        String sortDir = (sortDirection != null) ? sortDirection : pagination.getDefaultSortDirection();

        Page<KnowledgeResponseDTO> result = knowledgeService.searchByTitle(title, pageNum, pageSize, sortDir);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<KnowledgeResponseDTO> createKnowledge(
            @Valid @RequestBody KnowledgeRequestDTO requestDTO
    ) {
        KnowledgeResponseDTO created = knowledgeService.createKnowledge(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<KnowledgeResponseDTO> updatePublishStatus(
            @PathVariable UUID id,
            @RequestParam boolean published
    ) {
        KnowledgeResponseDTO updated = knowledgeService.updatePublishStatus(id, published);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable UUID id) {
        knowledgeService.deleteKnowledge(id);
        return ResponseEntity.noContent().build();
    }
}
