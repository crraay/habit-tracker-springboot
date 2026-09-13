package com.kostya.habittracker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.kostya.habittracker.controller.UserClockController;
import com.kostya.habittracker.dto.UserClockResponse;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.BadRequestException;
import com.kostya.habittracker.exception.GlobalExceptionHandler;
import com.kostya.habittracker.model.UserDetails;
import com.kostya.habittracker.service.UserClockService;
import com.kostya.habittracker.util.JwtUtil;

@WebMvcTest(controllers = UserClockController.class)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class UserClockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserClockService userClockService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getMeAuthenticatedReturnsClock() throws Exception {
        when(userClockService.getCurrentUserClock(any(User.class))).thenReturn(
            UserClockResponse.builder()
                .timezone("UTC")
                .today(LocalDate.of(2026, 1, 15))
                .build()
        );

        mockMvc.perform(get("/api/me").with(authentication(auth(authenticatedUser()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.timezone").value("UTC"))
            .andExpect(jsonPath("$.today").value("2026-01-15"));
    }

    @Test
    void patchMeAuthenticatedReturnsUpdatedClock() throws Exception {
        when(userClockService.updateTimezone(any(User.class), eq("Asia/Bangkok"))).thenReturn(
            UserClockResponse.builder()
                .timezone("Asia/Bangkok")
                .today(LocalDate.of(2026, 1, 16))
                .build()
        );

        mockMvc.perform(patch("/api/me")
                .with(authentication(auth(authenticatedUser())))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"timezone\":\"Asia/Bangkok\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.timezone").value("Asia/Bangkok"));
    }

    @Test
    void getMeUnauthenticatedReturns401() throws Exception {
        mockMvc.perform(get("/api/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void patchMeUnknownIanaReturns400() throws Exception {
        when(userClockService.updateTimezone(any(User.class), eq("Not/A/Zone")))
            .thenThrow(new BadRequestException("Unknown timezone: Not/A/Zone"));

        mockMvc.perform(patch("/api/me")
                .with(authentication(auth(authenticatedUser())))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"timezone\":\"Not/A/Zone\"}"))
            .andExpect(status().isBadRequest());
    }

    private User authenticatedUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("secret");
        user.setEmail("testuser@example.com");
        user.setTimezone("UTC");
        return user;
    }

    private UsernamePasswordAuthenticationToken auth(User user) {
        UserDetails details = new UserDetails(user);
        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }
}
