package com.cookbook.culinary_archive.mapper;

import com.cookbook.culinary_archive.dto.request.*;
import com.cookbook.culinary_archive.dto.response.CookingStepDTO;
import com.cookbook.culinary_archive.dto.response.IngredientDTO;
import com.cookbook.culinary_archive.dto.response.KnowledgeResponseDTO;
import com.cookbook.culinary_archive.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class KnowledgeMapper {

    public KnowledgeResponseDTO toDTO(Knowledge knowledge) {
        KnowledgeResponseDTO.KnowledgeResponseDTOBuilder builder = KnowledgeResponseDTO.builder()
                .id(knowledge.getId())
                .title(knowledge.getTitle())
                .description(knowledge.getDescription())
                .published(knowledge.getPublished())
                .knowledgeType(knowledge.getKnowledgeType())
                .createdAt(knowledge.getCreatedAt())
                .updatedAt(knowledge.getUpdatedAt());

        if (knowledge instanceof TextInfo textInfo) {
            builder.content(textInfo.getContent())
                   .category(textInfo.getCategory());
        } else if (knowledge instanceof LinkInfo linkInfo) {
            builder.url(linkInfo.getUrl())
                   .format(linkInfo.getFormat());
        } else if (knowledge instanceof QuoteInfo quoteInfo) {
            builder.quoteText(quoteInfo.getQuoteText())
                   .speaker(quoteInfo.getSpeaker())
                   .source(quoteInfo.getSource());
        } else if (knowledge instanceof Recipe recipe) {
            builder.cuisine(recipe.getCuisine())
                   .servings(recipe.getServings())
                   .prepTimeMinutes(recipe.getPrepTimeMinutes())
                   .difficultyLevel(recipe.getDifficultyLevel())
                   .ingredients(mapIngredients(recipe.getIngredientList()))
                   .cookingSteps(mapCookingSteps(recipe.getCookingStepList()));
        }

        return builder.build();
    }

    private List<IngredientDTO> mapIngredients(List<Ingredient> ingredients) {
        if (ingredients == null) return Collections.emptyList();
        return ingredients.stream()
                .map(this::toIngredientDTO)
                .toList();
    }

    private List<CookingStepDTO> mapCookingSteps(List<CookingStep> steps) {
        if (steps == null) return Collections.emptyList();
        return steps.stream()
                .map(this::toCookingStepDTO)
                .toList();
    }

    private IngredientDTO toIngredientDTO(Ingredient ingredient) {
        return IngredientDTO.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .quantity(ingredient.getQuantity())
                .unit(ingredient.getUnit())
                .build();
    }

    private CookingStepDTO toCookingStepDTO(CookingStep step) {
        return CookingStepDTO.builder()
                .id(step.getId())
                .stepNumber(step.getStepNumber())
                .instruction(step.getInstruction())
                .build();
    }

    // Entity Mapping (Request DTO -> Entity)

    public Knowledge toEntity(KnowledgeRequestDTO requestDTO) {
        if (requestDTO instanceof TextInfoRequestDTO textRequest) {
            return toTextInfo(textRequest);
        } else if (requestDTO instanceof LinkInfoRequestDTO linkRequest) {
            return toLinkInfo(linkRequest);
        } else if (requestDTO instanceof QuoteInfoRequestDTO quoteRequest) {
            return toQuoteInfo(quoteRequest);
        } else if (requestDTO instanceof RecipeRequestDTO recipeRequest) {
            return toRecipe(recipeRequest);
        }
        throw new IllegalArgumentException("Unknown knowledge type: " + requestDTO.getClass().getSimpleName());
    }

    private TextInfo toTextInfo(TextInfoRequestDTO dto) {
        TextInfo textInfo = new TextInfo();
        setCommonFields(textInfo, dto);
        textInfo.setContent(dto.getContent());
        textInfo.setCategory(dto.getCategory());
        return textInfo;
    }

    private LinkInfo toLinkInfo(LinkInfoRequestDTO dto) {
        LinkInfo linkInfo = new LinkInfo();
        setCommonFields(linkInfo, dto);
        linkInfo.setUrl(dto.getUrl());
        linkInfo.setFormat(dto.getFormat());
        return linkInfo;
    }

    private QuoteInfo toQuoteInfo(QuoteInfoRequestDTO dto) {
        QuoteInfo quoteInfo = new QuoteInfo();
        setCommonFields(quoteInfo, dto);
        quoteInfo.setQuoteText(dto.getQuoteText());
        quoteInfo.setSpeaker(dto.getSpeaker());
        quoteInfo.setSource(dto.getSource());
        return quoteInfo;
    }

    public Recipe toRecipe(RecipeRequestDTO dto) {
        Recipe recipe = new Recipe();
        setCommonFields(recipe, dto);
        recipe.setCuisine(dto.getCuisine());
        recipe.setServings(dto.getServings());
        recipe.setPrepTimeMinutes(dto.getPrepTimeMinutes());
        recipe.setDifficultyLevel(dto.getDifficultyLevel());
        recipe.setIngredientList(mapIngredientRequests(dto.getIngredients(), recipe));
        recipe.setCookingStepList(mapCookingStepRequests(dto.getCookingSteps(), recipe));
        return recipe;
    }

    private void setCommonFields(Knowledge entity, KnowledgeRequestDTO dto) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPublished(false);
    }

    private List<Ingredient> mapIngredientRequests(List<IngredientRequestDTO> dtos, Recipe recipe) {
        if (dtos == null) return new ArrayList<>();
        return dtos.stream()
                .map(dto -> toIngredient(dto, recipe))
                .toList();
    }

    private List<CookingStep> mapCookingStepRequests(List<CookingStepRequestDTO> dtos, Recipe recipe) {
        if (dtos == null) return new ArrayList<>();
        return dtos.stream()
                .map(dto -> toCookingStep(dto, recipe))
                .toList();
    }

    private Ingredient toIngredient(IngredientRequestDTO dto, Recipe recipe) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setQuantity(dto.getQuantity());
        ingredient.setUnit(dto.getUnit());
        ingredient.setRecipe(recipe);
        return ingredient;
    }

    private CookingStep toCookingStep(CookingStepRequestDTO dto, Recipe recipe) {
        CookingStep step = new CookingStep();
        step.setStepNumber(dto.getStepNumber());
        step.setInstruction(dto.getInstruction());
        step.setRecipe(recipe);
        return step;
    }
}
