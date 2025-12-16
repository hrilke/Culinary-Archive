package com.cookbook.culinary_archive.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteInfoRequestDTO extends KnowledgeRequestDTO {

    @NotBlank(message = "Quote text is required")
    private String quoteText;

    @NotBlank(message = "Speaker is required")
    private String speaker;

    private String source;
}
