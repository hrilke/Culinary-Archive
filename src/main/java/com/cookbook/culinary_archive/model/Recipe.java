package com.cookbook.culinary_archive.model;

import com.cookbook.culinary_archive.model.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("RECIPE")
public class Recipe extends Knowledge{

    @Column(nullable = false)
    private String cuisine;

    @Column(nullable = false)
    private Integer servings;

    @Column(nullable = false)
    private Integer prepTimeMinutes;

    @Column(nullable = false)
    private DifficultyLevel difficultyLevel;

    @ManyToMany
    @JoinTable(name = "recipe_references")
    private List<Knowledge> referencedKnowledge;

    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Ingredient> ingredientList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CookingStep> cookingStepList = new ArrayList<>();
}
