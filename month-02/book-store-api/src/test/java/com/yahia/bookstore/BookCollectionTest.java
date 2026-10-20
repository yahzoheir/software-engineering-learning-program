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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class}, inheritListeners = false)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookCollectionTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void listsAnEmptyCollectionAsJson() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"10.50", "0"})
    void createsAndListsABook(String price) throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Clean Code","author":"Robert Martin","price":%s}
                                """.formatted(price)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert Martin"))
                .andExpect(jsonPath("$.price").value(Double.parseDouble(price)));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":1,"title":"Clean Code","author":"Robert Martin","price":%s}]
                        """.formatted(price)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"title\":\"   \",\"author\":\"Author\",\"price\":10}",
            "{\"title\":\"Book\",\"author\":\"   \",\"price\":10}",
            "{\"title\":\"Book\",\"author\":\"Author\",\"price\":-1}",
            "{\"author\":\"Author\",\"price\":10}",
            "{\"title\":\"Book\",\"price\":10}",
            "{\"title\":\"Book\",\"author\":\"Author\"}",
            "{"
    })
    void rejectsInvalidInputWithoutSaving(String body) throws Exception {
        // Start with an existing book so a rejected request must also preserve it.
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Existing\",\"author\":\"Author\",\"price\":1}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Existing"));
    }

    @Test
    void assignsDifferentIdsToBooksWithTheSameTitle() throws Exception {
        String body = "{\"title\":\"Same title\",\"author\":\"Author\",\"price\":5}";
        for (int id = 1; id <= 2; id++) {
            mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(id));
        }
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
    @Test
    void retrievesTheRequestedBookById() throws Exception {
        for (String title : new String[]{"First", "Second"}) {
            mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\":\"%s\",\"author\":\"Author\",\"price\":10.50}".formatted(title)))
                    .andExpect(status().isCreated());
        }
        mvc.perform(get("/books/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"id":2,"title":"Second","author":"Author","price":10.50}
                        """));
    }

    @Test
    void returnsNotFoundWithoutChangingTheCollection() throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Existing\",\"author\":\"Author\",\"price\":1}"))
                .andExpect(status().isCreated());
        mvc.perform(get("/books/99"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":1,"title":"Existing","author":"Author","price":1}]
                        """));
    }

}
