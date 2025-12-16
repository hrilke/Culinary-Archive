package com.cookbook.culinary_archive.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CookingStepDTO {
    private UUID id;
    private Integer stepNumber;
    private String instruction;
}
