package com.cookbook.culinary_archive.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CookingStepRequestDTO {

    @NotNull(message = "Step number is required")
    @Positive(message = "Step number must be positive")
    private Integer stepNumber;

    @NotBlank(message = "Instruction is required")
    private String instruction;
}
