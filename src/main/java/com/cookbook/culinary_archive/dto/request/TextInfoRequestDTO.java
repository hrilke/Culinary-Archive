package com.cookbook.culinary_archive.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextInfoRequestDTO extends KnowledgeRequestDTO {

    @NotBlank(message = "Content is required")
    private String content;

    private String category;
}
