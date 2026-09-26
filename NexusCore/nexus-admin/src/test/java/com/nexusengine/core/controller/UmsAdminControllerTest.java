package com.nexusengine.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.dto.UmsAdminLoginParam;
import com.nexusengine.core.dto.UmsAdminParam;
import com.nexusengine.core.model.UmsAdmin;
import com.nexusengine.core.model.UmsRole;
import com.nexusengine.core.service.UmsAdminService;
import com.nexusengine.core.service.UmsRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UmsAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UmsAdminService adminService;

    @Mock
    private UmsRoleService roleService;

    @InjectMocks
    private UmsAdminController umsAdminController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(umsAdminController).build();
        ReflectionTestUtils.setField(umsAdminController, "tokenHeader", "Authorization");
        ReflectionTestUtils.setField(umsAdminController, "tokenHead", "Bearer ");
    }

    @Test
    void register() throws Exception {
        UmsAdminParam param = new UmsAdminParam();
        param.setUsername("testuser");
        param.setPassword("password");

        UmsAdmin admin = new UmsAdmin();
        admin.setUsername("testuser");

        when(adminService.register(any())).thenReturn(admin);

        mockMvc.perform(post("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void login() throws Exception {
        UmsAdminLoginParam param = new UmsAdminLoginParam();
        param.setUsername("testuser");
        param.setPassword("password");

        when(adminService.login("testuser", "password")).thenReturn("dummy-token");

        mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value("dummy-token"))
                .andExpect(jsonPath("$.data.tokenHead").value("Bearer "));
    }

    @Test
    void getAdminInfo() throws Exception {
        UmsAdmin admin = new UmsAdmin();
        admin.setId(1L);
        admin.setUsername("admin");
        
        UmsRole role = new UmsRole();
        role.setName("ROLE_ADMIN");

        when(adminService.getAdminByUsername("admin")).thenReturn(admin);
        when(roleService.getMenuList(1L)).thenReturn(Collections.emptyList());
        when(adminService.getRoleList(1L)).thenReturn(Collections.singletonList(role));

        Principal principal = () -> "admin";

        mockMvc.perform(get("/admin/info").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.roles[0]").value("ROLE_ADMIN"));
    }
}
