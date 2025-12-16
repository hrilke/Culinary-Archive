package com.cookbook.culinary_archive.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkInfoRequestDTO extends KnowledgeRequestDTO {

    @NotBlank(message = "URL is required")
    private String url;

    @NotBlank(message = "Format is required")
    private String format;
}
