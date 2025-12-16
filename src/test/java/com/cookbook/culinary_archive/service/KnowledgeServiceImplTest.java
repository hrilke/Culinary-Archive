package com.cookbook.culinary_archive.service;

import com.cookbook.culinary_archive.dto.request.QuoteInfoRequestDTO;
import com.cookbook.culinary_archive.dto.response.KnowledgeResponseDTO;
import com.cookbook.culinary_archive.mapper.KnowledgeMapper;
import com.cookbook.culinary_archive.model.QuoteInfo;
import com.cookbook.culinary_archive.model.enums.KnowledgeType;
import com.cookbook.culinary_archive.repository.KnowledgeRepository;
import com.cookbook.culinary_archive.service.Impl.KnowledgeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KnowledgeServiceImplTest {

    @Mock
    private KnowledgeRepository knowledgeRepository;

    @Mock
    private KnowledgeMapper knowledgeMapper;

    @InjectMocks
    private KnowledgeServiceImpl knowledgeService;

    private QuoteInfo sampleQuoteInfo;
    private KnowledgeResponseDTO sampleResponseDTO;
    private QuoteInfoRequestDTO sampleRequestDTO;

    @BeforeEach
    void setUp() {
        sampleQuoteInfo = new QuoteInfo();
        sampleQuoteInfo.setId(UUID.randomUUID());
        sampleQuoteInfo.setTitle("Famous Quote");
        sampleQuoteInfo.setDescription("A famous cooking quote");
        sampleQuoteInfo.setQuoteText("With enough butter, anything is good.");
        sampleQuoteInfo.setSpeaker("Julia Child");
        sampleQuoteInfo.setPublished(true);
        sampleQuoteInfo.setCreatedAt(Instant.now());
        sampleQuoteInfo.setUpdatedAt(Instant.now());

        sampleResponseDTO = KnowledgeResponseDTO.builder()
                .id(sampleQuoteInfo.getId())
                .title(sampleQuoteInfo.getTitle())
                .description(sampleQuoteInfo.getDescription())
                .knowledgeType(KnowledgeType.QUOTE)
                .quoteText(sampleQuoteInfo.getQuoteText())
                .speaker(sampleQuoteInfo.getSpeaker())
                .published(true)
                .createdAt(sampleQuoteInfo.getCreatedAt())
                .updatedAt(sampleQuoteInfo.getUpdatedAt())
                .build();

        sampleRequestDTO = new QuoteInfoRequestDTO();
        sampleRequestDTO.setType(KnowledgeType.QUOTE);
        sampleRequestDTO.setTitle("Famous Quote");
        sampleRequestDTO.setDescription("A famous cooking quote");
        sampleRequestDTO.setQuoteText("With enough butter, anything is good.");
        sampleRequestDTO.setSpeaker("Julia Child");
    }

    @Test
    @DisplayName("getAllKnowledge returns page of published knowledge")
    void getAllKnowledge_ReturnsPageOfPublishedKnowledge() {

        Page<QuoteInfo> knowledgePage = new PageImpl<>(List.of(sampleQuoteInfo));
        when(knowledgeRepository.findByKnowledgeTypeAndPublishedTrue(eq(KnowledgeType.QUOTE), any(Pageable.class)))
                .thenReturn((Page) knowledgePage);
        when(knowledgeMapper.toDTO(sampleQuoteInfo)).thenReturn(sampleResponseDTO);

        Page<KnowledgeResponseDTO> result = knowledgeService.getAllKnowledge(KnowledgeType.QUOTE, 0, 10, "desc");

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Famous Quote");
        verify(knowledgeRepository).findByKnowledgeTypeAndPublishedTrue(eq(KnowledgeType.QUOTE), any(Pageable.class));
    }

    @Test
    @DisplayName("createKnowledge saves and returns DTO")
    void createKnowledge_SavesAndReturnsDTO() {
        when(knowledgeMapper.toEntity(sampleRequestDTO)).thenReturn(sampleQuoteInfo);
        when(knowledgeRepository.save(sampleQuoteInfo)).thenReturn(sampleQuoteInfo);
        when(knowledgeMapper.toDTO(sampleQuoteInfo)).thenReturn(sampleResponseDTO);

        KnowledgeResponseDTO result = knowledgeService.createKnowledge(sampleRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Famous Quote");
        assertThat(result.getKnowledgeType()).isEqualTo(KnowledgeType.QUOTE);
        verify(knowledgeRepository).save(sampleQuoteInfo);
        verify(knowledgeMapper).toDTO(sampleQuoteInfo);
    }

    @Test
    @DisplayName("createKnowledge throws exception when duplicate title")
    void createKnowledge_ThrowsWhenDuplicateTitle() {

        when(knowledgeMapper.toEntity(sampleRequestDTO)).thenReturn(sampleQuoteInfo);
        when(knowledgeRepository.save(sampleQuoteInfo))
                .thenThrow(new DataIntegrityViolationException("Unique constraint violation"));

        assertThatThrownBy(() -> knowledgeService.createKnowledge(sampleRequestDTO))
                .isInstanceOf(DataIntegrityViolationException.class);
        verify(knowledgeRepository).save(sampleQuoteInfo);
    }
}
