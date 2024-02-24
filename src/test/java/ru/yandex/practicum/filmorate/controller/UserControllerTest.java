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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() throws Exception {
        mockMvc.perform(delete("/users"));
    }

    @SneakyThrows
    @Test
    void validateUserOk() {
        String validUser = "{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                "\"email\":\"user@mail.ru\",\"birthday\":\"2000-04-01\"}";

        mockMvc.perform(post("/users")
                .contentType("application/json").content(validUser)).andDo(
                h -> {
                    System.out.println(h.getResponse());
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                            "\"email\":\"user@mail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/users/1")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                            "\"email\":\"user@mail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        validUser = "{\"login\":\"UpdatedUserLogin\",\"name\":\"Updated user name\"," +
                "\"email\":\"user@gmail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}";
        mockMvc.perform(put("/users")
                .contentType("application/json").content(validUser)).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"login\":\"UpdatedUserLogin\",\"name\":\"Updated user name\"," +
                            "\"email\":\"user@gmail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/users")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "[{\"login\":\"UpdatedUserLogin\",\"name\":\"Updated user name\"," +
                            "\"email\":\"user@gmail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}]";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        validUser = "{\"login\":\"UpdatedUserLogin\",\"name\":\"Updated user name\"," +
                "\"email\":\"user@gmail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}";
        mockMvc.perform(delete("/users")
                .contentType("application/json").content(validUser)).andDo(
                h -> assertEquals(200, h.getResponse().getStatus())
        );
        mockMvc.perform(get("/users")
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
        String validUser = "{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                "\"email\":\"user@mail.ru\",\"birthday\":\"2000-04-01\"}";

        mockMvc.perform(post("/users")
                .contentType("application/json").content(validUser)).andDo(
                h -> {
                    System.out.println(h.getResponse());
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                            "\"email\":\"user@mail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/users")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "[{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                            "\"email\":\"user@mail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}]";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        String invalidUser = "{\"login\":\"UpdatedUserLogin\",\"name\":\"Updated user name\"," +
                "\"email\":\"user@gmail.ru\",\"birthday\":\"2000-04-01\",\"id\":0,\"friends\":[]}";
        mockMvc.perform(put("/users")
                .contentType("application/json").content(validUser)).andDo(
                h -> {
                    assertEquals(404, h.getResponse().getStatus());
                    String expectedResponse = "{\"error\":\"User not found.\"}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
        validUser = "{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                "\"email\":\"user@mail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}";
        mockMvc.perform(delete("/users")
                .contentType("application/json").content(validUser)).andDo(
                h -> assertEquals(200, h.getResponse().getStatus())
        );
    }

    @SneakyThrows
    @Test
    void validateInvalidUserEmail() {
        String invalidUser = "{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                "\"email\":\"user&mail.ru\",\"birthday\":\"2000-04-01\"}";

        mockMvc.perform(post("/users")
                .contentType("application/json").content(invalidUser)).andDo(
                h -> {
                    assertEquals(400, h.getResponse().getStatus());
                    assertEquals("{\"error\":\"User Email invalid.\"}",
                            h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/users")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[]", h.getResponse().getContentAsString());
                }
        );
    }

    @SneakyThrows
    @Test
    void validateInvalidUserLogin() {
        String invalidUser = "{\"login\":\"User Login\",\"name\":\"User name\"," +
                "\"email\":\"user@mail.ru\",\"birthday\":\"2000-04-01\"}";

        mockMvc.perform(post("/users")
                .contentType("application/json").content(invalidUser)).andDo(
                h -> {
                    assertEquals(400, h.getResponse().getStatus());
                    assertEquals("{\"error\":\"User login invalid.\"}",
                            h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/users")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[]", h.getResponse().getContentAsString());
                }
        );
    }

    @SneakyThrows
    @Test
    void validateInvalidUserBirthday() {
        String invalidUser = "{\"login\":\"UserLogin\",\"name\":\"User name\"," +
                "\"email\":\"user@mail.ru\",\"birthday\":\"3000-04-01\"}";

        mockMvc.perform(post("/users")
                .contentType("application/json").content(invalidUser)).andDo(
                h -> {
                    assertEquals(400, h.getResponse().getStatus());
                    assertEquals("{\"error\":\"User birthday invalid.\"}",
                            h.getResponse().getContentAsString());
                }
        );
        mockMvc.perform(get("/users")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    assertEquals("[]", h.getResponse().getContentAsString());
                }
        );
    }

    @SneakyThrows
    @Test
    void validateFriends() {
        String validUserOne = "{\"login\":\"UserLoginOne\",\"name\":\"One user name\"," +
                "\"email\":\"userOne@mail.ru\",\"birthday\":\"2000-04-01\"}";
        String validUserTwo = "{\"login\":\"UserLoginTwo\",\"name\":\"Two user name\"," +
                "\"email\":\"userTwo@mail.ru\",\"birthday\":\"2000-04-02\"}";
        String validUserThree = "{\"login\":\"UserLoginThree\",\"name\":\"Three user name\"," +
                "\"email\":\"userThree@mail.ru\",\"birthday\":\"2000-04-03\"}";

        mockMvc.perform(post("/users").contentType("application/json").content(validUserOne));
        mockMvc.perform(post("/users").contentType("application/json").content(validUserTwo));
        mockMvc.perform(post("/users").contentType("application/json").content(validUserThree));

        mockMvc.perform(put("/users/1/friends/3")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"login\":\"UserLoginOne\",\"name\":\"One user name\"," +
                            "\"email\":\"userOne@mail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[3]}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );

        mockMvc.perform(get("/users/1/friends")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "[{\"login\":\"UserLoginThree\",\"name\":\"Three user name\"," +
                            "\"email\":\"userThree@mail.ru\",\"birthday\":\"2000-04-03\",\"id\":3,\"friends\":[1]}]";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );

        mockMvc.perform(put("/users/2/friends/3")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"login\":\"UserLoginTwo\",\"name\":\"Two user name\"," +
                            "\"email\":\"userTwo@mail.ru\",\"birthday\":\"2000-04-02\",\"id\":2,\"friends\":[3]}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );

        mockMvc.perform(get("/users/1/friends/common/2")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "[{\"login\":\"UserLoginThree\",\"name\":\"Three user name\"," +
                            "\"email\":\"userThree@mail.ru\",\"birthday\":\"2000-04-03\",\"id\":3,\"friends\":[1,2]}]";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );

        mockMvc.perform(delete("/users/1/friends/3")
                .contentType("application/json")).andDo(
                h -> {
                    assertEquals(200, h.getResponse().getStatus());
                    String expectedResponse = "{\"login\":\"UserLoginOne\",\"name\":\"One user name\"," +
                            "\"email\":\"userOne@mail.ru\",\"birthday\":\"2000-04-01\",\"id\":1,\"friends\":[]}";
                    assertEquals(expectedResponse, h.getResponse().getContentAsString());
                }
        );
    }
}