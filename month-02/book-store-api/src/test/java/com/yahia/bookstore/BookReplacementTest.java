package com.yahia.bookstore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class}, inheritListeners = false)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookReplacementTest {
    private static final String ORIGINAL = """
            {"title":"Original","author":"Author","price":10}
            """;

    @Autowired
    private MockMvc mvc;

    private void createOriginal() throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(ORIGINAL))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(strings = {"20.50", "0"})
    void replacesAllFieldsAndRepeatsWithoutCreatingBooks(String price) throws Exception {
        createOriginal();
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Other\",\"author\":\"Other author\",\"price\":5}"))
                .andExpect(status().isCreated());
        String replacement = """
                {"title":"Replacement","author":"New author","price":%s}
                """.formatted(price);
        String expected = """
                {"id":1,"title":"Replacement","author":"New author","price":%s}
                """.formatted(price);
        for (int attempt = 0; attempt < 2; attempt++) {
            mvc.perform(put("/books/1").contentType(MediaType.APPLICATION_JSON).content(replacement))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(expected));
            mvc.perform(get("/books/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expected));
            mvc.perform(get("/books"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            [%s,{"id":2,"title":"Other","author":"Other author","price":5}]
                            """.formatted(expected)));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"price\":20}",
            "{\"title\":\"New\",\"price\":20}",
            "{\"title\":\"New\",\"author\":\"New author\"}",
            "{\"title\":\"   \",\"author\":\"New author\",\"price\":20}",
            "{\"title\":\"New\",\"author\":\"   \",\"price\":20}",
            "{\"title\":\"New\",\"author\":\"New author\",\"price\":-1}",
            "{"
    })
    void rejectsInvalidReplacementWithoutChangingBook(String body) throws Exception {
        createOriginal();
        mvc.perform(put("/books/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":1,"title":"Original","author":"Author","price":10}]
                        """));
    }

    @Test
    void missingIdReturnsNotFoundWithoutCreatingOrChangingBooks() throws Exception {
        createOriginal();
        mvc.perform(put("/books/99").contentType(MediaType.APPLICATION_JSON).content(ORIGINAL))
                .andExpect(status().isNotFound());
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":1,"title":"Original","author":"Author","price":10}]
                        """));
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(ORIGINAL))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }
}
