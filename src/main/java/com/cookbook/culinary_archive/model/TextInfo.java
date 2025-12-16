package com.cookbook.culinary_archive.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue("TEXT")
public class TextInfo extends Knowledge{
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String category;
}
