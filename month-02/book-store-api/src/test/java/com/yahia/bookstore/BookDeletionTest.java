package com.yahia.bookstore;

import org.junit.jupiter.api.Test;
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
class BookDeletionTest {
    @Autowired
    private MockMvc mvc;

    private void createBook(String title) throws Exception {
        mvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"%s\",\"author\":\"Author\",\"price\":10}".formatted(title)))
                .andExpect(status().isCreated());
    }

    @Test
    void deletesOnlyTheRequestedBookAndReturnsNotFoundOnRepeat() throws Exception {
        createBook("First");
        createBook("Second");

        mvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        mvc.perform(get("/books/1")).andExpect(status().isNotFound());
        mvc.perform(delete("/books/1")).andExpect(status().isNotFound());
        mvc.perform(get("/books/2"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"id":2,"title":"Second","author":"Author","price":10}
                        """));
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":2,"title":"Second","author":"Author","price":10}]
                        """));
    }

    @Test
    void deletingTheLastBookLeavesAnEmptyCollectionAndDoesNotReuseItsId() throws Exception {
        createBook("First");
        mvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        createBook("Second");
        mvc.perform(get("/books/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Second"));
    }

    @Test
    void deletingAnUnknownIdLeavesExistingBooksUnchanged() throws Exception {
        createBook("First");
        mvc.perform(delete("/books/99")).andExpect(status().isNotFound());
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":1,"title":"First","author":"Author","price":10}]
                        """));
    }
}
