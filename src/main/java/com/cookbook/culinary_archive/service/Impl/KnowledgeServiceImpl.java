package com.cookbook.culinary_archive.service.Impl;

import com.cookbook.culinary_archive.dto.request.KnowledgeRequestDTO;
import com.cookbook.culinary_archive.dto.request.RecipeRequestDTO;
import com.cookbook.culinary_archive.dto.response.KnowledgeResponseDTO;
import com.cookbook.culinary_archive.mapper.KnowledgeMapper;
import com.cookbook.culinary_archive.model.Knowledge;
import com.cookbook.culinary_archive.model.Recipe;
import com.cookbook.culinary_archive.model.enums.KnowledgeType;
import com.cookbook.culinary_archive.repository.KnowledgeRepository;
import com.cookbook.culinary_archive.service.KnowledgeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final KnowledgeMapper knowledgeMapper;

    @Override
    public Page<KnowledgeResponseDTO> getAllKnowledge(KnowledgeType knowledgeType, int page, int size, String sortDirection) {
        Sort sort = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.by(Sort.Direction.DESC, "createdAt")
                : Sort.by(Sort.Direction.ASC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Knowledge> knowledgePage = knowledgeRepository.findByKnowledgeTypeAndPublishedTrue(knowledgeType, pageable);

        return knowledgePage.map(knowledgeMapper::toDTO);
    }

    @Override
    public Page<KnowledgeResponseDTO> searchByTitle(String title, int page, int size, String sortDirection) {
        log.info("Searching knowledge by title: {}", title);
        Sort sort = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.by(Sort.Direction.DESC, "createdAt")
                : Sort.by(Sort.Direction.ASC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Knowledge> knowledgePage = knowledgeRepository.searchByTitleAndPublishedTrue(title, pageable);

        return knowledgePage.map(knowledgeMapper::toDTO);
    }

    @Override
    @Transactional
    public KnowledgeResponseDTO createKnowledge(KnowledgeRequestDTO requestDTO) {
        log.info("Creating new knowledge of type: {}", requestDTO.getType());
        Knowledge knowledge = knowledgeMapper.toEntity(requestDTO);

        // Handle referenced knowledge for Recipe
        if (requestDTO instanceof RecipeRequestDTO recipeRequest && knowledge instanceof Recipe recipe) {
            List<UUID> referencedIds = recipeRequest.getReferencedKnowledgeIds();
            if (referencedIds != null && !referencedIds.isEmpty()) {
                List<Knowledge> referencedKnowledge = knowledgeRepository.findAllById(referencedIds);
                recipe.setReferencedKnowledge(referencedKnowledge);
            }
        }

        Knowledge savedKnowledge = knowledgeRepository.save(knowledge);
        log.debug("Knowledge created with id: {}", savedKnowledge.getId());
        return knowledgeMapper.toDTO(savedKnowledge);
    }

    @Override
    @Transactional
    public KnowledgeResponseDTO updatePublishStatus(UUID id, boolean published) {
        log.info("Updating publish status for knowledge id: {} to {}", id, published);
        Knowledge knowledge = knowledgeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Knowledge not found with id: " + id));

        if (!knowledge.getPublished().equals(published)) {
            knowledge.setPublished(published);
            knowledge = knowledgeRepository.save(knowledge);
            log.debug("Publish status updated for knowledge id: {}", id);
        }

        return knowledgeMapper.toDTO(knowledge);
    }

    @Override
    @Transactional
    public void deleteKnowledge(UUID id) {
        log.info("Deleting knowledge with id: {}", id);
        if (!knowledgeRepository.existsById(id)) {
            throw new EntityNotFoundException("Knowledge not found with id: " + id);
        }
        knowledgeRepository.deleteById(id);
        log.debug("Knowledge deleted with id: {}", id);
    }
}
