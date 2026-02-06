package com.joelmofraga.artists_albums_api.auth.controller;

import com.joelmofraga.artists_albums_api.auth.dto.*;
import com.joelmofraga.artists_albums_api.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthService authService;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        controller = new AuthController(authService);
    }

    @Test
    void login_deveRetornar200_comBody() {
        LoginRequest req = mock(LoginRequest.class);

        LoginResponse respBody = new LoginResponse(
                "access.jwt",
                "Bearer",
                300L,
                "refresh.token"
        );

        when(authService.login(req)).thenReturn(respBody);


        ResponseEntity<LoginResponse> resp = controller.login(req);


        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(respBody);

        verify(authService).login(req);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void register_deveChamarService_eRetornar200SemBody() {

        RegisterRequest req = mock(RegisterRequest.class);


        ResponseEntity<Void> resp = controller.register(req);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isNull();

        verify(authService).register(req);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void refresh_deveChamarService_eRetornar200ComBody() {
        RefreshRequest req = new RefreshRequest("old.refresh");

        RefreshResponse respBody = new RefreshResponse(
                "new.access",
                "Bearer",
                300L,
                "new.refresh"
        );

        when(authService.refresh("old.refresh")).thenReturn(respBody);

        ResponseEntity<RefreshResponse> resp = controller.refresh(req);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(respBody);

        verify(authService).refresh("old.refresh");
        verifyNoMoreInteractions(authService);
    }
}
