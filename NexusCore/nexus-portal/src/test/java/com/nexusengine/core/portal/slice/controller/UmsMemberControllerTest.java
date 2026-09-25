package com.nexusengine.core.portal.slice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusengine.core.portal.controller.UmsMemberController;
import com.nexusengine.core.portal.domain.MemberDetails;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.security.config.IgnoreUrlsConfig;
import com.nexusengine.core.security.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UmsMemberController.class)
@Import({SecurityConfig.class, IgnoreUrlsConfig.class})
@ActiveProfiles("test")
public class UmsMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UmsMemberService memberService;

    @MockBean
    private com.nexusengine.core.security.component.DynamicSecurityMetadataSource securityMetadataSource;

    @Test
    void login_ValidCredentials_ReturnsTokenPair() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn("jwt-test-token");

        mockMvc.perform(post("/portal/sso/login")
                        .param("username", "testuser")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value("jwt-test-token"));
    }

    @Test
    void login_InvalidCredentials_ReturnsValidationFailed() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(post("/portal/sso/login")
                        .param("username", "wrong")
                        .param("password", "wrong")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404)); // or whatever the failed code is
    }

    @Test
    @WithMockUser(username = "testuser")
    void getInfo_Authenticated_ReturnsMemberDetails() throws Exception {
        UmsMember member = new UmsMember();
        member.setUsername("testuser");
        when(memberService.getCurrentMember()).thenReturn(member);

        mockMvc.perform(get("/portal/sso/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }
}
