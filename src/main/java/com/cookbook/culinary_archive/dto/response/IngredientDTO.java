package com.cookbook.culinary_archive.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class IngredientDTO {
    private UUID id;
    private String name;
    private Double quantity;
    private String unit;
}
