package com.cookbook.culinary_archive.dto.request;

import com.cookbook.culinary_archive.model.enums.DifficultyLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RecipeRequestDTO extends KnowledgeRequestDTO {

    @NotBlank(message = "Cuisine is required")
    private String cuisine;

    @NotNull(message = "Servings is required")
    @Positive(message = "Servings must be positive")
    private Integer servings;

    @NotNull(message = "Prep time is required")
    @Positive(message = "Prep time must be positive")
    private Integer prepTimeMinutes;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficultyLevel;

    private List<UUID> referencedKnowledgeIds = new ArrayList<>();

    @Valid
    @NotEmpty(message = "At least one ingredient is required")
    private List<IngredientRequestDTO> ingredients;

    @Valid
    @NotEmpty(message = "At least one cooking step is required")
    private List<CookingStepRequestDTO> cookingSteps;
}
