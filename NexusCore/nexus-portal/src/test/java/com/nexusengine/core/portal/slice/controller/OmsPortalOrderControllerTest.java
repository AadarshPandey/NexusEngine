package com.nexusengine.core.portal.slice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusengine.core.portal.controller.OmsPortalOrderController;
import com.nexusengine.core.portal.domain.ConfirmOrderResult;
import com.nexusengine.core.portal.domain.OrderParam;
import com.nexusengine.core.portal.service.OmsPortalOrderService;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OmsPortalOrderController.class)
@Import({SecurityConfig.class, IgnoreUrlsConfig.class})
@ActiveProfiles("test")
public class OmsPortalOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OmsPortalOrderService portalOrderService;

    @MockBean
    private UmsMemberService memberService;
    
    @MockBean
    private com.nexusengine.core.security.component.DynamicSecurityMetadataSource securityMetadataSource;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void generateOrder_Unauthenticated_Returns401() throws Exception {
        OrderParam orderParam = new OrderParam();
        orderParam.setMemberReceiveAddressId(1L);

        mockMvc.perform(post("/portal/order/generateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderParam)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void generateConfirmOrder_Authenticated_Returns200() throws Exception {
        ConfirmOrderResult mockResult = new ConfirmOrderResult();
        when(portalOrderService.generateConfirmOrder(anyList())).thenReturn(mockResult);

        mockMvc.perform(post("/portal/order/generateConfirmOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2,3]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @WithMockUser(username = "testuser")
    void generateOrder_ValidPayload_Returns200() throws Exception {
        OrderParam orderParam = new OrderParam();
        orderParam.setMemberReceiveAddressId(1L);
        orderParam.setPayType(1);
        
        Map<String, Object> mockResult = new HashMap<>();
        com.nexusengine.core.model.OmsOrder mockOrder = new com.nexusengine.core.model.OmsOrder();
        mockOrder.setOrderSn("SN12345");
        mockResult.put("order", mockOrder);
        
        when(portalOrderService.generateOrder(any(OrderParam.class))).thenReturn(mockResult);

        mockMvc.perform(post("/portal/order/generateOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderParam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.order.orderSn").value("SN12345"));
    }
}
