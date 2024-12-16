package com.employee.database.service.app.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(globalExceptionHandler).build();
    }

    @Test
    void testHandleGenericException() throws Exception {
        // Simulating a generic exception
        Exception exception = new Exception("Generic error occurred");

        ResponseEntity<Map<String, String>> responseEntity = globalExceptionHandler.handleGenericException(exception);

        // Verifying that the correct error message and HTTP status are returned
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).containsEntry("message", "Generic error occurred");
    }

    @Test
    void testHandleRuntimeException() throws Exception {
        // Simulating a runtime exception
        RuntimeException exception = new RuntimeException("Runtime error occurred");

        ResponseEntity<Map<String, String>> responseEntity = globalExceptionHandler.handleRuntimeException(exception);

        // Verifying that the correct error message and HTTP status are returned
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).containsEntry("message", "Runtime error occurred");
    }

//    @Test
//    void testExceptionHandlerWithMockMvc() throws Exception {
//        // Simulating a generic exception using MockMvc
//        mockMvc.perform(get("/some-endpoint")
//                        .param("param", "value"))
//                .andExpect(status().isBadRequest());
//    }
}

