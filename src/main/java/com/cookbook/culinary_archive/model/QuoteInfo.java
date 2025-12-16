package com.cookbook.culinary_archive.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("QUOTE")
public class QuoteInfo extends Knowledge{
    @Column(columnDefinition = "TEXT",nullable = false)
    private String quoteText;
    @Column(nullable = false)
    private String speaker;

    private String source;
}
