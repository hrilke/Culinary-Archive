package com.cookbook.culinary_archive.model;

import com.cookbook.culinary_archive.model.enums.KnowledgeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "knowledge_type")
public abstract class Knowledge extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    private Boolean published = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "knowledge_type", insertable = false, updatable = false)
    private KnowledgeType knowledgeType;
}
