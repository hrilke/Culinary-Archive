package com.cookbook.culinary_archive.dto.response;

import com.cookbook.culinary_archive.model.enums.DifficultyLevel;
import com.cookbook.culinary_archive.model.enums.KnowledgeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeResponseDTO {

    // Common fields (from Knowledge/BaseEntity)
    private UUID id;
    private String title;
    private String description;
    private Boolean published;
    private KnowledgeType knowledgeType;
    private Instant createdAt;
    private Instant updatedAt;

    // TextInfo fields
    private String content;
    private String category;

    // LinkInfo fields
    private String url;
    private String format;

    // QuoteInfo fields
    private String quoteText;
    private String speaker;
    private String source;

    // Recipe fields
    private String cuisine;
    private Integer servings;
    private Integer prepTimeMinutes;
    private DifficultyLevel difficultyLevel;
    private List<IngredientDTO> ingredients;
    private List<CookingStepDTO> cookingSteps;
}
