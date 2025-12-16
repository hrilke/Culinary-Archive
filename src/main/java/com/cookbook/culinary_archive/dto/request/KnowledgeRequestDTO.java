package com.cookbook.culinary_archive.dto.request;

import com.cookbook.culinary_archive.model.enums.KnowledgeType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextInfoRequestDTO.class, name = "TEXT"),
        @JsonSubTypes.Type(value = LinkInfoRequestDTO.class, name = "LINK"),
        @JsonSubTypes.Type(value = QuoteInfoRequestDTO.class, name = "QUOTE"),
        @JsonSubTypes.Type(value = RecipeRequestDTO.class, name = "RECIPE")
})
public abstract class KnowledgeRequestDTO {

    private KnowledgeType type;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;
}
