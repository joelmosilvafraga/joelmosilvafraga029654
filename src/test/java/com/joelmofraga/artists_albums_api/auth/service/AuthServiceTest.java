package com.joelmofraga.artists_albums_api.auth.service;

import com.joelmofraga.artists_albums_api.auth.domain.AppUser;
import com.joelmofraga.artists_albums_api.auth.domain.Role;
import com.joelmofraga.artists_albums_api.auth.dto.LoginRequest;
import com.joelmofraga.artists_albums_api.auth.dto.LoginResponse;
import com.joelmofraga.artists_albums_api.auth.dto.RefreshResponse;
import com.joelmofraga.artists_albums_api.auth.dto.RegisterRequest;
import com.joelmofraga.artists_albums_api.auth.repository.RoleRepository;
import com.joelmofraga.artists_albums_api.auth.repository.UserRepository;
import com.joelmofraga.artists_albums_api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class AuthServiceTest {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private RefreshTokenService refreshTokenService;

    private AuthService service;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtService = mock(JwtService.class);
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        refreshTokenService = mock(RefreshTokenService.class, RETURNS_DEEP_STUBS);

        service = new AuthService(
                authenticationManager,
                jwtService,
                userRepository,
                roleRepository,
                passwordEncoder,
                refreshTokenService,
                5L
        );
    }

    @Test
    void login_quandoCredenciaisValidas_deveRetornarAccessTokenERefreshToken() {
        LoginRequest req = mock(LoginRequest.class);
        when(req.getUsername()).thenReturn("joelmo");
        when(req.getPassword()).thenReturn("123");

        AppUser user = new AppUser();
        user.setId(10L);
        user.setUsername("joelmo");
        user.setPasswordHash("$2a$...");

        when(userRepository.findByUsername("joelmo")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("access.jwt");
        when(refreshTokenService.issue(10L).refreshToken()).thenReturn("refresh.token");

        LoginResponse resp = service.login(req);

        assertThat(resp).isNotNull();
        assertThat(resp.accessToken()).isEqualTo("access.jwt");
        assertThat(resp.tokenType()).isEqualTo("Bearer");
        assertThat(resp.expiresInSeconds()).isEqualTo(5L * 60);
        assertThat(resp.refreshToken()).isEqualTo("refresh.token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername("joelmo");
        verify(jwtService).generateToken(user);
        verify(refreshTokenService, atLeastOnce()).issue(10L);

        verifyNoMoreInteractions(authenticationManager, userRepository, jwtService);
        verifyNoMoreInteractions(roleRepository, passwordEncoder);
    }

    @Test
    void login_quandoAuthenticationFalha_deveLancarBadCredentials() {
        LoginRequest req = mock(LoginRequest.class);
        when(req.getUsername()).thenReturn("joelmo");
        when(req.getPassword()).thenReturn("errada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("x"));

        assertThatThrownBy(() -> service.login(req))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid credentials");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(authenticationManager);

        verifyNoInteractions(userRepository, jwtService, refreshTokenService, roleRepository, passwordEncoder);
    }

    @Test
    void login_quandoUsuarioNaoExiste_deveLancarUsernameNotFound() {
        LoginRequest req = mock(LoginRequest.class);
        when(req.getUsername()).thenReturn("naoexiste");
        when(req.getPassword()).thenReturn("123");

        when(userRepository.findByUsername("naoexiste")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(req))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername("naoexiste");
        verifyNoMoreInteractions(authenticationManager, userRepository);

        verifyNoInteractions(jwtService, refreshTokenService, roleRepository, passwordEncoder);
    }

    @Test
    void refresh_quandoTokenValido_deveEmitirNovoAccessENovoRefresh() {
        when(refreshTokenService.validateAndRevoke("old.refresh")).thenReturn(10L);

        AppUser user = new AppUser();
        user.setId(10L);
        user.setUsername("joelmo");

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("new.access");
        when(refreshTokenService.issue(10L).refreshToken()).thenReturn("new.refresh");

        RefreshResponse resp = service.refresh("old.refresh");

        assertThat(resp.accessToken()).isEqualTo("new.access");
        assertThat(resp.tokenType()).isEqualTo("Bearer");
        assertThat(resp.expiresInSeconds()).isEqualTo(5L * 60);
        assertThat(resp.refreshToken()).isEqualTo("new.refresh");

        verify(refreshTokenService).validateAndRevoke("old.refresh");
        verify(userRepository).findById(10L);
        verify(jwtService).generateToken(user);
        verify(refreshTokenService, atLeastOnce()).issue(10L);

        verifyNoMoreInteractions(userRepository, jwtService);
        verifyNoInteractions(authenticationManager, roleRepository, passwordEncoder);
    }

    @Test
    void refresh_quandoUsuarioNaoExiste_deveLancarUsernameNotFound() {
        when(refreshTokenService.validateAndRevoke("old.refresh")).thenReturn(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.refresh("old.refresh"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(refreshTokenService).validateAndRevoke("old.refresh");
        verify(userRepository).findById(99L);
        verifyNoMoreInteractions(userRepository);

        verifyNoInteractions(jwtService, authenticationManager, roleRepository, passwordEncoder);
    }

    @Test
    void register_quandoUsernameVazio_deveLancarIllegalArgument() {
        RegisterRequest req = mock(RegisterRequest.class);
        when(req.getUsername()).thenReturn("   ");
        when(req.getPassword()).thenReturn("123");

        assertThatThrownBy(() -> service.register(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username is required");

        verifyNoInteractions(userRepository, roleRepository, passwordEncoder, authenticationManager, jwtService, refreshTokenService);
    }

    @Test
    void register_quandoPasswordVazio_deveLancarIllegalArgument() {
        RegisterRequest req = mock(RegisterRequest.class);
        when(req.getUsername()).thenReturn("joelmo");
        when(req.getPassword()).thenReturn("");

        assertThatThrownBy(() -> service.register(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password is required");

        verifyNoInteractions(userRepository, roleRepository, passwordEncoder, authenticationManager, jwtService, refreshTokenService);
    }

    @Test
    void register_quandoUsernameJaExiste_deveLancarIllegalState() {
        RegisterRequest req = mock(RegisterRequest.class);
        when(req.getUsername()).thenReturn("joelmo");
        when(req.getPassword()).thenReturn("123");

        when(userRepository.existsByUsername("joelmo")).thenReturn(true);

        assertThatThrownBy(() -> service.register(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Username already exists");

        verify(userRepository).existsByUsername("joelmo");
        verifyNoMoreInteractions(userRepository);

        verifyNoInteractions(roleRepository, passwordEncoder, authenticationManager, jwtService, refreshTokenService);
    }

    @Test
    void register_quandoRoleNaoExiste_deveCriarRolePadrao_eSalvarUserComSenhaHash() {
        RegisterRequest req = mock(RegisterRequest.class);
        when(req.getUsername()).thenReturn("joelmo");
        when(req.getPassword()).thenReturn("123");

        when(userRepository.existsByUsername("joelmo")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        Role roleUser = new Role("ROLE_USER");
        roleUser.setId(2L);
        when(roleRepository.save(any(Role.class))).thenReturn(roleUser);

        when(passwordEncoder.encode("123")).thenReturn("$hash");

        service.register(req);

        verify(userRepository).existsByUsername("joelmo");
        verify(roleRepository).findByName("ROLE_USER");
        verify(roleRepository).save(any(Role.class));
        verify(passwordEncoder).encode("123");
        verify(userRepository).save(any(AppUser.class));

        verifyNoMoreInteractions(userRepository, roleRepository, passwordEncoder);
        verifyNoInteractions(authenticationManager, jwtService, refreshTokenService);
    }

    @Test
    void register_quandoRoleExiste_deveReusarRole_eSalvarUser() {
        RegisterRequest req = mock(RegisterRequest.class);
        when(req.getUsername()).thenReturn("joelmo");
        when(req.getPassword()).thenReturn("123");

        when(userRepository.existsByUsername("joelmo")).thenReturn(false);

        Role roleUser = new Role("ROLE_USER");
        roleUser.setId(2L);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));

        when(passwordEncoder.encode("123")).thenReturn("$hash");

        service.register(req);

        verify(userRepository).existsByUsername("joelmo");
        verify(roleRepository).findByName("ROLE_USER");
        verify(roleRepository, never()).save(any(Role.class));

        verify(passwordEncoder).encode("123");
        verify(userRepository).save(any(AppUser.class));

        verifyNoMoreInteractions(userRepository, roleRepository, passwordEncoder);
        verifyNoInteractions(authenticationManager, jwtService, refreshTokenService);
    }
}
