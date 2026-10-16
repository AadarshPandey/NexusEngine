package com.nexusengine.core.admin.iam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusengine.core.admin.iam.dto.AdminLoginParam;
import com.nexusengine.core.admin.iam.dto.AdminUserParam;
import com.nexusengine.core.domain.iam.entity.AdminUser;
import com.nexusengine.core.domain.iam.entity.Role;
import com.nexusengine.core.admin.iam.service.AdminUserService;
import com.nexusengine.core.admin.iam.service.RoleService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminUserService adminService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AdminUserController adminUserController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
        ReflectionTestUtils.setField(adminUserController, "tokenHeader", "Authorization");
        ReflectionTestUtils.setField(adminUserController, "tokenHead", "Bearer ");
    }

    @Test
    void register() throws Exception {
        AdminUserParam param = new AdminUserParam("testuser", "password", null, null, null, null);

        AdminUser admin = new AdminUser();
        admin.setUsername("testuser");

        when(adminService.register(any())).thenReturn(admin);

        mockMvc.perform(post("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void login() throws Exception {
        AdminLoginParam param = new AdminLoginParam("testuser", "password");

        when(adminService.login("testuser", "password")).thenReturn("dummy-token");

        mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"))
                .andExpect(jsonPath("$.tokenHead").value("Bearer "));
    }

    @Test
    void getAdminInfo() throws Exception {
        AdminUser admin = new AdminUser();
        admin.setId(1L);
        admin.setUsername("admin");

        Role role = new Role();
        role.setName("ROLE_ADMIN");

        when(adminService.getAdminByUsername("admin")).thenReturn(admin);
        when(roleService.getMenuList(1L)).thenReturn(Collections.emptyList());
        when(adminService.getRoleList(1L)).thenReturn(Collections.singletonList(role));

        Principal principal = () -> "admin";

        mockMvc.perform(get("/admin/info").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"));
    }
}
