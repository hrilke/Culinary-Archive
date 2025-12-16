package com.cookbook.culinary_archive.service;

import com.cookbook.culinary_archive.dto.request.KnowledgeRequestDTO;
import com.cookbook.culinary_archive.dto.response.KnowledgeResponseDTO;
import com.cookbook.culinary_archive.model.enums.KnowledgeType;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface KnowledgeService {

    Page<KnowledgeResponseDTO> getAllKnowledge(KnowledgeType knowledgeType, int page, int size, String sortDirection);

    Page<KnowledgeResponseDTO> searchByTitle(String title, int page, int size, String sortDirection);

    KnowledgeResponseDTO createKnowledge(KnowledgeRequestDTO requestDTO);

    KnowledgeResponseDTO updatePublishStatus(UUID id, boolean published);

    void deleteKnowledge(UUID id);
}
