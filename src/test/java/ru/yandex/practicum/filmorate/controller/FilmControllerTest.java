package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest()
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() throws Exception {
        mockMvc.perform(delete("/films"));
    }

    @SneakyThrows
    @Test
    void validateFilmOk() {
        String validFilm = "{\"name\":\"Film name\",\"description\":\"Film description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":100}";

        mockMvc.perform(post("/films")
                .contentType("application/json").content(validFilm)).andDo(
                        h -> {
                            assertEquals(200, h.getResponse().getStatus());
                            String expectedResponse = "{\"name\":\"Film name\",\"description\":\"Film description\"," +
                                    "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[]," +
                                    "\"countLikes\":0}";
                            assertEquals(expectedResponse, h.getResponse().getContentAsString());
                        }
        );
        mockMvc.perform(get("/films/1")
                .contentType("application/json")).andDo(
                        h -> {
                            assertEquals(200, h.getResponse().getStatus());
                            String expectedResponse = "{\"name\":\"Film name\",\"description\":\"Film description\"," +
                                    "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[]," +
                                    "\"countLikes\":0}";
                            assertEquals(expectedResponse, h.getResponse().getContentAsString());
                        }
        );
        validFilm = "{\"name\":\"Updated film name\",\"description\":\"Updated film description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":200,\"id\":1,\"likes\":[]}";
        mockMvc.perform(put("/films")
                .contentType("application/json").content(validFilm)).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("{\"name\":\"Updated film name\"," +
                                    "\"description\":\"Updated film description\"," +
                                    "\"releaseDate\":\"1895-12-28\",\"duration\":200,\"id\":1,\"likes\":[]," +
                            "\"countLikes\":0}",
                            h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/films")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[{\"name\":\"Updated film name\"," +
                                    "\"description\":\"Updated film description\"," +
                            "\"releaseDate\":\"1895-12-28\",\"duration\":200,\"id\":1,\"likes\":[]," +
                                    "\"countLikes\":0}]",
                            h.getResponse().getContentAsString());
                }
        );
        validFilm = "{\"name\":\"Updated film name\",\"description\":\"Updated film description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":200,\"id\":1,\"likes\":[]}";
        mockMvc.perform(delete("/films")
                .contentType("application/json").content(validFilm)).andDo(
                h -> assertEquals(200, h.getResponse().getStatus())
        );
        mockMvc.perform(get("/films")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[]", h.getResponse().getContentAsString());
                }
        );
    }

    @SneakyThrows
    @Test
    void validateNotFound() {
        String validFilm = "{\"name\":\"Film name\",\"description\":\"Film description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":100}";

        mockMvc.perform(post("/films")
                .contentType("application/json").content(validFilm)).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"name\":\"Film name\",\"description\":\"Film description\"," +
                            "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[]," +
                            "\"countLikes\":0}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/films")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "[{\"name\":\"Film name\",\"description\":\"Film description\"," +
                            "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[]," +
                            "\"countLikes\":0}]";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        String invalidFilm = "{\"name\":\"Updated film name\",\"description\":\"Updated film description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":200,\"id\":0,\"likes\":[]}";
        mockMvc.perform(put("/films")
                .contentType("application/json").content(invalidFilm)).andDo(
                h -> {
                    System.out.println(h.getResponse());
                    assertEquals(404, h.getResponse().getStatus());
                    assertEquals("{\"error\":\"Film not found.\"}",
                            h.getResponse().getContentAsString());
                }
        );
        validFilm = "{\"name\":\"Film name\",\"description\":\"Film description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[]}";
        mockMvc.perform(delete("/films")
                .contentType("application/json").content(validFilm)).andDo(
                h -> assertEquals(200, h.getResponse().getStatus())
        );
    }

    @SneakyThrows
    @Test
    void validateInvalidFilmName() {
        String invalidFilm = "{\"name\":\"\",\"description\":\"Film description\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":100}";

        mockMvc.perform(post("/films")
                .contentType("application/json").content(invalidFilm)).andDo(
                h -> {
                    assertEquals(400, h.getResponse().getStatus());
                    assertEquals("{\"error\":\"Film name invalid.\"}",
                            h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/films")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[]", h.getResponse().getContentAsString());
                }
        );
    }

    @SneakyThrows
    @Test
    void validateInvalidFilmDescription() {
        String invalidFilm = "{\"name\":\"Film name\",\"description\":\"" +
                "f".repeat(201) + "\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":100}";

        mockMvc.perform(post("/films")
                .contentType("application/json").content(invalidFilm)).andDo(
                h -> {
                    assertEquals(400, h.getResponse().getStatus());
                    assertEquals("{\"error\":\"Film description invalid.\"}",
                            h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/films")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[]", h.getResponse().getContentAsString());
                }
        );
    }

    @SneakyThrows
    @Test
    void validateInvalidFilmReleaseDate() {
        String invalidFilm = "{\"name\":\"Film name\",\"description\":\"Film description\"," +
                "\"releaseDate\":\"1895-12-27\",\"duration\":100}";

        mockMvc.perform(post("/films")
                .contentType("application/json").content(invalidFilm)).andDo(
                h -> {
                    assertEquals(400, h.getResponse().getStatus());
                    assertEquals("{\"error\":\"Film release date invalid.\"}",
                            h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/films")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[]", h.getResponse().getContentAsString());
                }
        );
    }

    @SneakyThrows
    @Test
    void validateInvalidFilmDuration() {
        String invalidFilm = "{\"name\":\"Film name\",\"description\":\"Film description\"," +
                "\"releaseDate\":\"1985-12-28\",\"duration\":-1}";

        mockMvc.perform(post("/films")
                .contentType("application/json").content(invalidFilm)).andDo(
                h -> {
                    assertEquals(400, h.getResponse().getStatus());
                    assertEquals("{\"error\":\"Film duration invalid.\"}",
                            h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/films")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[]", h.getResponse().getContentAsString());
                }
        );
    }

    @SneakyThrows
    @Test
    void validateLikes() {
        String validFilmOne = "{\"name\":\"Film name one\",\"description\":\"Film description one\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":100}";
        String validFilmTwo = "{\"name\":\"Film name two\",\"description\":\"Film description two\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":200}";
        String validFilmThree = "{\"name\":\"Film name three\",\"description\":\"Film description three\"," +
                "\"releaseDate\":\"1895-12-28\",\"duration\":300}";

        mockMvc.perform(post("/films")
                .contentType("application/json").content(validFilmOne)).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"name\":\"Film name one\",\"description\":\"Film description one\"," +
                            "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[]," +
                            "\"countLikes\":0}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );

        mockMvc.perform(post("/films")
                .contentType("application/json").content(validFilmTwo));
        mockMvc.perform(post("/films")
                .contentType("application/json").content(validFilmThree));

        mockMvc.perform(put("/films/1/like/1")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"name\":\"Film name one\",\"description\":\"Film description one\"," +
                            "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[1]," +
                            "\"countLikes\":1}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );

        mockMvc.perform(put("/films/1/like/2"));
        mockMvc.perform(put("/films/3/like/1"));

        mockMvc.perform(get("/films/popular?count=2")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "[{\"name\":\"Film name one\",\"description\":\"Film description one\"," +
                            "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[1,2]," +
                    "\"countLikes\":2}," +
                            "{\"name\":\"Film name three\",\"description\":\"Film description three\"," +
                            "\"releaseDate\":\"1895-12-28\",\"duration\":300,\"id\":3,\"likes\":[1]," +
                            "\"countLikes\":1}]";
                            assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );

        mockMvc.perform(delete("/films/1/like/1")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"name\":\"Film name one\",\"description\":\"Film description one\"," +
                            "\"releaseDate\":\"1895-12-28\",\"duration\":100,\"id\":1,\"likes\":[2]," +
                            "\"countLikes\":1}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
    }
}