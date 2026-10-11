package com.nexusengine.core.admin.infra.controller;

import com.nexusengine.core.admin.infra.dto.S3CallbackResult;
import com.nexusengine.core.admin.infra.dto.S3PolicyResult;
import com.nexusengine.core.admin.infra.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class S3ControllerTest {

    private MockMvc mockMvc;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private S3Controller s3Controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(s3Controller).build();
    }

    @Test
    void presign() throws Exception {
        S3PolicyResult result = new S3PolicyResult("https://s3.example.com/upload", "test.jpg", "test-bucket");

        when(s3Service.presign()).thenReturn(result);

        mockMvc.perform(get("/s3/presign"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.presignedUrl").value("https://s3.example.com/upload"));
    }

    @Test
    void callback() throws Exception {
        S3CallbackResult result = new S3CallbackResult("test.jpg", null, null, "https://s3.example.com/test.jpg");

        when(s3Service.callback(any(HttpServletRequest.class))).thenReturn(result);

        mockMvc.perform(post("/s3/callback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.filename").value("test.jpg"))
                .andExpect(jsonPath("$.data.url").value("https://s3.example.com/test.jpg"));
    }
}
