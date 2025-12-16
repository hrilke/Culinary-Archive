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
@DiscriminatorValue("LINK")
public class LinkInfo extends Knowledge{
    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String format;
}
