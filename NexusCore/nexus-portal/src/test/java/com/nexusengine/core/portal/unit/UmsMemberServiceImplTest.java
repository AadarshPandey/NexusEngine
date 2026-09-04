package com.nexusengine.core.portal.unit;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import cn.hutool.core.collection.CollUtil;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.common.exception.ApiException;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.UmsMemberLevel;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.domain.MemberDetails;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.service.UmsMemberCacheService;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.UmsMemberLevelRepository;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.UmsMemberRepository;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.security.util.JwtTokenUtil;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.BeforeEach;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.Test;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.InjectMocks;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.Mock;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.security.core.userdetails.UserDetails;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Collections;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.Optional;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.junit.jupiter.api.Assertions.*;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.ArgumentMatchers.any;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.ArgumentMatchers.anyString;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UmsMemberServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private UmsMemberRepository memberRepository;
    @Mock
    private UmsMemberLevelRepository memberLevelRepository;
    @Mock
    private UmsMemberCacheService memberCacheService;

    @InjectMocks
    private UmsMemberServiceImpl memberService;

    private UmsMember testMember;
    private UmsMemberLevel defaultLevel;

    @BeforeEach
    void setUp() {
        testMember = new UmsMember();
        testMember.setId(1L);
        testMember.setUsername("testuser");
        testMember.setPassword("encodedPassword");
        testMember.setPhone("1234567890");

        defaultLevel = new UmsMemberLevel();
        defaultLevel.setId(4L);
        defaultLevel.setDefaultStatus(1);
    }

    @Test
    void getByUsername_CacheHit_ReturnsMember() {
        when(memberCacheService.getMember("testuser")).thenReturn(testMember);

        UmsMember result = memberService.getByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(memberRepository, never()).findByUsername(anyString());
    }

    @Test
    void getByUsername_CacheMiss_FetchesFromDbAndCaches() {
        when(memberCacheService.getMember("testuser")).thenReturn(null);
        when(memberRepository.findByUsername("testuser")).thenReturn(testMember);

        UmsMember result = memberService.getByUsername("testuser");

        assertNotNull(result);
        verify(memberCacheService).setMember(testMember);
    }

    @Test
    void register_Success() {
        when(memberCacheService.getAuthCode("1234567890")).thenReturn("123456");
        when(memberRepository.findByUsernameOrPhone("newuser", "1234567890")).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(memberLevelRepository.findByDefaultStatus(1)).thenReturn(Collections.singletonList(defaultLevel));

        assertDoesNotThrow(() -> memberService.register("newuser", "password", "1234567890", "123456"));
        verify(memberRepository).save(any(UmsMember.class));
    }

    @Test
    void register_InvalidAuthCode_ThrowsException() {
        when(memberCacheService.getAuthCode("1234567890")).thenReturn("654321");

        assertThrows(ApiException.class, () -> 
            memberService.register("newuser", "password", "1234567890", "123456")
        );
        verify(memberRepository, never()).save(any());
    }

    @Test
    void generateAuthCode_ReturnsCodeAndCaches() {
        String code = memberService.generateAuthCode("1234567890");
        
        assertNotNull(code);
        assertEquals(6, code.length());
        verify(memberCacheService).setAuthCode(eq("1234567890"), eq(code));
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        when(memberCacheService.getMember("testuser")).thenReturn(testMember);

        UserDetails userDetails = memberService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        when(memberCacheService.getMember("unknown")).thenReturn(null);
        when(memberRepository.findByUsername("unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> 
            memberService.loadUserByUsername("unknown")
        );
    }

    @Test
    void login_Success_ReturnsToken() {
        when(memberCacheService.getMember("testuser")).thenReturn(testMember);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn("fake-jwt-token");

        String token = memberService.login("testuser", "password");

        assertEquals("fake-jwt-token", token);
    }

    @Test
    void login_BadPassword_ReturnsNull() {
        when(memberCacheService.getMember("testuser")).thenReturn(testMember);
        when(passwordEncoder.matches("wrongpass", "encodedPassword")).thenReturn(false);

        String token = memberService.login("testuser", "wrongpass");

        assertNull(token);
    }
}
