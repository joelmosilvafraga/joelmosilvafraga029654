package com.joelmofraga.artists_albums_api.security;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final int LIMIT = 10;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final JsonMapper jsonMapper;
    private final String loginPath;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public LoginRateLimitFilter(JsonMapper jsonMapper, String loginPath) {
        this.jsonMapper = jsonMapper;
        this.loginPath = loginPath;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(HttpMethod.POST.matches(request.getMethod()) && pathMatches(request, loginPath));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        CachedBodyHttpServletRequest wrapped = new CachedBodyHttpServletRequest(request);

        String username = extractUsername(wrapped);
        String ip = resolveClientIp(wrapped);

        String key = StringUtils.hasText(username)
                ? "login:user:" + username.trim().toLowerCase()
                : "login:ip:" + ip;

        Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(wrapped, response);
            return;
        }

        response.setStatus(429);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
                {
                  "error": "too_many_requests",
                  "message": "Rate limit excedido: até 10 requisições por minuto."
                }
                """);
    }

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.classic(LIMIT, Refill.intervally(LIMIT, WINDOW));
        return Bucket.builder().addLimit(limit).build();
    }

    private boolean pathMatches(HttpServletRequest request, String expectedPath) {
        String uri = request.getRequestURI();
        return uri != null && uri.equals(expectedPath);
    }

    private String extractUsername(CachedBodyHttpServletRequest request) {
        try {
            byte[] body = request.getCachedBody();
            if (body == null || body.length == 0) return null;

            JsonNode node = jsonMapper.readTree(body);
            JsonNode usernameNode = node.get("username");
            if (usernameNode == null || usernameNode.isNull()) return null;

            String username = usernameNode.asText();
            return StringUtils.hasText(username) ? username : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xff)) {
            String first = xff.split(",")[0].trim();
            if (StringUtils.hasText(first)) return first;
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) return realIp.trim();

        String remote = request.getRemoteAddr();
        return StringUtils.hasText(remote) ? remote : "unknown";
    }
}
