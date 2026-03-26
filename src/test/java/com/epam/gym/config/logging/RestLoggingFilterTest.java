package com.epam.gym.config.logging;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestLoggingFilterTest {

    private RestLoggingFilter filter = new RestLoggingFilter();

    @Test
    void shouldLogRequestAndResponse() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("test-body".getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {
            res.getWriter().write("response-body");
        };

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
    }
}