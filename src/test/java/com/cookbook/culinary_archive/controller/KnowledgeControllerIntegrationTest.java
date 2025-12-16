package com.cookbook.culinary_archive.controller;

import com.cookbook.culinary_archive.model.QuoteInfo;
import com.cookbook.culinary_archive.repository.KnowledgeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class KnowledgeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Test
    @DisplayName("GET /api/knowledge returns 200 OK for public access")
    void getAllKnowledge_ReturnsOk_WhenPublic() throws Exception {
        QuoteInfo quote = new QuoteInfo();
        quote.setTitle("Test Quote");
        quote.setDescription("Test Description");
        quote.setQuoteText("Test quote text");
        quote.setSpeaker("Test Speaker");
        quote.setPublished(true);
        knowledgeRepository.save(quote);

        mockMvc.perform(get("/api/knowledge")
                        .param("type", "QUOTE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("POST /api/knowledge returns 401 when not authenticated")
    void createKnowledge_Returns401_WhenNotAuthenticated() throws Exception {
        String requestBody = """
                {
                    "type": "QUOTE",
                    "title": "New Quote",
                    "description": "A new quote",
                    "quoteText": "Testing is important",
                    "speaker": "Developer"
                }
                """;

        mockMvc.perform(post("/api/knowledge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/knowledge returns 201 when authenticated as CREATOR")
    @WithMockUser(roles = "CREATOR")
    void createKnowledge_Returns201_WhenAuthenticatedAsCreator() throws Exception {
        String requestBody = """
                {
                    "type": "QUOTE",
                    "title": "New Quote",
                    "description": "A new quote",
                    "quoteText": "Testing is important",
                    "speaker": "Developer"
                }
                """;

        mockMvc.perform(post("/api/knowledge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Quote"))
                .andExpect(jsonPath("$.quoteText").value("Testing is important"))
                .andExpect(jsonPath("$.published").value(false));
    }
}
