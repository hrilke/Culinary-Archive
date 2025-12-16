package com.cookbook.culinary_archive.repository;

import com.cookbook.culinary_archive.model.Knowledge;
import com.cookbook.culinary_archive.model.enums.KnowledgeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, UUID> {

    Page<Knowledge> findByKnowledgeTypeAndPublishedTrue(KnowledgeType knowledgeType, Pageable pageable);

    @Query("SELECT k FROM Knowledge k WHERE LOWER(k.title) LIKE LOWER(CONCAT('%', :title, '%')) AND k.published = true")
    Page<Knowledge> searchByTitleAndPublishedTrue(@Param("title") String title, Pageable pageable);
}
