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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    // ✅ Padronize o banco com ROLE_USER / ROLE_ADMIN
    private static final String DEFAULT_ROLE_CODE = "ROLE_USER";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    private final long accessTokenTtlSeconds;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            RefreshTokenService refreshTokenService,
            @Value("${app.security.jwt.expiration-minutes:5}") long accessTokenTtlMinutes
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenTtlSeconds = accessTokenTtlMinutes * 60;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid credentials");
        }

        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.issue(user.getId()).refreshToken();

        return new LoginResponse(accessToken, "Bearer", accessTokenTtlSeconds, refreshToken);
    }

    @Transactional
    public RefreshResponse refresh(String rawRefreshToken) {
        Long userId = refreshTokenService.validateAndRevoke(rawRefreshToken);

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = refreshTokenService.issue(userId).refreshToken();

        return new RefreshResponse(newAccessToken, "Bearer", accessTokenTtlSeconds, newRefreshToken);
    }

    @Transactional
    public void register(RegisterRequest request) {
        String username = request.getUsername();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists");
        }

        Role roleUser = roleRepository.findByName(DEFAULT_ROLE_CODE)
                .orElseGet(() -> roleRepository.save(new Role(DEFAULT_ROLE_CODE)));

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        user.addRole(roleUser);

        userRepository.save(user);
    }
}
